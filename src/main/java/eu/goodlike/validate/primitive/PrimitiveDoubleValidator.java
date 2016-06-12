package eu.goodlike.validate.primitive;

import com.google.common.primitives.Doubles;
import eu.goodlike.neat.Null;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.*;

import static eu.goodlike.functional.Predicates.*;

/**
 * Validator for double (primitive)
 */
public final class PrimitiveDoubleValidator implements DoublePredicate {

    public final PrimitiveDoubleValidator isLatitude() {
        return isBetween(-90.0, 90.0);
    }

    public final PrimitiveDoubleValidator isLongitude() {
        return isBetween(-180.0, 180.0);
    }

    /**
     * Adds a predicate which tests if the double being validated is equal to some other double
     */
    public final PrimitiveDoubleValidator isEqual(double t) {
        return registerCondition(o -> o == t);
    }

    /**
     * Adds a predicate which tests if the double being validated is contained in the given collection
     * @throws NullPointerException if collection is null
     */
    public final PrimitiveDoubleValidator isIn(Collection<Double> collection) {
        Null.check(collection).ifAny("Collection cannot be null");
        return registerCondition(collection::contains);
    }

    /**
     * Adds a predicate which tests if the double being validated is contained in the given array
     * @throws NullPointerException if array is null
     */
    public final PrimitiveDoubleValidator isIn(double... array) {
        Null.checkAlone(array).ifAny("Array cannot be null");
        return isIn(Doubles.asList(array));
    }

    /**
     * Adds a predicate which tests if the double being validated is contained in the given array
     * @throws NullPointerException if array is null
     */
    public final PrimitiveDoubleValidator isIn(Double[] array) {
        Null.checkAlone(array).ifAny("Array cannot be null");
        return isIn(Arrays.asList(array));
    }

    /**
     * Adds a predicate which tests if the double being validated is contained in the given map's key set
     * @throws NullPointerException if map is null
     */
    public final PrimitiveDoubleValidator isKeyOf(Map<Double, ?> map) {
        Null.check(map).ifAny("Map cannot be null");
        return isIn(map.keySet());
    }

    /**
     * Adds a predicate which tests if the double being validated is contained in the given map's value collection
     * @throws NullPointerException if map is null
     */
    public final PrimitiveDoubleValidator isValueIn(Map<?, Double> map) {
        Null.check(map).ifAny("Map cannot be null");
        return isIn(map.values());
    }

    /**
     * Adds a custom predicate for validating the double
     * @throws NullPointerException if customPredicate is null
     */
    public final PrimitiveDoubleValidator passes(DoublePredicate customPredicate) {
        return registerCondition(customPredicate);
    }

    /**
     * Adds a custom predicate for validating the double; the double is first transformed using given function
     * @throws NullPointerException if mapper or customPredicate is null
     */
    public final <U> PrimitiveDoubleValidator passesAs(DoubleFunction<? extends U> mapper, Predicate<? super U> customPredicate) {
        Null.check(mapper, customPredicate).ifAny("Mapper and predicate cannot be null");
        return registerCondition(o -> customPredicate.test(mapper.apply(o)));
    }

    /**
     * Adds a custom predicate for validating the double; the double is first transformed using given function
     * @throws NullPointerException if mapper or customPredicate is null
     */
    public final PrimitiveDoubleValidator passesAsInt(DoubleToIntFunction mapper, IntPredicate customPredicate) {
        Null.check(mapper, customPredicate).ifAny("Mapper and predicate cannot be null");
        return registerCondition(o -> customPredicate.test(mapper.applyAsInt(o)));
    }

    /**
     * Adds a custom predicate for validating the double; the double is first transformed using given function
     * @throws NullPointerException if mapper or customPredicate is null
     */
    public final PrimitiveDoubleValidator passesAsLong(DoubleToLongFunction mapper, LongPredicate customPredicate) {
        Null.check(mapper, customPredicate).ifAny("Mapper and predicate cannot be null");
        return registerCondition(o -> customPredicate.test(mapper.applyAsLong(o)));
    }

