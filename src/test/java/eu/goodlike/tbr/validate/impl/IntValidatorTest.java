package eu.goodlike.tbr.validate.impl;

import eu.goodlike.functional.some.Some;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IntValidatorTest {

    private IntValidator validator;
    private List<Integer> actionTester;

    @Before
    public void setup() {
        validator = new IntValidator();
        actionTester = new ArrayList<>();
    }

    @Test
    public void tryPassesWithValid_shouldBeTrue() {
        assertThat(validator.passes(i -> i == 10).test(10)).isTrue();
    }

    @Test
    public void tryPassesWithInvalid_shouldBeFalse() {
        assertThat(validator.passes(i -> i == 10).test(11)).isFalse();
    }

    @Test
    public void tryContainedInArrayWithContained_shouldBeTrue() {
        assertThat(validator.isContainedIn(1, 2, 3).test(2)).isTrue();
    }

    @Test
    public void tryContainedInArrayWithContained_shouldBeFalse() {
        assertThat(validator.isContainedIn(1, 2, 3).test(4)).isFalse();
    }

    @Test
    public void tryContainedInCollectionWithContained_shouldBeTrue() {
        assertThat(validator.isContainedIn(Some.ints().oneUpTo(3)).test(2)).isTrue();
    }

    @Test
    public void tryContainedInCollectionWithContained_shouldBeFalse() {
        assertThat(validator.isContainedIn(Some.ints().oneUpTo(3)).test(4)).isFalse();
    }

    @Test
    public void tryMoreThanWithMoreThan_shouldBeTrue() {
        assertThat(validator.isMoreThan(10).test(11)).isTrue();
    }

    @Test
    public void tryMoreThanWithExactValue_shouldBeFalse() {
        assertThat(validator.isMoreThan(10).test(10)).isFalse();
    }

    @Test
    public void tryMoreThanWithLessThan_shouldBeFalse() {
        assertThat(validator.isMoreThan(10).test(9)).isFalse();
    }

    @Test
    public void tryLessThanWithMoreThan_shouldBeFalse() {
        assertThat(validator.isLessThan(10).test(11)).isFalse();
    }

    @Test
    public void tryLessThanWithExactValue_shouldBeFalse() {
        assertThat(validator.isLessThan(10).test(10)).isFalse();
    }

    @Test
    public void tryLessThanWithLessThan_shouldBeTrue() {
        assertThat(validator.isLessThan(10).test(9)).isTrue();
    }

    @Test
    public void tryAtLeastWithAtLeast_shouldBeTrue() {
        assertThat(validator.isAtLeast(10).test(11)).isTrue();
    }

    @Test
    public void tryAtLeastWithExactValue_shouldBeTrue() {
        assertThat(validator.isAtLeast(10).test(10)).isTrue();
    }

    @Test
    public void tryAtLeastWithLess_shouldBeTrue() {
        assertThat(validator.isAtLeast(10).test(9)).isFalse();
    }

    @Test
    public void tryAtMostWithMore_shouldBeTrue() {
        assertThat(validator.isAtMost(10).test(11)).isFalse();
    }

    @Test
    public void tryAtMostWithExactValue_shouldBeTrue() {
        assertThat(validator.isAtMost(10).test(10)).isTrue();
    }

    @Test
    public void tryAtMostWithAtMost_shouldBeTrue() {
        assertThat(validator.isAtMost(10).test(9)).isTrue();
    }

    @Test
    public void tryBetweenWithBetween_shouldBeTrue() {
        assertThat(validator.isBetween(1, 10).test(5)).isTrue();
    }

    @Test
    public void tryBetweenWithLowestBetween_shouldBeTrue() {
        assertThat(validator.isBetween(1, 10).test(1)).isTrue();
    }

    @Test
    public void tryBetweenWithHighestBetween_shouldBeTrue() {
        assertThat(validator.isBetween(1, 10).test(10)).isTrue();
    }

    @Test
    public void tryBetweenWithLessThanLowest_shouldBeFalse() {
        assertThat(validator.isBetween(1, 10).test(0)).isFalse();
    }

    @Test
    public void tryBetweenWithMoreThanHighest_shouldBeFalse() {
        assertThat(validator.isBetween(1, 10).test(11)).isFalse();
    }

    @Test
    public void tryDayOfMonthWithDayOfMonth_shouldBeTrue() {
        assertThat(validator.isDayOfMonth().test(15)).isTrue();
    }

    @Test
    public void tryDayOfMonthWithNotDayOfMonth_shouldBeFalse() {
        assertThat(validator.isDayOfMonth().test(150)).isFalse();
    }

    @Test
    public void tryHourOfDayWithHourOfDay_shouldBeTrue() {
        assertThat(validator.isHourOfDay().test(15)).isTrue();
    }

    @Test
    public void tryHourOfDayWithNotHourOfDay_shouldBeFalse() {
        assertThat(validator.isHourOfDay().test(150)).isFalse();
    }

    @Test
    public void tryMinuteOfHourWithMinuteOfHour_shouldBeTrue() {
        assertThat(validator.isMinuteOfHour().test(15)).isTrue();
    }

    @Test
    public void tryMinuteOfHourWithNotMinuteOfHour_shouldBeFalse() {
        assertThat(validator.isMinuteOfHour().test(150)).isFalse();
    }

    @Test
    public void tryLeftAndRightWithBoth_shouldBeTrue() {
        assertThat(validator.isMoreThan(1).and().isLessThan(5).test(3)).isTrue();
    }

    @Test
    public void tryLeftAndRightWithLeftOnly_shouldBeFalse() {
        assertThat(validator.isMoreThan(1).and().isLessThan(5).test(6)).isFalse();
    }

    @Test
    public void tryLeftAndRightWithRightOnly_shouldBeFalse() {
        assertThat(validator.isMoreThan(1).and().isLessThan(5).test(0)).isFalse();
    }

    @Test
    public void tryLeftAndRightWithNeither_shouldBeFalse() {
        assertThat(validator.isMoreThan(5).and().isLessThan(1).test(3)).isFalse();
    }

    @Test
    public void tryLeftOrRightWithBoth_shouldBeTrue() {
        assertThat(validator.isMoreThan(1).or().isLessThan(5).test(3)).isTrue();
    }

    @Test
    public void tryLeftOrRightWithLeftOnly_shouldBeTrue() {
        assertThat(validator.isMoreThan(5).or().isLessThan(1).test(6)).isTrue();
    }

    @Test
    public void tryLeftOrRightWithRightOnly_shouldBeTrue() {
        assertThat(validator.isMoreThan(5).or().isLessThan(1).test(0)).isTrue();
    }

    @Test
    public void tryLeftOrRightWithNeither_shouldBeFalse() {
        assertThat(validator.isMoreThan(5).or().isLessThan(1).test(3)).isFalse();
    }

    @Test
    public void tryNotWithOtherwiseInvalid_shouldBeTrue() {
        assertThat(validator.not().isLessThan(5).test(6)).isTrue();
    }

    @Test
    public void tryNotWithOtherwiseValid_shouldBeFalse() {
        assertThat(validator.not().isLessThan(5).test(4)).isFalse();
    }

    @Test
    public void tryIsInvalid_shouldBeSameAsNotTest() {
        assertThat(validator.isLessThan(5).test(4)).isEqualTo(!validator.isLessThan(5).isInvalid(4));
    }

    @Test
    public void tryInvalidCustomActionWithInvalid_shouldPerformAction() {
        validator.isLessThan(5).ifInvalid(6, () -> actionTester.add(1));
        assertThat(actionTester).isEqualTo(Some.ofInt(1).oneUpTo(1));
    }

    @Test
    public void tryInvalidCustomActionWithValid_shouldDoNothing() {
        validator.isLessThan(5).ifInvalid(4, () -> actionTester.add(1));
        assertThat(actionTester).isEmpty();
    }

    @Test
    public void tryInvalidCustomConsumerWithInvalid_shouldConsume() {
        validator.isLessThan(5).ifInvalid(6, actionTester::add);
        assertThat(actionTester).isEqualTo(Some.ofInt(6).oneUpTo(1));
    }

    @Test
    public void tryInvalidCustomConsumerWithValid_shouldDoNothing() {
        validator.isLessThan(5).ifInvalid(4, actionTester::add);
        assertThat(actionTester).isEmpty();
    }

    @Test(expected = RuntimeException.class)
    public void tryInvalidThrowWithInvalid_shouldThrow() {
        validator.isLessThan(5).ifInvalidThrow(6, () -> new RuntimeException("Invalid number found!"));
    }

    @Test
    public void tryInvalidThrowWithValid_shouldPass() {
        validator.isLessThan(5).ifInvalidThrow(4, () -> new RuntimeException("Invalid number found!"));
    }

    @Test(expected = RuntimeException.class)
    public void tryInvalidThrowConsumingWithInvalid_shouldThrow() {
        validator.isLessThan(5).ifInvalidThrow(6, i -> new RuntimeException("Invalid number found: " + i));
    }

    @Test
    public void tryInvalidThrowConsumingWithValid_shouldPass() {
        validator.isLessThan(5).ifInvalidThrow(4, i -> new RuntimeException("Invalid number found: " + i));
    }

    @Test
    public void tryIsMonthOfYearWithMonth_shouldBeTrue() {
        assertThat(validator.isMonthOfYear().test(10)).isTrue();
    }

    @Test
    public void tryIsMonthOfYearWithNotMonth_shouldBeFalse() {
        assertThat(validator.isMonthOfYear().test(13)).isFalse();
    }

    @Test
    public void tryIsDayOfMonthSpecificWithCorrectDay_shouldBeTrue() {
        assertThat(validator.isDayOfMonth(2015, 2).test(10)).isTrue();
    }

    @Test
    public void tryIsDayOfMonthSpecificWithIncorrectDay_shouldBeFalse() {
        assertThat(validator.isDayOfMonth(2015, 2).test(30)).isFalse();
    }

    @Test
    public void tryIsDayOfMonthSpecificWithExtraDay_shouldBeTrue() {
        assertThat(validator.isDayOfMonth(2016, 2).test(29)).isTrue();
    }

}
