package eu.goodlike.v2.validate.impl;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CharArrayValidatorTest {

    private final char[] testArray = {'t', 'e', 's', 't', 'A', 'r', 'r', 'a', 'y'};

    private CharArrayValidator validator;

    @Before
    public void setup() {
        validator = new CharArrayValidator();
    }

    @Test
    public void tryEqualsSameArray_shouldBeTrue() {
        assertThat(validator.isEqual(testArray).test(testArray)).isTrue();
    }

    @Test
    public void tryEqualsSameArrayDifferentInstance_shouldBeTrue() {
        assertThat(validator.isEqual(testArray).test("testArray".toCharArray())).isTrue();
    }

    @Test
    public void tryEqualsDifferentArrays_shouldBeFalse() {
        assertThat(validator.isEqual(testArray).test("notTestArray".toCharArray())).isFalse();
    }

    @Test
    public void tryEqualsEquivalentString_shouldBeTrue() {
        assertThat(validator.isEqual("testArray").test(testArray)).isTrue();
    }

    @Test
    public void tryEqualsDifferentString_shouldBeFalse() {
        assertThat(validator.isEqual("notTestArray").test(testArray)).isFalse();
    }

    @Test
    public void tryEmptyOnEmptyArray_shouldBeTrue() {
        assertThat(validator.isEmpty().test(new char[0])).isTrue();
    }

    @Test
    public void tryEmptyOnNonEmptyArray_shouldBeFalse() {
        assertThat(validator.isEmpty().test(testArray)).isFalse();
    }

    @Test
    public void tryNoLargerThan5OnArrayOf4_shouldBeTrue() {
        assertThat(validator.isNoLargerThan(5).test(new char[4])).isTrue();
    }

    @Test
    public void tryNoLargerThan5OnArrayOf5_shouldBeTrue() {
        assertThat(validator.isNoLargerThan(5).test(new char[5])).isTrue();
    }

    @Test
    public void tryNoLargerThan5OnArrayOf6_shouldBeFalse() {
        assertThat(validator.isNoLargerThan(5).test(new char[6])).isFalse();
    }

    @Test
    public void tryAllMatchOnEmptyArray_shouldBeTrue() {
        assertThat(validator.allMatch(Character::isDigit).test(new char[0])).isTrue();
    }

    @Test
    public void tryAllMatchOnMatchingArray_shouldBeTrue() {
        assertThat(validator.allMatch(Character::isDigit).test("123".toCharArray())).isTrue();
    }

    @Test
    public void tryAllMatchOnNonMatchingArray_shouldBeFalse() {
        assertThat(validator.allMatch(Character::isDigit).test("123a".toCharArray())).isFalse();
    }

    @Test
    public void tryAnyMatchOnEmptyArray_shouldBeFalse() {
        assertThat(validator.anyMatch(Character::isDigit).test(new char[0])).isFalse();
    }

    @Test
    public void tryAnyMatchOnArrayWithMatch_shouldBeTrue() {
        assertThat(validator.anyMatch(Character::isDigit).test("a1b2c3".toCharArray())).isTrue();
    }

    @Test
    public void tryAnyMatchOnArrayWithoutMatch_shouldBeFalse() {
        assertThat(validator.anyMatch(Character::isDigit).test("abc".toCharArray())).isFalse();
    }

    @Test
    public void tryBlankWithBlank_shouldBeTrue() {
        char[] blank = {' ', ' '};
        assertThat(validator.isBlank().test(blank)).isTrue();
    }

    @Test
    public void tryBlankWithNotBlank_shouldBeFalse() {
        assertThat(validator.isBlank().test(testArray)).isFalse();
    }

}
