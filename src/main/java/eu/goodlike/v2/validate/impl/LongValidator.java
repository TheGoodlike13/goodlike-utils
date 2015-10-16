package eu.goodlike.v2.validate.impl;

import com.google.common.primitives.Longs;
import eu.goodlike.functional.Action;
import eu.goodlike.neat.Null;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.*;

/**
 * <pre>
 * Primitive long validator implementation
 *
 * Primitive and boxed versions are separate, because LongPredicate is not compatible with Predicate of Long; more
 * specifically, their methods or() and and() require those two different kind of predicates; as a result, there is no
 * way to decide which is which by just looking at the lambda expressions
 * </pre>
 */
public final class LongValidator implements LongPredicate {

    /**
     * Adds a custom predicate for validating longs
     */
    public LongValidator passes(LongPredicate customPredicate) {
        return registerCondition(customPredicate);
    }

    /**
     * Adds a predicate which tests if the long being validated is contained by given collection
     * @throws NullPointerException if collection is null
     */
    public LongValidator isContainedIn(Collection<Long> values) {
        Null.check(values).ifAny("Values collection cannot be null");
        return registerCondition(values::contains);
    }

    /**
     * Adds a predicate which tests if the long being validated is contained by given array
     * @throws NullPointerException if array is null
     */
    public LongValidator isContainedIn(long... values) {
        Null.checkAlone(values).ifAny("Values array cannot be null");
        return isContainedIn(Longs.asList(values));
    }

    /**
     * Does nothing, only useful for readability
     */
    public LongValidator and() {
        return this;
    }

    /**
     * Accumulates all predicates before this or() that haven't been accumulated previously using && operator, then
     * adds it to the previously accumulated condition (if such exists) using || operator
     * @throws IllegalStateException if or() is used before any condition, i.e. Long().or()...
     */
    public LongValidator or() {
        if (subConditions.isEmpty())
            throw new IllegalStateException("There must be at least a single condition before every or()");

        return new LongValidator(outerValidator, mainCondition(), new ArrayList<>(), false);
    }

    /**
     * <pre>
     * Sets the next registered condition to be negated
     *
     * Registered conditions are basically every method call that performs a boolean test, including brackets
     * </pre>
     */
    public LongValidator not() {
        return new LongValidator(outerValidator, condition, subConditions, !notCondition);
    }

    /**
     * <pre>
     * Simulates opening brackets which allows for
     *      p1 && (p2 || p3)
     * which would be interpreted as
     *      (p1 && p2) || p3
     * without brackets
     * </pre>
     */
    public LongValidator openBracket() {
        return new LongValidator(this, null, new ArrayList<>(), false);
    }

    /**
     * <pre>
     * Simulates closing brackets which allows for
     *      p1 && (p2 || p3)
     * which would be interpreted as
     *      (p1 && p2) || p3
     * without brackets
     *
     * You can skip calling closeBracket() before terminating the validator, it will be called automatically
     * </pre>
     * @throws IllegalStateException if closeBracket() is called before openBracket()
     * @throws IllegalStateException if there are no conditions between openBracket() and closeBracket()
     */
    public LongValidator closeBracket() {
        if (!hasOuterValidator())
            throw new IllegalStateException("You must use openBracket() before using closeBracket()");

        return outerValidator.registerCondition(collapseCondition());
    }

    /**
     * Closes all brackets, if any, and evaluates constructed predicate for given long
     * @return true if long passes the predicate test, false otherwise
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    @Override
    public boolean test(long integer) {
        return hasOuterValidator()
                ? closeBracket().test(integer)
                : collapseCondition().test(integer);
    }

    /**
     * @return true if long does not pass the predicate test, false otherwise
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public boolean isInvalid(long integer) {
        return !test(integer);
    }

    /**
     * Executes an arbitrary action if and only if the given long is NOT valid
     * @throws NullPointerException if invalidAction is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public void ifInvalid(long integer, Action invalidAction) {
        Null.check(invalidAction).ifAny("Action cannot be null");
        if (isInvalid(integer))
            invalidAction.doIt();
    }

    /**
     * Executes an action using the object if and only if the given long is NOT valid
     * @throws NullPointerException if invalidConsumer is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public void ifInvalid(long integer, LongConsumer invalidConsumer) {
        Null.check(invalidConsumer).ifAny("Consumer cannot be null");
        if (isInvalid(integer))
            invalidConsumer.accept(integer);
    }

    /**
     * Throws an arbitrary exception if and only if the given long is NOT valid
     * @throws NullPointerException if exceptionSupplier is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public <X extends Throwable> void ifInvalidThrow(long integer, Supplier<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");
        if (isInvalid(integer))
            throw exceptionSupplier.get();
    }

    /**
     * Throws an exception using the object if and only if the given long is NOT valid
     * @throws NullPointerException if exceptionSupplier is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public <X extends Throwable> void ifInvalidThrow(long integer, LongFunction<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");
        if (isInvalid(integer))
            throw exceptionSupplier.apply(integer);
    }

    /**
     * Adds a predicate which tests if the long being validated is larger than some amount
     */
    public LongValidator isMoreThan(long amount) {
        return registerCondition(i -> i > amount);
    }

