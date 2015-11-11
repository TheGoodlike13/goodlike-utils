package eu.goodlike.v2.validate.impl;

import eu.goodlike.test.Fake;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringValidatorTest {

    private StringValidator validator;

    @Before
    public void setup() {
        validator = new StringValidator();
    }

    @Test
    public void tryEmptyWithEmpty_shouldBeTrue() {
        assertThat(validator.isEmpty().test("")).isTrue();
    }

    @Test
    public void tryEmptyWithBlank_shouldBeFalse() {
        assertThat(validator.isEmpty().test(" ")).isFalse();
    }

    @Test
    public void tryEmptyWithNotEmpty_shouldBeFalse() {
        assertThat(validator.isEmpty().test("NotEmpty")).isFalse();
    }

    @Test
    public void tryBlankWithEmpty_shouldBeTrue() {
        assertThat(validator.isBlank().test("")).isTrue();
    }

    @Test
    public void tryBlankWithBlank_shouldBeTrue() {
        assertThat(validator.isBlank().test(" ")).isTrue();
    }

    @Test
    public void tryBlankWithNotEmpty_shouldBeFalse() {
        assertThat(validator.isBlank().test("NotEmpty")).isFalse();
    }

    @Test
    public void tryNoLargerThanWithNoLargerThan_shouldBeTrue() {
        assertThat(validator.isNoLargerThan(5).test("yes")).isTrue();
    }

    @Test
    public void tryNoLargerThanWithExactSize_shouldBeTrue() {
        assertThat(validator.isNoLargerThan(5).test("size5")).isTrue();
    }

    @Test
    public void tryNoLargerThanWithLargerThan_shouldBeFalse() {
        assertThat(validator.isNoLargerThan(5).test("sizeMoreThan5")).isFalse();
    }

    @Test
    public void tryEmailWithEmail_shouldBeTrue() {
        assertThat(validator.isEmail().test(Fake.email(1))).isTrue();
    }

    @Test
    public void tryEmailWithNotEmail_shouldBeFalse() {
        assertThat(validator.isEmail().test(Fake.name(1))).isFalse();
    }

    @Test
    public void tryCommaSeparatedListOfIntegersWithCommaSeparatedListOfIntegers_shouldBeTrue() {
        assertThat(validator.isCommaSeparatedListOfIntegers().test("1,2,3")).isTrue();
    }

    @Test
    public void tryCommaSeparatedListOfIntegersWithCommaAndSpaceSeparatedListOfIntegers_shouldBeFalse() {
        assertThat(validator.isCommaSeparatedListOfIntegers().test("1, 2, 3")).isFalse();
    }

    @Test
    public void tryCommaSeparatedListOfIntegersWithNotCommaSeparatedListOfIntegers_shouldBeFalse() {
        assertThat(validator.isCommaSeparatedListOfIntegers().test("1.2.3")).isFalse();
    }

    @Test
    public void tryCommaSeparatedListOfIntegersWithCommaSeparatedListOfNegativeIntegers_shouldBeFalse() {
        assertThat(validator.isCommaSeparatedListOfIntegers().test("-1,2,-3")).isFalse();
    }

    @Test
    public void tryCommaSeparatedListOfIntegersWithZero_shouldBeFalse() {
        assertThat(validator.isCommaSeparatedListOfIntegers().test("0")).isFalse();
    }

    @Test
    public void tryCommaSeparatedListOfIntegersWithOne_shouldBeTrue() {
        assertThat(validator.isCommaSeparatedListOfIntegers().test("1")).isTrue();
    }

    @Test
    public void tryNoSmallerThanWithSmaller_shouldBeFalse() {
        assertThat(validator.isNoSmallerThan(10).test("small")).isFalse();
    }

    @Test
    public void tryNoSmallerThanWithExact_shouldBeTrue() {
        String test = "test";
        assertThat(validator.isNoSmallerThan(test.length()).test(test)).isTrue();
    }

    @Test
    public void tryNoSmallerThanWithBigger_shouldBeTrue() {
        assertThat(validator.isNoSmallerThan(2).test("large")).isTrue();
    }

    @Test
    public void tryIntegerWithInteger_shouldBeTrue() {
        assertThat(validator.isInteger().test("123456789")).isTrue();
    }

    @Test
    public void tryIntegerWithNegativeInteger_shouldBeTrue() {
        assertThat(validator.isInteger().test("-123456789")).isTrue();
    }

    @Test
    public void tryIntegerWithNotInteger_shouldBeFalse() {
        assertThat(validator.isInteger().test("not integer")).isFalse();
    }

    @Test
    public void tryIntegerCustomWithPassingInteger_shouldBeTrue() {
        assertThat(validator.isInteger(i -> i >= 0).test("123456789")).isTrue();
    }

    @Test
    public void tryIntegerCustomWithNotPassingInteger_shouldBeFalse() {
        assertThat(validator.isInteger(i -> i >= 0).test("-123456789")).isFalse();
    }

    @Test
    public void tryIntegerCustomWithNotInteger_shouldBeFalse() {
        assertThat(validator.isInteger(i -> i >= 0).test("not integer")).isFalse();
    }

    @Test
    public void tryDateWithDate_shouldBeTrue() {
        assertThat(validator.isDate().test("2015-11-10")).isTrue();
    }

    @Test
    public void tryDateWithNotDate_shouldBeFalse() {
        assertThat(validator.isDate().test("not date")).isFalse();
    }

    @Test
    public void tryDateWithPoorlyFormattedDate_shouldBeFalse() {
        assertThat(validator.isDate().test("2015-1-15")).isFalse();
    }

    @Test
    public void tryDateWithImpossibleDate_shouldBeFalse() {
        assertThat(validator.isDate().test("2015-13-15")).isFalse();
    }

    @Test
    public void tryDateWithNegativeZeroYears_shouldBeFalse() {
        assertThat(validator.isDate().test("-0000-11-10")).isFalse();
    }

    @Test
    public void tryDateWithManyLongYearsWithPlus_shouldBeTrue() {
        assertThat(validator.isDate().test("+12345-11-10")).isTrue();
    }

    @Test
    public void tryDateWithManyLongYearsWithoutPlus_shouldBeFalse() {
        assertThat(validator.isDate().test("12345-11-10")).isFalse();
    }

    @Test
    public void tryDateWithFourLongYearsWithPlus_shouldBeFalse() {
        assertThat(validator.isDate().test("+2015-11-10")).isFalse();
    }

    @Test
    public void tryExactlyOfSizeWithExactSize_shouldBeTrue() {
        assertThat(validator.isExactlyOfSize(4).test("test")).isTrue();
    }

    @Test
    public void tryExactlyOfSizeWithDifferentSize_shouldBeFalse() {
        assertThat(validator.isExactlyOfSize(10).test("test")).isFalse();
    }

}
