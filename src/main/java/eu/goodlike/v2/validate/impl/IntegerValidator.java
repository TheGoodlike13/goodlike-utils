package eu.goodlike.v2.validate.impl;

import eu.goodlike.v2.validate.Validate;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 * <pre>
 * Boxed Integer validator implementation
 *
 * Primitive and boxed versions are separate, because IntPredicate is not compatible with Predicate of Integer; more
 * specifically, their methods or() and and() require those two different kind of predicates; as a result, there is no
 * way to decide which is which by just looking at the lambda expressions
 * </pre>
 */
public class IntegerValidator extends Validate<Integer, IntegerValidator> {

    /**
     * Adds a predicate which tests if the integer being validated is larger than some amount
     */
    public IntegerValidator isMoreThan(int amount) {
        return registerCondition(i -> i > amount);
    }

    /**
     * Adds a predicate which tests if the integer being validated is smaller than some amount
     */
    public IntegerValidator isLessThan(int amount) {
        return registerCondition(i -> i < amount);
    }

    /**
     * Adds a predicate which tests if the integer being validated is larger or equal to some amount
     */
    public IntegerValidator isAtLeast(int amount) {
        return registerCondition(i -> i >= amount);
    }

    /**
     * Adds a predicate which tests if the integer being validated is smaller or equal to some amount
     */
    public IntegerValidator isAtMost(int amount) {
        return registerCondition(i -> i <= amount);
    }

    /**
     * Adds a predicate which tests if the integer being validated is between some two numbers, both inclusive
     */
    public IntegerValidator isBetween(int lowBound, int highBound) {
        return registerCondition(i -> i >= lowBound && i <= highBound);
    }

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
    public IntegerValidator isDayOfMonth() {
        return isBetween(1, 31);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the integer can describe an hour of day
     *
     * This is equivalent to isBetween(0, 23)
     * </pre>
     */
    public IntegerValidator isHourOfDay() {
        return isBetween(0, 23);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the integer can describe a minute of hour
     *
     * This is equivalent to isBetween(0, 59)
     * </pre>
     */
    public IntegerValidator isMinuteOfHour() {
        return isBetween(0, 59);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the integer can describe a month of year
     *
     * This is equivalent to isBetween(1, 12)
     * </pre>
     */
    public IntegerValidator isMonthOfYear() {
        return isBetween(1, 12);
    }

    /**
     * Adds a predicate which tests if the integer is a valid day at a certain year and month; it is assumed
     * year and month values are already validated
     */
    public IntegerValidator isDayOfMonth(int year, int month) {
        ValueRange dayRange = ChronoField.DAY_OF_MONTH.rangeRefinedBy(LocalDate.of(year, month, 1));
        return registerCondition(dayRange::isValidIntValue);
    }

    public IntPredicate asIntPredicate() {
        return this::test;
    }

    // CONSTRUCTORS

    public IntegerValidator() {
        this(null, null, null, false);
    }

    protected IntegerValidator(IntegerValidator outerValidator, Predicate<Integer> mainCondition, Predicate<Integer> accumulatedCondition, boolean notCondition) {
        super(outerValidator, mainCondition, accumulatedCondition, notCondition);
    }

    // PROTECTED

    @Override
    protected IntegerValidator thisValidator() {
        return this;
    }

    @Override
    protected IntegerValidator newValidator(IntegerValidator outerValidator, Predicate<Integer> mainCondition, Predicate<Integer> accumulatedCondition, boolean notCondition) {
        return new IntegerValidator(outerValidator, mainCondition, accumulatedCondition, notCondition);
    }

}
