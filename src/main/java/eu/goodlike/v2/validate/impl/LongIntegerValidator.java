package eu.goodlike.v2.validate.impl;

import eu.goodlike.v2.validate.Validate;

import java.util.function.LongPredicate;
import java.util.function.Predicate;

/**
 * <pre>
 * Boxed Long validator implementation
 *
 * Primitive and boxed versions are separate, because LongPredicate is not compatible with Predicate of Long; more
 * specifically, their methods or() and and() require those two different kind of predicates; as a result, there is no
 * way to decide which is which by just looking at the lambda expressions
 * </pre>
 */
public final class LongIntegerValidator extends Validate<Long, LongIntegerValidator> {

    /**
     * Adds a predicate which tests if the long being validated is larger than some amount
     */
    public LongIntegerValidator isMoreThan(long amount) {
        return registerCondition(i -> i > amount);
    }

    /**
     * Adds a predicate which tests if the long being validated is smaller than some amount
     */
    public LongIntegerValidator isLessThan(long amount) {
        return registerCondition(i -> i < amount);
    }

    /**
     * Adds a predicate which tests if the long being validated is larger or equal to some amount
     */
    public LongIntegerValidator isAtLeast(long amount) {
        return registerCondition(i -> i >= amount);
    }

    /**
     * Adds a predicate which tests if the long being validated is smaller or equal to some amount
     */
    public LongIntegerValidator isAtMost(long amount) {
        return registerCondition(i -> i <= amount);
    }

    /**
     * Adds a predicate which tests if the long being validated is between some two numbers, both inclusive
     */
    public LongIntegerValidator isBetween(long lowBound, long highBound) {
        return registerCondition(i -> i >= lowBound && i <= highBound);
    }

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
    public LongIntegerValidator isDayOfMonth() {
        return isBetween(1, 31);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the long can describe an hour of day
     *
     * This is equivalent to isBetween(0, 23)
     * </pre>
     */
    public LongIntegerValidator isHourOfDay() {
        return isBetween(0, 23);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the long can describe a minute of hour
     *
     * This is equivalent to isBetween(0, 59)
     * </pre>
     */
    public LongIntegerValidator isMinuteOfHour() {
        return isBetween(0, 59);
    }

    public LongPredicate asLongPredicate() {
        return this::test;
    }

    // CONSTRUCTORS

    public LongIntegerValidator() {
        this(null, null, null, false);
    }

    protected LongIntegerValidator(LongIntegerValidator outerValidator, Predicate<Long> mainCondition, Predicate<Long> accumulatedCondition, boolean notCondition) {
        super(outerValidator, mainCondition, accumulatedCondition, notCondition);
    }

    // PROTECTED

    @Override
    protected LongIntegerValidator thisValidator() {
        return this;
    }

    @Override
    protected LongIntegerValidator newValidator(LongIntegerValidator outerValidator, Predicate<Long> mainCondition, Predicate<Long> accumulatedCondition, boolean notCondition) {
        return new LongIntegerValidator(outerValidator, mainCondition, accumulatedCondition, notCondition);
    }

}
