package eu.goodlike.tbr.validate.impl;

import com.google.common.primitives.Longs;
import eu.goodlike.neat.Null;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import java.util.Collection;
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
     * @throws IllegalStateException if and() is used before any condition, i.e. Long().and()..
     */
    public LongValidator and() {
        if (!hasAccumulatedAnyConditions())
            throw new IllegalStateException("There must be at least a single condition before every and()");

        return this;
    }

    /**
     * Accumulates all predicates before this or() that haven't been accumulated previously using && operator, then
     * adds it to the previously accumulated condition (if such exists) using || operator
     * @throws IllegalStateException if or() is used before any condition, i.e. Long().or()...
     */
    public LongValidator or() {
        if (!hasAccumulatedAnyConditions())
            throw new IllegalStateException("There must be at least a single condition before every or()");

        return new LongValidator(outerValidator, mainCondition(), null, false);
    }

    /**
     * <pre>
     * Sets the next registered condition to be negated
     *
     * Registered conditions are basically every method call that performs a boolean test, including brackets
     * </pre>
     */
    public LongValidator not() {
        return new LongValidator(outerValidator, mainCondition, accumulatedCondition, !notCondition);
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
        return new LongValidator(this, null, null, false);
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
     * @return validator actor, which allows specifying an action if the object is invalid
     */
    public final LongValidationActor ifInvalid(long object) {
        return LongValidationActor.of(object, this);
    }

    /**
     * Executes an arbitrary action if and only if the given long is NOT valid
     * @throws NullPointerException if invalidAction is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public LongValidator ifInvalid(long integer, Runnable invalidAction) {
        Null.check(invalidAction).ifAny("Action cannot be null");
        if (isInvalid(integer))
            invalidAction.run();
        return this;
    }

    /**
     * Executes an action using the object if and only if the given long is NOT valid
     * @throws NullPointerException if invalidConsumer is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public LongValidator ifInvalid(long integer, LongConsumer invalidConsumer) {
        Null.check(invalidConsumer).ifAny("Consumer cannot be null");
        if (isInvalid(integer))
            invalidConsumer.accept(integer);
        return this;
    }

    /**
     * Throws an arbitrary exception if and only if the given long is NOT valid
     * @throws NullPointerException if exceptionSupplier is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public <X extends Throwable> LongValidator ifInvalidThrow(long integer, Supplier<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");
        if (isInvalid(integer))
            throw exceptionSupplier.get();
        return this;
    }

    /**
     * Throws an exception using the object if and only if the given long is NOT valid
     * @throws NullPointerException if exceptionSupplier is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public <X extends Throwable> LongValidator ifInvalidThrow(long integer, LongFunction<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");
        if (isInvalid(integer))
            throw exceptionSupplier.apply(integer);
        return this;
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
     * <pre>
     * Adds a predicate which tests if the long can describe a month of year
     *
     * This is equivalent to isBetween(1, 12)
     * </pre>
     */
    public LongValidator isMonthOfYear() {
        return isBetween(1, 12);
    }

    /**
     * Adds a predicate which tests if the long is a valid day at a certain year and month; it is assumed
     * year and month values are already validated
     */
    public LongValidator isDayOfMonth(int year, int month) {
        ValueRange dayRange = ChronoField.DAY_OF_MONTH.rangeRefinedBy(LocalDate.of(year, month, 1));
        return registerCondition(dayRange::isValidIntValue);
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
        this(null, null, null, false);
    }

    private LongValidator(LongValidator outerValidator, LongPredicate mainCondition, LongPredicate accumulatedCondition, boolean notCondition) {
        this.outerValidator = outerValidator;
        this.mainCondition = mainCondition;
        this.accumulatedCondition = accumulatedCondition;
        this.notCondition = notCondition;
    }

    // PRIVATE

    private final LongValidator outerValidator;
    private final LongPredicate mainCondition;
    private final LongPredicate accumulatedCondition;
    private final boolean notCondition;

    /**
     * <pre>
     * Adds a predicate to subCondition list, negating if not() was called before this method
     *
     * DO NOT use this method more than once per method call, as this will cause not() to malfunction
     * </pre>
     */
    private LongValidator registerCondition(LongPredicate predicate) {
        Null.check(predicate).ifAny("Registered predicate cannot be null");
        if (notCondition)
            predicate = predicate.negate();
        return new LongValidator(outerValidator, mainCondition,
                accumulatedCondition == null ? predicate : accumulatedCondition.and(predicate), false);
    }

    /**
     * @return true if this validator is used for bracket simulation, false otherwise
     */
    private boolean hasOuterValidator() {
        return outerValidator != null;
    }

    private boolean hasAccumulatedAnyConditions() {
        return accumulatedCondition != null;
    }

    private LongPredicate collapseCondition() {
        LongPredicate condition = hasAccumulatedAnyConditions() ? mainCondition() : mainCondition;

        if (condition == null)
            throw new IllegalStateException("You must have at least one condition total, or between openBracket() and closeBracket()");

        return condition;
    }

    private LongPredicate mainCondition() {
        return mainCondition == null ? accumulatedCondition : mainCondition.or(accumulatedCondition);
    }

    public static final class LongValidationActor {

        /**
         * Executes an arbitrary action if and only if an invalid value is passed
         * @throws NullPointerException if someAction is null
         */
        public LongValidator thenDo(Runnable someAction) {
            return validator.ifInvalid(value, someAction);
        }

        /**
         * Consumes the value if and only if an invalid value is passed
         * @throws NullPointerException if valueConsumer is null
         */
        public LongValidator thenDo(LongConsumer valueConsumer) {
            return validator.ifInvalid(value, valueConsumer);
        }

        /**
         * Throws an arbitrary exception if and only if an invalid value is passed
         * @throws NullPointerException if exceptionSupplier is null
         */
        public <X extends Throwable> LongValidator thenThrow(Supplier<X> exceptionSupplier) throws X {
            return validator.ifInvalidThrow(value, exceptionSupplier);
        }

        /**
         * Throws an exception using the value if and only if an invalid value is passed
         * @throws NullPointerException if exceptionFunction is null
         */
        public <X extends Throwable> LongValidator thenThrow(LongFunction<X> exceptionFunction) throws X {
            return validator.ifInvalidThrow(value, exceptionFunction);
        }

        // CONSTRUCTORS

        public static LongValidationActor of(long value, LongValidator validator) {
            Null.check(validator).ifAny("Validator cannot be null");
            return new LongValidationActor(value, validator);
        }

        private LongValidationActor(long value, LongValidator validator) {
            this.value = value;
            this.validator = validator;
        }

        // PRIVATE

        private final long value;
        private final LongValidator validator;

    }

}
