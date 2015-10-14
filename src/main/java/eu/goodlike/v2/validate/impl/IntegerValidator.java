package eu.goodlike.v2.validate.impl;

import eu.goodlike.v2.validate.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 * Boxed Integer validator implementation
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
     * Adds a predicate which tests if the integer being validated is equal to some amount
     */
    public IntegerValidator isEqual(int amount) {
        return registerCondition(i -> i == amount);
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

    public IntPredicate asIntPredicate() {
        return this::test;
    }

    // CONSTRUCTORS

    public IntegerValidator() {
        this(null, null, new ArrayList<>(), false);
    }

    protected IntegerValidator(IntegerValidator outerValidator, Predicate<Integer> condition, List<Predicate<Integer>> subConditions, boolean notCondition) {
        super(outerValidator, condition, subConditions, notCondition);
    }

    // PROTECTED

    @Override
    protected IntegerValidator thisValidator() {
        return this;
    }

    @Override
    protected IntegerValidator newValidator(IntegerValidator outerValidator, Predicate<Integer> condition, List<Predicate<Integer>> subConditions, boolean notCondition) {
        return new IntegerValidator(outerValidator, condition, subConditions, notCondition);
    }

}
