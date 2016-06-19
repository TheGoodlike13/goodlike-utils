package eu.goodlike.validate.primitive;

import com.google.common.primitives.Longs;
import eu.goodlike.neat.Null;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.*;

import static eu.goodlike.functional.Predicates.*;

/**
 * Validator for long (primitive)
 */
public final class PrimitiveLongValidator implements LongPredicate {

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
    public PrimitiveLongValidator isDayOfMonth() {
        return isBetween(1L, 31L);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the long can describe an hour of day
     *
     * This is equivalent to isBetween(0, 23)
     * </pre>
     */
    public PrimitiveLongValidator isHourOfDay() {
        return isBetween(0L, 23L);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the long can describe a minute of hour
     *
     * This is equivalent to isBetween(0, 59)
     * </pre>
     */
    public PrimitiveLongValidator isMinuteOfHour() {
        return isBetween(0L, 59L);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the long can describe a month of year
     *
     * This is equivalent to isBetween(1, 12)
     * </pre>
     */
    public PrimitiveLongValidator isMonthOfYear() {
        return isBetween(1L, 12L);
    }

    /**
     * Adds a predicate which tests if the long is a valid day at a certain year and month; it is assumed
     * year and month values are already validated
     */
    public PrimitiveLongValidator isDayOfMonth(int year, int month) {
        ValueRange dayRange = ChronoField.DAY_OF_MONTH.rangeRefinedBy(LocalDate.of(year, month, 1));
        return registerCondition(dayRange::isValidIntValue);
    }

    /**
     * Adds a predicate which tests if the long being validated is equal to some other long
     */
    public final PrimitiveLongValidator isEqual(long t) {
        return registerCondition(o -> o == t);
    }

    /**
     * Adds a predicate which tests if the long being validated is contained in the given collection
     * @throws NullPointerException if collection is null
     */
    public final PrimitiveLongValidator isIn(Collection<Long> collection) {
        Null.check(collection).ifAny("Collection cannot be null");
        return registerCondition(collection::contains);
    }

    /**
     * Adds a predicate which tests if the long being validated is contained in the given array
     * @throws NullPointerException if array is null
     */
    public final PrimitiveLongValidator isIn(long... array) {
        Null.checkAlone(array).ifAny("Array cannot be null");
        return isIn(Longs.asList(array));
    }

    /**
     * Adds a predicate which tests if the long being validated is contained in the given array
     * @throws NullPointerException if array is null
     */
    public final PrimitiveLongValidator isIn(Long[] array) {
        Null.checkAlone(array).ifAny("Array cannot be null");
        return isIn(Arrays.asList(array));
    }

    /**
     * Adds a predicate which tests if the long being validated is contained in the given map's key set
     * @throws NullPointerException if map is null
     */
    public final PrimitiveLongValidator isKeyOf(Map<Long, ?> map) {
        Null.check(map).ifAny("Map cannot be null");
        return isIn(map.keySet());
    }

    /**
     * Adds a predicate which tests if the long being validated is contained in the given map's value collection
     * @throws NullPointerException if map is null
     */
    public final PrimitiveLongValidator isValueIn(Map<?, Long> map) {
        Null.check(map).ifAny("Map cannot be null");
        return isIn(map.values());
    }

    /**
     * Adds a custom predicate for validating the long
     * @throws NullPointerException if customPredicate is null
     */
    public final PrimitiveLongValidator passes(LongPredicate customPredicate) {
        return registerCondition(customPredicate);
    }

    /**
     * Adds a custom predicate for validating the long; the long is first transformed using given function
     * @throws NullPointerException if mapper or customPredicate is null
     */
    public final <U> PrimitiveLongValidator passesAs(LongFunction<? extends U> mapper, Predicate<? super U> customPredicate) {
        Null.check(mapper, customPredicate).ifAny("Mapper and predicate cannot be null");
        return registerCondition(o -> customPredicate.test(mapper.apply(o)));
    }

    /**
     * Adds a custom predicate for validating the long; the long is first transformed using given function
     * @throws NullPointerException if mapper or customPredicate is null
     */
    public final PrimitiveLongValidator passesAsInt(LongToIntFunction mapper, IntPredicate customPredicate) {
        Null.check(mapper, customPredicate).ifAny("Mapper and predicate cannot be null");
        return registerCondition(o -> customPredicate.test(mapper.applyAsInt(o)));
    }

    /**
     * Adds a custom predicate for validating the long; the long is first transformed using given function
     * @throws NullPointerException if mapper or customPredicate is null
     */
    public final PrimitiveLongValidator passesAsDouble(LongToDoubleFunction mapper, DoublePredicate customPredicate) {
        Null.check(mapper, customPredicate).ifAny("Mapper and predicate cannot be null");
        return registerCondition(o -> customPredicate.test(mapper.applyAsDouble(o)));
    }

    /**
     * Adds a predicate which tests if the comparable being validated is equal based on its comparison
     */
    public final PrimitiveLongValidator isEqualComparably(long other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> Long.compare(t, other) == 0);
    }

    /**
     * Adds a predicate which tests if the comparable being validated is less than given comparable
     * based on their comparison
     */
    public final PrimitiveLongValidator isLessThan(long other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> Long.compare(t, other) < 0);
    }

    /**
     * Adds a predicate which tests if the comparable being validated is more than given comparable
     * based on their comparison
     */
    public final PrimitiveLongValidator isMoreThan(long other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> Long.compare(t, other) > 0);
    }

