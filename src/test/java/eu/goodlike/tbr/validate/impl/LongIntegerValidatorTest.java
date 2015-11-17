package eu.goodlike.tbr.validate.impl;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LongIntegerValidatorTest {

    private LongIntegerValidator validator;

    @Before
    public void setup() {
        validator = new LongIntegerValidator();
    }

    @Test
    public void tryMoreThanWithMoreThan_shouldBeTrue() {
        assertThat(validator.isMoreThan(10).test(11L)).isTrue();
    }

    @Test
    public void tryMoreThanWithExactValue_shouldBeFalse() {
        assertThat(validator.isMoreThan(10).test(10L)).isFalse();
    }

    @Test
    public void tryMoreThanWithLessThan_shouldBeFalse() {
        assertThat(validator.isMoreThan(10).test(9L)).isFalse();
    }

    @Test
    public void tryLessThanWithMoreThan_shouldBeFalse() {
        assertThat(validator.isLessThan(10).test(11L)).isFalse();
    }

    @Test
    public void tryLessThanWithExactValue_shouldBeFalse() {
        assertThat(validator.isLessThan(10).test(10L)).isFalse();
    }

    @Test
    public void tryLessThanWithLessThan_shouldBeTrue() {
        assertThat(validator.isLessThan(10).test(9L)).isTrue();
    }

    @Test
    public void tryAtLeastWithAtLeast_shouldBeTrue() {
        assertThat(validator.isAtLeast(10).test(11L)).isTrue();
    }

    @Test
    public void tryAtLeastWithExactValue_shouldBeTrue() {
        assertThat(validator.isAtLeast(10).test(10L)).isTrue();
    }

    @Test
    public void tryAtLeastWithLess_shouldBeTrue() {
        assertThat(validator.isAtLeast(10).test(9L)).isFalse();
    }

    @Test
    public void tryAtMostWithMore_shouldBeTrue() {
        assertThat(validator.isAtMost(10).test(11L)).isFalse();
    }

    @Test
    public void tryAtMostWithExactValue_shouldBeTrue() {
        assertThat(validator.isAtMost(10).test(10L)).isTrue();
    }

    @Test
    public void tryAtMostWithAtMost_shouldBeTrue() {
        assertThat(validator.isAtMost(10).test(9L)).isTrue();
    }

    @Test
    public void tryBetweenWithBetween_shouldBeTrue() {
        assertThat(validator.isBetween(1, 10).test(5L)).isTrue();
    }

    @Test
    public void tryBetweenWithLowestBetween_shouldBeTrue() {
        assertThat(validator.isBetween(1, 10).test(1L)).isTrue();
    }

    @Test
    public void tryBetweenWithHighestBetween_shouldBeTrue() {
        assertThat(validator.isBetween(1, 10).test(10L)).isTrue();
    }

    @Test
    public void tryBetweenWithLessThanLowest_shouldBeFalse() {
        assertThat(validator.isBetween(1, 10).test(0L)).isFalse();
    }

    @Test
    public void tryBetweenWithMoreThanHighest_shouldBeFalse() {
        assertThat(validator.isBetween(1, 10).test(11L)).isFalse();
    }

    @Test
    public void tryDayOfMonthWithDayOfMonth_shouldBeTrue() {
        assertThat(validator.isDayOfMonth().test(15L)).isTrue();
    }

    @Test
    public void tryDayOfMonthWithNotDayOfMonth_shouldBeFalse() {
        assertThat(validator.isDayOfMonth().test(150L)).isFalse();
    }

    @Test
    public void tryHourOfDayWithHourOfDay_shouldBeTrue() {
        assertThat(validator.isHourOfDay().test(15L)).isTrue();
    }

    @Test
    public void tryHourOfDayWithNotHourOfDay_shouldBeFalse() {
        assertThat(validator.isHourOfDay().test(150L)).isFalse();
    }

    @Test
    public void tryMinuteOfHourWithMinuteOfHour_shouldBeTrue() {
        assertThat(validator.isMinuteOfHour().test(15L)).isTrue();
    }

    @Test
    public void tryMinuteOfHourWithNotMinuteOfHour_shouldBeFalse() {
        assertThat(validator.isMinuteOfHour().test(150L)).isFalse();
    }

    @Test
    public void tryIsMonthOfYearWithMonth_shouldBeTrue() {
        assertThat(validator.isMonthOfYear().test(10L)).isTrue();
    }

    @Test
    public void tryIsMonthOfYearWithNotMonth_shouldBeFalse() {
        assertThat(validator.isMonthOfYear().test(13L)).isFalse();
    }

    @Test
    public void tryIsDayOfMonthSpecificWithCorrectDay_shouldBeTrue() {
        assertThat(validator.isDayOfMonth(2015, 2).test(10L)).isTrue();
    }

    @Test
    public void tryIsDayOfMonthSpecificWithIncorrectDay_shouldBeFalse() {
        assertThat(validator.isDayOfMonth(2015, 2).test(30L)).isFalse();
    }

    @Test
    public void tryIsDayOfMonthSpecificWithExtraDay_shouldBeTrue() {
        assertThat(validator.isDayOfMonth(2016, 2).test(29L)).isTrue();
    }

}
