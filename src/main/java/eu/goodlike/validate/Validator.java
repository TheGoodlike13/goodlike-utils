package eu.goodlike.validate;

import eu.goodlike.misc.SpecialUtils;
import eu.goodlike.neat.Null;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Stream;

import static eu.goodlike.functional.Predicates.*;

/**
 * <pre>
 * Predicate builder which allows for fluent validation
 *
 * Intended to be used through Validate class methods, but can also be extended for custom usage
 *
 * The instances of Validator classes are all immutable and thus thread-safe. Every call creates a new instance,
 * therefore caching/pre-loading Validators is suggested.
 * </pre>
 */
public abstract class Validator<T, V extends Validator<T, V>> implements Predicate<T> {

    /**
     * return this;
     * @return implementing validator, so the methods can be chained from extending classes
     */
    protected abstract V thisValidator();

    /**
     * return new V(mainCondition, accumulatedCondition, negateNext);
     * @return new instance of implementing validator, so the methods can be chained from extending classes
     */
    protected abstract V newValidator(Predicate<T> mainCondition, Predicate<T> accumulatedCondition, boolean negateNext);

    /**
     * Adds a predicate which tests if the object being validated is null
     */
    public final V isNull() {
        return registerCondition(o -> o == null);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the object being validated is equal to some other object
     *
     * This uses Objects.equals() for equality testing
     * </pre>
     */
    public final V isEqual(T t) {
        return registerCondition(o -> Objects.equals(o, t));
    }

    /**
     * <pre>
     * Adds a predicate which tests if the object being validated is equal to some other object
     *
     * This uses custom BiPredicate to assert equality
     * </pre>
     * @throws NullPointerException if customEqualityCheck is null
     */
    public final V isEqual(T t, BiPredicate<? super T, ? super T> customEqualityCheck) {
        Null.check(customEqualityCheck).ifAny("Custom equality check cannot be null");
        return registerCondition(o -> customEqualityCheck.test(o, t));
    }

    /**
     * <pre>
     * Adds a predicate which tests if the object being validated is equal to some other object, when transformed
     * using given converter function
     *
     * This uses Objects.equals() for equality testing
     * </pre>
     * @throws NullPointerException if converter is null
     */
    public final <U> V isEqualAs(T t, Function<? super T, ? extends U> converter) {
        Null.check(converter).ifAny("Converter cannot be null");
        return registerCondition(o -> SpecialUtils.equals(o, t, converter));
    }

    /**
     * Adds a predicate which tests if the object being validated is exactly the same as given object, testing for
     * reference equality
     */
    public final V isSameAs(T t) {
        return registerCondition(o -> o == t);
    }

    /**
     * Adds a predicate which tests if the object being validated is contained in the given collection
     * @throws NullPointerException if collection is null
     */
    public final V isIn(Collection<? super T> collection) {
        Null.check(collection).ifAny("Collection cannot be null");
        return registerCondition(collection::contains);
    }

    /**
     * Adds a predicate which tests if the object being validated is contained in the given array
     * @throws NullPointerException if array is null
     */
    @SafeVarargs
    public final V isIn(T... array) {
        Null.checkAlone(array).ifAny("Array cannot be null");
        return isIn(Arrays.asList(array));
    }

    /**
     * Adds a predicate which tests if the object being validated is contained in the given map's key set
     * @throws NullPointerException if map is null
     */
    public final V isKeyOf(Map<? super T, ?> map) {
        Null.check(map).ifAny("Map cannot be null");
        return isIn(map.keySet());
    }

    /**
     * Adds a predicate which tests if the object being validated is contained in the given map's value collection
     * @throws NullPointerException if map is null
     */
    public final V isValueIn(Map<?, ? super T> map) {
        Null.check(map).ifAny("Map cannot be null");
        return isIn(map.values());
    }

    /**
     * Adds a custom predicate for validating the object
     * @throws NullPointerException if customPredicate is null
     */
    public final V passes(Predicate<? super T> customPredicate) {
        return registerCondition(customPredicate);
    }

    /**
     * Adds a custom predicate for validating the object; the object is first transformed using given function
     * @throws NullPointerException if mapper or customPredicate is null
     */
    public final <U> V passesAs(Function<? super T, ? extends U> mapper, Predicate<? super U> customPredicate) {
        Null.check(mapper, customPredicate).ifAny("Mapper and predicate cannot be null");
        return registerCondition(o -> customPredicate.test(mapper.apply(o)));
    }

    /**
     * Adds a custom predicate for validating the object; the object is first transformed using given function
     * @throws NullPointerException if mapper or customPredicate is null
     */
    public final V passesAsInt(ToIntFunction<? super T> mapper, IntPredicate customPredicate) {
        Null.check(mapper, customPredicate).ifAny("Mapper and predicate cannot be null");
        return registerCondition(o -> customPredicate.test(mapper.applyAsInt(o)));
    }

    /**
     * Adds a custom predicate for validating the object; the object is first transformed using given function
     * @throws NullPointerException if mapper or customPredicate is null
     */
    public final V passesAsLong(ToLongFunction<? super T> mapper, LongPredicate customPredicate) {
        Null.check(mapper, customPredicate).ifAny("Mapper and predicate cannot be null");
        return registerCondition(o -> customPredicate.test(mapper.applyAsLong(o)));
    }

    /**
     * Adds a custom predicate for validating the object; the object is first transformed using given function
     * @throws NullPointerException if mapper or customPredicate is null
     */
    public final V passesAsDouble(ToDoubleFunction<? super T> mapper, DoublePredicate customPredicate) {
        Null.check(mapper, customPredicate).ifAny("Mapper and predicate cannot be null");
        return registerCondition(o -> customPredicate.test(mapper.applyAsDouble(o)));
    }

    /**
     * Adds a predicate which tests if the object being validated is instance of the given class
     * @throws NullPointerException if clazz is null
     */
    public final V isInstanceOf(Class<?> clazz) {
        Null.check(clazz).ifAny("Class cannot be null");
        return registerCondition(clazz::isInstance);
    }

    /**
     * Adds a predicate which tests if the object being validated is specifically of the given class;
     * this does not allow for extending classes to pass
     * @throws NullPointerException if clazz is null
     */
    public final V isExactlyInstanceOf(Class<?> clazz) {
        Null.check(clazz).ifAny("Class cannot be null");
        return passesAs(Object::getClass, clazz::equals);
    }

    /**
     * Adds a predicate which tests if the object being validated is instance of any of the given classes
     * @throws NullPointerException if classes is or contains null
     */
    public final V isInstanceOfAny(Class<?>... classes) {
        Null.checkArray(classes).ifAny("Classes cannot be null");
        return registerCondition(o -> Stream.of(classes).anyMatch(c -> c.isInstance(o)));
    }

    /**
     * Adds a predicate which tests if the object being validated is specifically of any of the given classes;
     * this does not allow for extending classes to pass
     * @throws NullPointerException if classes is or contains null
     */
    public final V isExactlyInstanceOfAny(Class<?>... classes) {
        Null.checkArray(classes).ifAny("Classes cannot be null");
        return passesAs(Object::getClass, clazz -> Stream.of(classes).anyMatch(clazz::equals));
    }

    /**
     * <pre>
     * Represents && in predicate logic
     *
     * This operation is the default one, therefore it can be skipped for brevity
     * </pre>
     */
    public final V and() {
        return thisValidator();
    }

    /**
     * <pre>
     * Represents || in predicate logic
     *
     * This operation is NOT the default one; it is to be used for separating "blocks" of predicates which are
     * to be evaluated using and()
     * </pre>
     */
    public final V or() {
        return newValidator(fullCondition(), alwaysTrue(), false);
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
    public final V not() {
        return newValidator(mainCondition, accumulatedCondition, !negateNext);
    }

    @Override
    public final boolean test(T object) {
        return fullCondition().test(object);
    }

    /**
     * @return true if given object passes this Validator as a Predicate, false otherwise
     */
    public final boolean isValid(T object) {
        return test(object);
    }

    /**
     * @return true if given object does not pass this Validator as a Predicate, false otherwise
     */
    public final boolean isInvalid(T object) {
        return !test(object);
    }

    /**
     * @return ValidatorActor for this Validator and given object
     */
    public final <E extends T> ValidatorActor<T, E, V> ifInvalid(E object) {
        return ValidatorActor.of(object, thisValidator());
    }

    /**
     * Validates given object; if the object is invalid, executes the given runnable
     * @throws NullPointerException if customAction is null
     */
    public final V ifInvalidRun(T object, Runnable customAction) {
        Null.check(customAction).ifAny("Runnable cannot be null");

        if (isInvalid(object))
            customAction.run();

        return thisValidator();
    }

    /**
     * Validates given object; if the object is invalid, passes the object to the given consumer
     * @throws NullPointerException if valueConsumer is null
     */
    public final <E extends T> V ifInvalidAccept(E object, Consumer<? super E> valueConsumer) {
        Null.check(valueConsumer).ifAny("Consumer cannot be null");

        if (isInvalid(object))
            valueConsumer.accept(object);

        return thisValidator();
    }

    /**
     * Validates given object; if the object is invalid, throws the exception from given supplier
     * @throws NullPointerException if exceptionSupplier is null
     */
    public final <X extends Throwable> V ifInvalidThrow(T object, Supplier<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");

        if (isInvalid(object))
            throw exceptionSupplier.get();

        return thisValidator();
    }

    /**
     * Validates given object; if the object is invalid, throws the exception from given function, using the object
     * as the parameter
     * @throws NullPointerException if exceptionFunction is null
     */
    public final <E extends T, X extends Throwable> V ifInvalidThrowWith(E object, Function<? super E, ? extends X> exceptionFunction) throws X {
        Null.check(exceptionFunction).ifAny("Exception supplier cannot be null");

        if (isInvalid(object))
            throw exceptionFunction.apply(object);

        return thisValidator();
    }

    // CONSTRUCTORS

    protected Validator() {
        this(alwaysFalse(), alwaysTrue(), false);
    }

    protected Validator(Predicate<T> mainCondition, Predicate<T> accumulatedCondition, boolean negateNext) {
        this.mainCondition = mainCondition;
        this.accumulatedCondition = accumulatedCondition;
        this.negateNext = negateNext;
    }

    // PROTECTED

    /**
     * <pre>
     * Returns a new Validator which contains all predicates accumulated so far, plus the new one. If not() was the
     * previous call to this Validator, the predicate is first negated.
     * </pre>
     * @throws NullPointerException if customPredicate is null
     */
    protected final V registerCondition(Predicate<? super T> condition) {
        Null.check(condition).ifAny("Predicate cannot be null");

        if (negateNext)
            condition = condition.negate();

        return newValidator(mainCondition, accumulatedCondition.and(condition), false);
    }

    /**
     * <pre>
     * Returns a new Validator which contains all predicates accumulated so far, plus the new ones, as a single
     * predicate. If not() was the previous call to this Validator, this single predicate is first negated.
     * </pre>
     * @throws NullPointerException if conditions is or contains null
     */
    @SafeVarargs
    protected final V registerConditions(Predicate<? super T>... conditions) {
        return registerCondition(conjunction(conditions));
    }

    // PRIVATE

    private final Predicate<T> mainCondition;
    private final Predicate<T> accumulatedCondition;
    private final boolean negateNext;

    private Predicate<T> fullCondition() {
        return mainCondition.or(accumulatedCondition);
    }

}