    /**
     * Adds a predicate which tests if the comparable being validated is at least as big given comparable
     * based on their comparison
     */
    public final PrimitiveLongValidator isAtLeast(long other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> Long.compare(t, other) >= 0);
    }

    /**
     * Adds a predicate which tests if the comparable being validated is at most as big given comparable
     * based on their comparison
     */
    public final PrimitiveLongValidator isAtMost(long other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> Long.compare(t, other) <= 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is inclusive for both comparables
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final PrimitiveLongValidator isBetween(long left, long right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> Long.compare(t, left) >= 0, t -> Long.compare(t, right) <= 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is exclusive for both comparables
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final PrimitiveLongValidator isBetweenExclusive(long left, long right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> Long.compare(t, left) > 0, t -> Long.compare(t, right) < 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is exclusive for left and inclusive for right comparable
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final PrimitiveLongValidator isBetweenExclusiveLeft(long left, long right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> Long.compare(t, left) > 0, t -> Long.compare(t, right) <= 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is inclusive for left and exclusive for right comparable
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final PrimitiveLongValidator isBetweenExclusiveRight(long left, long right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> Long.compare(t, left) >= 0, t -> Long.compare(t, right) < 0);
    }

    /**
     * <pre>
     * Represents && in predicate logic
     *
     * This operation is the default one, therefore it can be skipped for brevity
     * </pre>
     */
    public final PrimitiveLongValidator and() {
        return this;
    }

    /**
     * <pre>
     * Represents || in predicate logic
     *
     * This operation is NOT the default one; it is to be used for separating "blocks" of predicates which are
     * to be evaluated using and()
     * </pre>
     */
    public final PrimitiveLongValidator or() {
        return new PrimitiveLongValidator(fullCondition(), alwaysTrueForLong(), false);
    }

    /**
     * <pre>
     * Negates the SINGLE next predicate added to this builder
     *
     * Make sure the call is immediately followed with the condition you wish to negate! Leaving a dangling call to
     * not() will not cause any problems for this particular Validator, but any Validator that builds upon it will
     * act incorrectly
     * </pre>
     */
    public final PrimitiveLongValidator not() {
        return new PrimitiveLongValidator(mainCondition, accumulatedCondition, !negateNext);
    }

    @Override
    public boolean test(long value) {
        return fullCondition().test(value);
    }

    /**
     * @return true if given long passes this Validator as a Predicate, false otherwise
     */
    public final boolean isValid(long value) {
        return test(value);
    }

    /**
     * @return true if given long does not pass this Validator as a Predicate, false otherwise
     */
    public final boolean isInvalid(long value) {
        return !test(value);
    }

    /**
     * @return ValidatorActor for this Validator and given long
     */
    public PrimitiveLongValidatorActor ifInvalid(int value) {
        return PrimitiveLongValidatorActor.of(value, this);
    }

    /**
     * Validates given long; if the long is invalid, executes the given runnable
     * @throws NullPointerException if customAction is null
     */
    public final PrimitiveLongValidator ifInvalidRun(long value, Runnable customAction) {
        Null.check(customAction).ifAny("Runnable cannot be null");

        if (isInvalid(value))
            customAction.run();

        return this;
    }

    /**
     * Validates given long; if the long is invalid, passes the long to the given consumer
     * @throws NullPointerException if valueConsumer is null
     */
    public final PrimitiveLongValidator ifInvalidAccept(long value, LongConsumer valueConsumer) {
        Null.check(valueConsumer).ifAny("Consumer cannot be null");

        if (isInvalid(value))
            valueConsumer.accept(value);

        return this;
    }

    /**
     * Validates given long; if the long is invalid, throws the exception from given supplier
     * @throws NullPointerException if exceptionSupplier is null
     */
    public final <X extends Throwable> PrimitiveLongValidator ifInvalidThrow(long value, Supplier<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");

        if (isInvalid(value))
            throw exceptionSupplier.get();

        return this;
    }

    /**
     * Validates given long; if the long is invalid, throws the exception from given function, using the long
     * as the parameter
     * @throws NullPointerException if exceptionFunction is null
     */
    public final <X extends Throwable> PrimitiveLongValidator ifInvalidThrowWith(long value, LongFunction<? extends X> exceptionFunction) throws X {
        Null.check(exceptionFunction).ifAny("Exception supplier cannot be null");

        if (isInvalid(value))
            throw exceptionFunction.apply(value);

        return this;
    }

    // CONSTRUCTORS

    public PrimitiveLongValidator() {
        this(alwaysFalseForLong(), alwaysTrueForLong(), false);
    }

    public PrimitiveLongValidator(LongPredicate mainCondition, LongPredicate accumulatedCondition, boolean negateNext) {
        this.mainCondition = mainCondition;
        this.accumulatedCondition = accumulatedCondition;
        this.negateNext = negateNext;
    }

    // PRIVATE

    private final LongPredicate mainCondition;
    private final LongPredicate accumulatedCondition;
    private final boolean negateNext;

    private LongPredicate fullCondition() {
        return mainCondition.or(accumulatedCondition);
    }

    private PrimitiveLongValidator registerCondition(LongPredicate condition) {
        Null.check(condition).ifAny("Predicate cannot be null");

        if (negateNext)
            condition = condition.negate();

        return new PrimitiveLongValidator(mainCondition, accumulatedCondition.and(condition), false);
    }

    private PrimitiveLongValidator registerConditions(LongPredicate... conditions) {
        return registerCondition(conjunction(conditions));
    }

    /**
     * Actor which allows to specify what to do when an invalid value is passed to a validator
     */
    public static final class PrimitiveLongValidatorActor {
        /**
         * Executes an arbitrary action if and only if an invalid value is passed
         * @throws NullPointerException if someAction is null
         */
        public PrimitiveLongValidator thenRun(Runnable someAction) {
            return validator.ifInvalidRun(value, someAction);
        }

        /**
         * Consumes the value if and only if an invalid value is passed
         * @throws NullPointerException if valueConsumer is null
         */
        public PrimitiveLongValidator thenAccept(LongConsumer valueConsumer) {
            return validator.ifInvalidAccept(value, valueConsumer);
        }

        /**
         * Throws an arbitrary exception if and only if an invalid value is passed
         * @throws NullPointerException if exceptionSupplier is null
         */
        public <X extends RuntimeException> PrimitiveLongValidator thenThrow(Supplier<? extends X> exceptionSupplier) throws X {
            return validator.ifInvalidThrow(value, exceptionSupplier);
        }

        /**
         * Throws an exception using the value if and only if an invalid value is passed
         * @throws NullPointerException if exceptionFunction is null
         */
        public <X extends RuntimeException> PrimitiveLongValidator thenThrowWith(LongFunction<? extends X> exceptionFunction) throws X {
            return validator.ifInvalidThrowWith(value, exceptionFunction);
        }

        // CONSTRUCTORS

        public static PrimitiveLongValidatorActor of(int value, PrimitiveLongValidator validator) {
            Null.check(value, validator).ifAny("Value and validator cannot be null");
            return new PrimitiveLongValidatorActor(value, validator);
        }

        private PrimitiveLongValidatorActor(int value, PrimitiveLongValidator validator) {
            this.value = value;
            this.validator = validator;
        }

        // PRIVATE

        private final int value;
        private final PrimitiveLongValidator validator;
    }

}
