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

}
