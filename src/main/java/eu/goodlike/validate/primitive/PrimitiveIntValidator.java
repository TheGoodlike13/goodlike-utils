package eu.goodlike.validate.primitive;

import com.google.common.primitives.Ints;
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
 * Validator for int (primitive)
 */
public final class PrimitiveIntValidator implements IntPredicate {

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
    public PrimitiveIntValidator isDayOfMonth() {
        return isBetween(1, 31);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the integer can describe an hour of day
     *
     * This is equivalent to isBetween(0, 23)
     * </pre>
     */
    public PrimitiveIntValidator isHourOfDay() {
        return isBetween(0, 23);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the integer can describe a minute of hour
     *
     * This is equivalent to isBetween(0, 59)
     * </pre>
     */
    public PrimitiveIntValidator isMinuteOfHour() {
        return isBetween(0, 59);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the integer can describe a month of year
     *
     * This is equivalent to isBetween(1, 12)
     * </pre>
     */
    public PrimitiveIntValidator isMonthOfYear() {
        return isBetween(1, 12);
    }

    /**
     * Adds a predicate which tests if the integer is a valid day at a certain year and month; it is assumed
     * year and month values are already validated
     */
    public PrimitiveIntValidator isDayOfMonth(int year, int month) {
        ValueRange dayRange = ChronoField.DAY_OF_MONTH.rangeRefinedBy(LocalDate.of(year, month, 1));
        return registerCondition(dayRange::isValidIntValue);
    }

    /**
     * Adds a predicate which tests if the integer represents a character between '0' and '9' as a code point
     */
    public PrimitiveIntValidator isSimpleDigit() {
        return isBetween((int)'0', (int)'9');
    }

    /**
     * Adds a predicate which tests if the int being validated is equal to some other int
     */
    public final PrimitiveIntValidator isEqual(int t) {
        return registerCondition(o -> o == t);
    }

    /**
     * Adds a predicate which tests if the int being validated is contained in the given collection
     * @throws NullPointerException if collection is null
     */
    public final PrimitiveIntValidator isIn(Collection<Integer> collection) {
        Null.check(collection).ifAny("Collection cannot be null");
        return registerCondition(collection::contains);
    }

    /**
     * Adds a predicate which tests if the int being validated is contained in the given array
     * @throws NullPointerException if array is null
     */
    public final PrimitiveIntValidator isIn(int... array) {
        Null.checkAlone(array).ifAny("Array cannot be null");
        return isIn(Ints.asList(array));
    }

    /**
     * Adds a predicate which tests if the int being validated is contained in the given array
     * @throws NullPointerException if array is null
     */
    public final PrimitiveIntValidator isIn(Integer[] array) {
        Null.checkAlone(array).ifAny("Array cannot be null");
        return isIn(Arrays.asList(array));
    }

    /**
     * Adds a predicate which tests if the int being validated is contained in the given map's key set
     * @throws NullPointerException if map is null
     */
    public final PrimitiveIntValidator isKeyOf(Map<Integer, ?> map) {
        Null.check(map).ifAny("Map cannot be null");
        return isIn(map.keySet());
    }

    /**
     * Adds a predicate which tests if the int being validated is contained in the given map's value collection
     * @throws NullPointerException if map is null
     */
    public final PrimitiveIntValidator isValueIn(Map<?, Integer> map) {
        Null.check(map).ifAny("Map cannot be null");
        return isIn(map.values());
    }

    /**
     * Adds a custom predicate for validating the int
     * @throws NullPointerException if customPredicate is null
     */
    public final PrimitiveIntValidator passes(IntPredicate customPredicate) {
        return registerCondition(customPredicate);
    }

    /**
     * Adds a custom predicate for validating the int; the int is first transformed using given function
     * @throws NullPointerException if mapper or customPredicate is null
     */
    public final <U> PrimitiveIntValidator passesAs(IntFunction<? extends U> mapper, Predicate<? super U> customPredicate) {
        Null.check(mapper, customPredicate).ifAny("Mapper and predicate cannot be null");
        return registerCondition(o -> customPredicate.test(mapper.apply(o)));
    }

    /**
     * Adds a custom predicate for validating the int; the int is first transformed using given function
     * @throws NullPointerException if mapper or customPredicate is null
     */
    public final PrimitiveIntValidator passesAsLong(IntToLongFunction mapper, LongPredicate customPredicate) {
        Null.check(mapper, customPredicate).ifAny("Mapper and predicate cannot be null");
        return registerCondition(o -> customPredicate.test(mapper.applyAsLong(o)));
    }

    /**
     * Adds a custom predicate for validating the int; the int is first transformed using given function
     * @throws NullPointerException if mapper or customPredicate is null
     */
    public final PrimitiveIntValidator passesAsDouble(IntToDoubleFunction mapper, DoublePredicate customPredicate) {
        Null.check(mapper, customPredicate).ifAny("Mapper and predicate cannot be null");
        return registerCondition(o -> customPredicate.test(mapper.applyAsDouble(o)));
    }

    /**
     * Adds a predicate which tests if the comparable being validated is equal based on its comparison
     */
    public final PrimitiveIntValidator isEqualComparably(int other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> Integer.compare(t, other) == 0);
    }

    /**
     * Adds a predicate which tests if the comparable being validated is less than given comparable
     * based on their comparison
     */
    public final PrimitiveIntValidator isLessThan(int other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> Integer.compare(t, other) < 0);
    }

    /**
     * Adds a predicate which tests if the comparable being validated is more than given comparable
     * based on their comparison
     */
    public final PrimitiveIntValidator isMoreThan(int other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> Integer.compare(t, other) > 0);
    }