    /**
     * Adds a predicate which tests if the long being validated is smaller than some amount
     */
    public LongValidator isLessThan(long amount) {
        return registerCondition(i -> i < amount);
    }

    /**
     * Adds a predicate which tests if the long being validated is larger or equal to some amount
     */
    public LongValidator isAtLeast(long amount) {
        return registerCondition(i -> i >= amount);
    }

    /**
     * Adds a predicate which tests if the long being validated is smaller or equal to some amount
     */
    public LongValidator isAtMost(long amount) {
        return registerCondition(i -> i <= amount);
    }

    /**
     * Adds a predicate which tests if the long being validated is equal to some amount
     */
    public LongValidator isEqual(long amount) {
        return registerCondition(i -> i == amount);
    }

    /**
     * Adds a predicate which tests if the long being validated is between some two numbers, both inclusive
     */
    public LongValidator isBetween(long lowBound, long highBound) {
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
    public LongValidator isDayOfMonth() {
        return isBetween(1, 31);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the long can describe an hour of day
     *
     * This is equivalent to isBetween(0, 23)
     * </pre>
     */
    public LongValidator isHourOfDay() {
        return isBetween(0, 23);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the long can describe a minute of hour
     *
     * This is equivalent to isBetween(0, 59)
     * </pre>
     */
    public LongValidator isMinuteOfHour() {
        return isBetween(0, 59);
    }

    /**
     * @return this validator as just a Predicate
     */
    public Predicate<Long> asPredicate() {
        return this::test;
    }

    /**
     * @return this validator as just a LongPredicate
     */
    public LongPredicate asLongPredicate() {
        return this;
    }

    // CONSTRUCTORS

    public LongValidator() {
        this(null, null, new ArrayList<>(), false);
    }

    private LongValidator(LongValidator outerValidator, LongPredicate condition, List<LongPredicate> subConditions, boolean notCondition) {
        this.outerValidator = outerValidator;
        this.condition = condition;
        this.subConditions = subConditions;
        this.notCondition = notCondition;
    }

    // PRIVATE

    private final LongValidator outerValidator;
    private final LongPredicate condition;
    private final List<LongPredicate> subConditions;
    private final boolean notCondition;

    /**
     * @return true if this validator is used for bracket simulation, false otherwise
     */
    private boolean hasOuterValidator() {
        return outerValidator != null;
    }

    /**
     * Adds a predicate to subCondition list, negating if not() was called before this method
     */
    private LongValidator registerCondition(LongPredicate predicate) {
        List<LongPredicate> subConditions = new ArrayList<>(this.subConditions);
        subConditions.add(notCondition ? predicate.negate() : predicate);
        return new LongValidator(outerValidator, condition, subConditions, false);
    }

    private LongPredicate collapseCondition() {
        LongPredicate condition = subConditions.isEmpty() ? this.condition : mainCondition();

        if (condition == null)
            throw new IllegalStateException("You must have at least one condition total, or between openBracket() and closeBracket()");

        return condition;
    }

    private LongPredicate mainCondition() {
        return this.condition == null ? accumulatedCondition() : this.condition.or(accumulatedCondition());
    }

    private LongPredicate accumulatedCondition() {
        return subConditions.stream().reduce(LongPredicate::and)
                .orElseThrow(() -> new IllegalStateException("Cannot accumulate an empty list"));
    }

}