    /**
     * Adds a predicate which tests if the comparable being validated is equal based on its comparison
     */
    public final PrimitiveDoubleValidator isEqualComparably(double other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> Double.compare(t, other) == 0);
    }

    /**
     * Adds a predicate which tests if the comparable being validated is less than given comparable
     * based on their comparison
     */
    public final PrimitiveDoubleValidator isLessThan(double other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> Double.compare(t, other) < 0);
    }

    /**
     * Adds a predicate which tests if the comparable being validated is more than given comparable
     * based on their comparison
     */
    public final PrimitiveDoubleValidator isMoreThan(double other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> Double.compare(t, other) > 0);
    }

    /**
     * Adds a predicate which tests if the comparable being validated is at least as big given comparable
     * based on their comparison
     */
    public final PrimitiveDoubleValidator isAtLeast(double other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> Double.compare(t, other) >= 0);
    }
    /**
     * Adds a predicate which tests if the comparable being validated is at most as big given comparable
     * based on their comparison
     */
    public final PrimitiveDoubleValidator isAtMost(double other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> Double.compare(t, other) <= 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is inclusive for both comparables
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final PrimitiveDoubleValidator isBetween(double left, double right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> Double.compare(t, left) >= 0, t -> Double.compare(t, right) <= 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is exclusive for both comparables
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final PrimitiveDoubleValidator isBetweenExclusive(double left, double right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> Double.compare(t, left) > 0, t -> Double.compare(t, right) < 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is exclusive for left and inclusive for right comparable
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final PrimitiveDoubleValidator isBetweenExclusiveLeft(double left, double right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> Double.compare(t, left) > 0, t -> Double.compare(t, right) <= 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is inclusive for left and exclusive for right comparable
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final PrimitiveDoubleValidator isBetweenExclusiveRight(double left, double right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> Double.compare(t, left) >= 0, t -> Double.compare(t, right) < 0);
    }

    /**
     * <pre>
     * Represents && in predicate logic
     *
     * This operation is the default one, therefore it can be skipped for brevity
     * </pre>
     */
    public final PrimitiveDoubleValidator and() {
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
    public final PrimitiveDoubleValidator or() {
        return new PrimitiveDoubleValidator(fullCondition(), alwaysTrueForDouble(), false);
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
    public final PrimitiveDoubleValidator not() {
        return new PrimitiveDoubleValidator(mainCondition, accumulatedCondition, !negateNext);
    }

    @Override
    public boolean test(double value) {
        return fullCondition().test(value);
    }

    /**
     * @return true if given double passes this Validator as a Predicate, false otherwise
     */
    public final boolean isValid(double value) {
        return test(value);
    }

    /**
     * @return true if given double does not pass this Validator as a Predicate, false otherwise
     */
    public final boolean isInvalid(double value) {
        return !test(value);
    }

    /**
     * @return ValidatorActor for this Validator and given double
     */
    public PrimitiveDoubleValidatorActor ifInvalid(int value) {
        return PrimitiveDoubleValidatorActor.of(value, this);
    }

    /**
     * Validates given double; if the double is invalid, executes the given runnable
     * @throws NullPointerException if customAction is null
     */
    public final PrimitiveDoubleValidator ifInvalidRun(double value, Runnable customAction) {
        Null.check(customAction).ifAny("Runnable cannot be null");

        if (isInvalid(value))
            customAction.run();

        return this;
    }

    /**
     * Validates given double; if the double is invalid, passes the double to the given consumer
     * @throws NullPointerException if valueConsumer is null
     */
    public final PrimitiveDoubleValidator ifInvalidAccept(double value, DoubleConsumer valueConsumer) {
        Null.check(valueConsumer).ifAny("Consumer cannot be null");

        if (isInvalid(value))
            valueConsumer.accept(value);

        return this;
    }

    /**
     * Validates given double; if the double is invalid, throws the exception from given supplier
     * @throws NullPointerException if exceptionSupplier is null
     */
    public final <X extends Throwable> PrimitiveDoubleValidator ifInvalidThrow(double value, Supplier<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");

        if (isInvalid(value))
            throw exceptionSupplier.get();

        return this;
    }

    /**
     * Validates given double; if the double is invalid, throws the exception from given function, using the double
     * as the parameter
     * @throws NullPointerException if exceptionFunction is null
     */
    public final <X extends Throwable> PrimitiveDoubleValidator ifInvalidThrowWith(double value, DoubleFunction<? extends X> exceptionFunction) throws X {
        Null.check(exceptionFunction).ifAny("Exception supplier cannot be null");

        if (isInvalid(value))
            throw exceptionFunction.apply(value);

        return this;
    }

    // CONSTRUCTORS

    public PrimitiveDoubleValidator() {
        this(alwaysFalseForDouble(), alwaysTrueForDouble(), false);
    }

    public PrimitiveDoubleValidator(DoublePredicate mainCondition, DoublePredicate accumulatedCondition, boolean negateNext) {
        this.mainCondition = mainCondition;
        this.accumulatedCondition = accumulatedCondition;
        this.negateNext = negateNext;
    }

    // PRIVATE

    private final DoublePredicate mainCondition;
    private final DoublePredicate accumulatedCondition;
    private final boolean negateNext;

    private DoublePredicate fullCondition() {
        return mainCondition.or(accumulatedCondition);
    }

    private PrimitiveDoubleValidator registerCondition(DoublePredicate condition) {
        Null.check(condition).ifAny("Predicate cannot be null");

        if (negateNext)
            condition = condition.negate();

        return new PrimitiveDoubleValidator(mainCondition, accumulatedCondition.and(condition), false);
    }

    private PrimitiveDoubleValidator registerConditions(DoublePredicate... conditions) {
        return registerCondition(conjunction(conditions));
    }

    /**
     * Actor which allows to specify what to do when an invalid value is passed to a validator
     */
    public static final class PrimitiveDoubleValidatorActor {
        /**
         * Executes an arbitrary action if and only if an invalid value is passed
         * @throws NullPointerException if someAction is null
         */
        public PrimitiveDoubleValidator thenRun(Runnable someAction) {
            return validator.ifInvalidRun(value, someAction);
        }

        /**
         * Consumes the value if and only if an invalid value is passed
         * @throws NullPointerException if valueConsumer is null
         */
        public PrimitiveDoubleValidator thenAccept(DoubleConsumer valueConsumer) {
            return validator.ifInvalidAccept(value, valueConsumer);
        }

        /**
         * Throws an arbitrary exception if and only if an invalid value is passed
         * @throws NullPointerException if exceptionSupplier is null
         */
        public <X extends RuntimeException> PrimitiveDoubleValidator thenThrow(Supplier<? extends X> exceptionSupplier) throws X {
            return validator.ifInvalidThrow(value, exceptionSupplier);
        }

        /**
         * Throws an exception using the value if and only if an invalid value is passed
         * @throws NullPointerException if exceptionFunction is null
         */
        public <X extends RuntimeException> PrimitiveDoubleValidator thenThrowWith(DoubleFunction<? extends X> exceptionFunction) throws X {
            return validator.ifInvalidThrowWith(value, exceptionFunction);
        }

        // CONSTRUCTORS

        public static PrimitiveDoubleValidatorActor of(int value, PrimitiveDoubleValidator validator) {
            Null.check(value, validator).ifAny("Value and validator cannot be null");
            return new PrimitiveDoubleValidatorActor(value, validator);
        }

        private PrimitiveDoubleValidatorActor(int value, PrimitiveDoubleValidator validator) {
            this.value = value;
            this.validator = validator;
        }

        // PRIVATE

        private final int value;
        private final PrimitiveDoubleValidator validator;
    }

}