    /**
     * Adds a predicate which tests if the comparable being validated is at least as big given comparable
     * based on their comparison
     */
    public final PrimitiveIntValidator isAtLeast(int other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> Integer.compare(t, other) >= 0);
    }

    /**
     * Adds a predicate which tests if the comparable being validated is at most as big given comparable
     * based on their comparison
     */
    public final PrimitiveIntValidator isAtMost(int other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> Integer.compare(t, other) <= 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is inclusive for both comparables
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final PrimitiveIntValidator isBetween(int left, int right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> Integer.compare(t, left) >= 0, t -> Integer.compare(t, right) <= 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is exclusive for both comparables
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final PrimitiveIntValidator isBetweenExclusive(int left, int right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> Integer.compare(t, left) > 0, t -> Integer.compare(t, right) < 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is exclusive for left and inclusive for right comparable
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final PrimitiveIntValidator isBetweenExclusiveLeft(int left, int right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> Integer.compare(t, left) > 0, t -> Integer.compare(t, right) <= 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is inclusive for left and exclusive for right comparable
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final PrimitiveIntValidator isBetweenExclusiveRight(int left, int right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> Integer.compare(t, left) >= 0, t -> Integer.compare(t, right) < 0);
    }

    /**
     * <pre>
     * Represents && in predicate logic
     *
     * This operation is the default one, therefore it can be skipped for brevity
     * </pre>
     */
    public final PrimitiveIntValidator and() {
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
    public final PrimitiveIntValidator or() {
        return new PrimitiveIntValidator(fullCondition(), alwaysTrueForInt(), false);
    }

    /**
     * <pre>
     * Negates the SINGLE next predicate added to this builder
     *
     * Make sure the call is immediately followed with the condition you wish to negate! Leaving a dangling call to
     * not() will not cause any problems for this particular Validator, but any Validator that build upon it will
     * act incorrectly
     * </pre>
     */
    public final PrimitiveIntValidator not() {
        return new PrimitiveIntValidator(mainCondition, accumulatedCondition, !negateNext);
    }

    @Override
    public boolean test(int value) {
        return fullCondition().test(value);
    }

    /**
     * @return true if given int passes this Validator as a Predicate, false otherwise
     */
    public final boolean isValid(int value) {
        return test(value);
    }

    /**
     * @return true if given int does not pass this Validator as a Predicate, false otherwise
     */
    public final boolean isInvalid(int value) {
        return !test(value);
    }

    /**
     * @return ValidatorActor for this Validator and given int
     */
    public PrimitiveIntValidatorActor ifInvalid(int value) {
        return PrimitiveIntValidatorActor.of(value, this);
    }

    /**
     * Validates given int; if the int is invalid, executes the given runnable
     * @throws NullPointerException if customAction is null
     */
    public final PrimitiveIntValidator ifInvalidRun(int value, Runnable customAction) {
        Null.check(customAction).ifAny("Runnable cannot be null");

        if (isInvalid(value))
            customAction.run();

        return this;
    }

    /**
     * Validates given int; if the int is invalid, passes the int to the given consumer
     * @throws NullPointerException if valueConsumer is null
     */
    public final PrimitiveIntValidator ifInvalidAccept(int value, IntConsumer valueConsumer) {
        Null.check(valueConsumer).ifAny("Consumer cannot be null");

        if (isInvalid(value))
            valueConsumer.accept(value);

        return this;
    }

    /**
     * Validates given int; if the int is invalid, throws the exception from given supplier
     * @throws NullPointerException if exceptionSupplier is null
     */
    public final <X extends Throwable> PrimitiveIntValidator ifInvalidThrow(int value, Supplier<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");

        if (isInvalid(value))
            throw exceptionSupplier.get();

        return this;
    }

    /**
     * Validates given int; if the int is invalid, throws the exception from given function, using the int
     * as the parameter
     * @throws NullPointerException if exceptionFunction is null
     */
    public final <X extends Throwable> PrimitiveIntValidator ifInvalidThrowWith(int value, IntFunction<? extends X> exceptionFunction) throws X {
        Null.check(exceptionFunction).ifAny("Exception supplier cannot be null");

        if (isInvalid(value))
            throw exceptionFunction.apply(value);

        return this;
    }

    // CONSTRUCTORS

    public PrimitiveIntValidator() {
        this(alwaysFalseForInt(), alwaysTrueForInt(), false);
    }

    public PrimitiveIntValidator(IntPredicate mainCondition, IntPredicate accumulatedCondition, boolean negateNext) {
        this.mainCondition = mainCondition;
        this.accumulatedCondition = accumulatedCondition;
        this.negateNext = negateNext;
    }

    // PRIVATE

    private final IntPredicate mainCondition;
    private final IntPredicate accumulatedCondition;
    private final boolean negateNext;

    private IntPredicate fullCondition() {
        return mainCondition.or(accumulatedCondition);
    }

    private PrimitiveIntValidator registerCondition(IntPredicate condition) {
        Null.check(condition).ifAny("Predicate cannot be null");

        if (negateNext)
            condition = condition.negate();

        return new PrimitiveIntValidator(mainCondition, accumulatedCondition.and(condition), false);
    }

    private PrimitiveIntValidator registerConditions(IntPredicate... conditions) {
        return registerCondition(conjunction(conditions));
    }

    /**
     * Actor which allows to specify what to do when an invalid value is passed to a validator
     */
    public static final class PrimitiveIntValidatorActor {
        /**
         * Executes an arbitrary action if and only if an invalid value is passed
         * @throws NullPointerException if someAction is null
         */
        public PrimitiveIntValidator thenRun(Runnable someAction) {
            return validator.ifInvalidRun(value, someAction);
        }

        /**
         * Consumes the value if and only if an invalid value is passed
         * @throws NullPointerException if valueConsumer is null
         */
        public PrimitiveIntValidator thenAccept(IntConsumer valueConsumer) {
            return validator.ifInvalidAccept(value, valueConsumer);
        }

        /**
         * Throws an arbitrary exception if and only if an invalid value is passed
         * @throws NullPointerException if exceptionSupplier is null
         */
        public <X extends RuntimeException> PrimitiveIntValidator thenThrow(Supplier<? extends X> exceptionSupplier) throws X {
            return validator.ifInvalidThrow(value, exceptionSupplier);
        }

        /**
         * Throws an exception using the value if and only if an invalid value is passed
         * @throws NullPointerException if exceptionFunction is null
         */
        public <X extends RuntimeException> PrimitiveIntValidator thenThrowWith(IntFunction<? extends X> exceptionFunction) throws X {
            return validator.ifInvalidThrowWith(value, exceptionFunction);
        }

        // CONSTRUCTORS

        public static PrimitiveIntValidatorActor of(int value, PrimitiveIntValidator validator) {
            Null.check(value, validator).ifAny("Value and validator cannot be null");
            return new PrimitiveIntValidatorActor(value, validator);
        }

        private PrimitiveIntValidatorActor(int value, PrimitiveIntValidator validator) {
            this.value = value;
            this.validator = validator;
        }

        // PRIVATE

        private final int value;
        private final PrimitiveIntValidator validator;
    }

}
