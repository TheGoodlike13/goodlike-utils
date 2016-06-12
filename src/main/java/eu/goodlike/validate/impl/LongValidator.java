package eu.goodlike.validate.impl;

import eu.goodlike.validate.ComparableValidator;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import java.util.function.Predicate;

/**
 * Validator implementation for Long (boxed)
 */
public final class LongValidator extends ComparableValidator<Long, LongValidator> {

    /**
     * <pre>
     * Adds a predicate which tests if the long can describe a day of the month
     *
     * This is equivalent to isBetween(1, 31)
     *
     * While certain months do not have days 29 to 31, it is assumed that this is handled somewhere else
     * entirely; the purpose of this is simply to validate the input
     * </pre>
     */
    public LongValidator isDayOfMonth() {
        return isBetween(1L, 31L);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the long can describe an hour of day
     *
     * This is equivalent to isBetween(0, 23)
     * </pre>
     */
    public LongValidator isHourOfDay() {
        return isBetween(0L, 23L);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the long can describe a minute of hour
     *
     * This is equivalent to isBetween(0, 59)
     * </pre>
     */
    public LongValidator isMinuteOfHour() {
        return isBetween(0L, 59L);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the long can describe a month of year
     *
     * This is equivalent to isBetween(1, 12)
     * </pre>
     */
    public LongValidator isMonthOfYear() {
        return isBetween(1L, 12L);
    }

    /**
     * Adds a predicate which tests if the long is a valid day at a certain year and month; it is assumed
     * year and month values are already validated
     */
    public LongValidator isDayOfMonth(int year, int month) {
        ValueRange dayRange = ChronoField.DAY_OF_MONTH.rangeRefinedBy(LocalDate.of(year, month, 1));
        return registerCondition(dayRange::isValidIntValue);
    }

    // CONSTRUCTORS

    public LongValidator() {
        super();
    }

    protected LongValidator(Predicate<Long> mainCondition, Predicate<Long> accumulatedCondition, boolean negateNext) {
        super(mainCondition, accumulatedCondition, negateNext);
    }

    // PROTECTED

    @Override
    protected LongValidator thisValidator() {
        return null;
    }

    @Override
    protected LongValidator newValidator(Predicate<Long> mainCondition, Predicate<Long> accumulatedCondition, boolean negateNext) {
        return new LongValidator(mainCondition, accumulatedCondition, negateNext);
    }

}
