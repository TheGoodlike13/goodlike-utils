package eu.goodlike.validate.impl;

import eu.goodlike.validate.ComparableValidator;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import java.util.function.Predicate;

/**
 * Validator implementation for Integer (boxed)
 */
public final class IntValidator extends ComparableValidator<Integer, IntValidator> {

    /**
     * <pre>
     * Adds a predicate which tests if the integer can describe a day of the month
     *
     * This is equivalent to isBetween(1, 31)
     *
     * While certain months do not have days 29 to 31, it is assumed that this is handled somewhere else
     * entirely; the purpose of this is simply to validate the input
     * </pre>
     */
    public IntValidator isDayOfMonth() {
        return isBetween(1, 31);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the integer can describe an hour of day
     *
     * This is equivalent to isBetween(0, 23)
     * </pre>
     */
    public IntValidator isHourOfDay() {
        return isBetween(0, 23);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the integer can describe a minute of hour
     *
     * This is equivalent to isBetween(0, 59)
     * </pre>
     */
    public IntValidator isMinuteOfHour() {
        return isBetween(0, 59);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the integer can describe a month of year
     *
     * This is equivalent to isBetween(1, 12)
     * </pre>
     */
    public IntValidator isMonthOfYear() {
        return isBetween(1, 12);
    }

    /**
     * Adds a predicate which tests if the integer is a valid day at a certain year and month; it is assumed
     * year and month values are already validated
     */
    public IntValidator isDayOfMonth(int year, int month) {
        ValueRange dayRange = ChronoField.DAY_OF_MONTH.rangeRefinedBy(LocalDate.of(year, month, 1));
        return registerCondition(dayRange::isValidIntValue);
    }

    /**
     * Adds a predicate which tests if the integer represents a character between '0' and '9' as a code point
     */
    public IntValidator isSimpleDigit() {
        return isBetween((int)'0', (int)'9');
    }

    // CONSTRUCTORS

    public IntValidator() {
        super();
    }

    protected IntValidator(Predicate<Integer> mainCondition, Predicate<Integer> accumulatedCondition, boolean negateNext) {
        super(mainCondition, accumulatedCondition, negateNext);
    }

    // PROTECTED

    @Override
    protected IntValidator thisValidator() {
        return null;
    }

    @Override
    protected IntValidator newValidator(Predicate<Integer> mainCondition, Predicate<Integer> accumulatedCondition, boolean negateNext) {
        return new IntValidator(mainCondition, accumulatedCondition, negateNext);
    }

}
