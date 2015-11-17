package eu.goodlike.tbr.validate;

import eu.goodlike.neat.Null;
import eu.goodlike.tbr.validate.impl.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <pre>
 * Evolution of previous Validate class, which can be used as a predicate while also allowing simple, custom validation
 *
 * Why choose this over annotations (@Valid)?
 *      1) Predicate > annotation
 *      2) Predicate >>> annotation
 *      3) Did I mention you can use these as predicates?
 *      4) Trivially customize the way invalid values are handled
 *
 * Why not just use predicates?
 *      1) Negation of predicates is still a pain, with this you can negate ANYTHING with a single not() call before it
 *      2) You're basically using predicates anyway
 *
 * Examples (using static import):
 *      listOfStrings.stream().allMatch(string().not().isNull().not().isBlank());
 *      string.chars().allMatch(codePoint().isDigit().or().equal(','));
 *      Int().isDayOfMonth().ifInvalid(50).thenDo(() -> System.out.println("An invalid day of month was passed));
 *      bigDecimal().not().isPositive().ifInvalid(ZERO).thenDo(invalidBigDecimalList::add);
 *      collectionOf(String.class).not().isNull()
 *          .forEachIfNot(string().not().isNull().not().isBlank().isEmail())
 *              .Throw(str -> new IllegalArgumentException("List cannot contain null, blank or non-emails: " + str))
 *          .ifInvalid(listOfEmails).thenThrow(() -> new IllegalArgumentException("List cannot be null"));
 *
 * Only or() needs to be explicitly called, and() is assumed by default, though you can add it if you want more readability
 *
 * In case of brackets:
 *      1) use openBracket() and closeBracket()
 *              string().not().isNull()
 *                  .not().openBracket().isEmpty().or().isBlank().closeBracket();
 *      2) use passes() with another validator as the predicate
 *              string().not().isNull()
 *                  .not().passes(string().isEmpty().or().isBlank());
 *      3) be smart:
 *              string().not().isNull().not().isBlank(); // :D
 *
 * All validators in the impl package are immutable, can be stored and reused
 * </pre>
 * @param <T> type which can be validated using implementing Validator
 * @param <V> the implementing validator
 */
public abstract class Validate<T, V extends Validate<T, V>> implements Predicate<T> {

    /**
     * return this;
     * @return implementing validator, so the methods can be chained from extending classes
     */
    protected abstract V thisValidator();

    /**
     * return new V(outerValidator, condition, subConditions, notCondition);
     * @return new instance of implementing validator, so the methods can be chained from extending classes
     */
    protected abstract V newValidator(V outerValidator, Predicate<T> mainCondition, Predicate<T> accumulatedCondition, boolean notCondition);

    /**
     * <pre>
     * You can override this method to provide custom behaviour
     *
     * Adds a predicate which tests if the object being validated is equal to some other object
     * </pre>
     */
    public V isEqual(T t) {
        return registerCondition(o -> Objects.equals(o, t));
    }

    /**
     * Adds a custom predicate for validating objects
     */
    public final V passes(Predicate<? super T> customPredicate) {
        return registerCondition(customPredicate);
    }

    /**
     * Adds a predicate which tests if the object being validated is null
     */
    public final V isNull() {
        return registerCondition(t -> t == null);
    }

    /**
     * Adds a predicate which tests if the object being validated is contained by given collection
     * @throws NullPointerException if collection is null
     * @throws ClassCastException if the collection is incompatible with the object; this is fairly rare and probably
     * won't happen for your average Collection
     */
    public final V isContainedIn(Collection<?> collection) {
        Null.check(collection).ifAny("Collection cannot be null");
        return registerCondition(collection::contains);
    }

    /**
     * Adds a predicate which tests if the object being validated is contained by given array
     * @throws NullPointerException if array is null
     */
    @SafeVarargs
    public final V isContainedIn(T... array) {
        Null.checkAlone(array).ifAny("Array cannot be null");
        return isContainedIn(Arrays.asList(array));
    }

    /**
     * Does nothing, only useful for readability
     * @throws IllegalStateException if and() is used before any condition, i.e. string().and()..
     */
    public final V and() {
        if (!hasAccumulatedAnyConditions())
            throw new IllegalStateException("There must be at least a single condition before every and()");

        return thisValidator();
    }

    /**
     * Accumulates all predicates before this or() that haven't been accumulated previously using && operator, then
     * adds it to the previously accumulated condition (if such exists) using || operator
     * @throws IllegalStateException if or() is used before any condition, i.e. string().or()...
     */
    public final V or() {
        if (!hasAccumulatedAnyConditions())
            throw new IllegalStateException("There must be at least a single condition before every or()");

        return newValidator(outerValidator, mainCondition(), null, false);
    }

    /**
     * <pre>
     * Sets the next registered condition to be negated
     *
     * Registered conditions are basically every method call that performs a boolean test, including brackets
     * </pre>
     */
    public final V not() {
        return newValidator(outerValidator, mainCondition, accumulatedCondition, !notCondition);
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
    public final V openBracket() {
        return newValidator(thisValidator(), null, null, false);
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
    public final V closeBracket() {
        if (!hasOuterValidator())
            throw new IllegalStateException("You must use openBracket() before using closeBracket()");

        return outerValidator.registerCondition(collapseCondition());
    }

    /**
     * Closes all brackets, if any, and evaluates constructed predicate for given object
     * @return true if object passes the predicate test, false otherwise
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    @Override
    public final boolean test(T object) {
        return hasOuterValidator()
                ? closeBracket().test(object)
                : collapseCondition().test(object);
    }

    /**
     * @return true if object does not pass the predicate test, false otherwise
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public final boolean isInvalid(T object) {
        return !test(object);
    }

    /**
     * @return validator actor, which allows specifying an action if the object is invalid
     */
    public final <E extends T> ValidationActor<T, E, V> ifInvalid(E object) {
        return ValidationActor.of(object, thisValidator());
    }

    /**
     * Executes an arbitrary action if and only if the given object is NOT valid
     * @throws NullPointerException if invalidAction is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public final <E extends T> V ifInvalid(E object, Runnable invalidAction) {
        Null.check(invalidAction).ifAny("Action cannot be null");
        if (isInvalid(object))
            invalidAction.run();
        return thisValidator();
    }

    /**
     * Executes an action using the object if and only if the given object is NOT valid
     * @throws NullPointerException if invalidConsumer is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public final <E extends T> V ifInvalid(E object, Consumer<? super E> invalidConsumer) {
        Null.check(invalidConsumer).ifAny("Consumer cannot be null");
        if (isInvalid(object))
            invalidConsumer.accept(object);
        return thisValidator();
    }

    /**
     * Throws an arbitrary exception if and only if the given object is NOT valid
     * @throws NullPointerException if exceptionSupplier is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public final <E extends T, X extends Throwable> V ifInvalidThrow(E object, Supplier<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");
        if (isInvalid(object))
            throw exceptionSupplier.get();
        return thisValidator();
    }

    /**
     * Throws an exception using the object if and only if the given object is NOT valid
     * @throws NullPointerException if exceptionSupplier is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public final <E extends T, X extends Throwable> V ifInvalidThrow(E object, Function<? super E, ? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");
        if (isInvalid(object))
            throw exceptionSupplier.apply(object);
        return thisValidator();
    }

    /**
     * @return this validator as just a Predicate
     */
    public final Predicate<T> asPredicate() {
        return this;
    }

    // CONSTRUCTORS

    /**
     * @return BigDecimal validator implementation
     */
    public static BigDecimalValidator bigDecimal() {
        return new BigDecimalValidator();
    }

    /**
     * @return boolean validator implementation
     */
    public static BooleanValidator bool() {
        return new BooleanValidator();
    }

    /**
     * @return char[] validator implementation
     */
    public static CharArrayValidator charArray() {
        return new CharArrayValidator();
    }

    /**
     * @return arbitrary type collection validator implementation
     */
    public static CollectionValidator<?> collection() {
        return new CollectionValidator<>();
    }

    /**
     * @return collection of given class validator implementation
     */
    public static <T> CollectionValidator<T> collectionOf(Class<T> clazz) {
        return new CollectionValidator<>();
    }

    /**
     * @return collection validator, using same type as given object, implementation
     */
    public static <T> CollectionValidator<T> collectionOf(T object) {
        return new CollectionValidator<>();
    }

    /**
     * @return collection validator, using same type as given collection, implementation
     */
    public static <T> CollectionValidator<T> collectionOf(Collection<T> collection) {
        return new CollectionValidator<>();
    }

    /**
     * @return boxed Integer validator implementation
     */
    public static IntegerValidator integer() {
        return new IntegerValidator();
    }

    /**
     * Same as codePoint()
     * @return primitive int validator implementation
     */
    public static IntValidator Int() {
        return new IntValidator();
    }

    /**
     * Same as Int(), but more clear when dealing with streams of code points
     * @return primitive int validator implementation
     */
    public static IntValidator codePoint() {
        return new IntValidator();
    }

    /**
     * @return boxed Long validator implementation
     */
    public static LongIntegerValidator longInt() {
        return new LongIntegerValidator();
    }

    /**
     * @return primitive long validator implementation
     */
    public static LongValidator Long() {
        return new LongValidator();
    }

    /**
     * Do not use if a more specific type already has an implementation!
     * @return arbitrary object validator implementation
     */
    public static ObjectValidator<?> any() {
        return new ObjectValidator<>();
    }

    /**
     * Do not use if a more specific type already has an implementation!
     * @return object of given class validator implementation
     */
    public static <T> ObjectValidator<T> a(Class<T> clazz) {
        return new ObjectValidator<>();
    }

    /**
     * Do not use if a more specific type already has an implementation!
     * @return object validator, using same type as given object, implementation
     */
    public static <T> ObjectValidator<T> a(T t) {
        return new ObjectValidator<>();
    }

    /**
     * @return string validator implementation
     */
    public static StringValidator string() {
        return new StringValidator();
    }

    protected Validate(V outerValidator, Predicate<T> mainCondition, Predicate<T> accumulatedCondition, boolean notCondition) {
        this.outerValidator = outerValidator;
        this.mainCondition = mainCondition;
        this.accumulatedCondition = accumulatedCondition;
        this.notCondition = notCondition;
    }

    // PROTECTED

    /**
     * <pre>
     * Adds a predicate to subCondition list, negating if not() was called before this method
     *
     * All extending classes should use this method to register ALL conditions
     *
     * DO NOT use this method more than once per method call, as this will cause not() to malfunction
     * </pre>
     */
    protected final V registerCondition(Predicate<? super T> predicate) {
        Null.check(predicate).ifAny("Registered predicate cannot be null");
        if (notCondition)
            predicate = predicate.negate();
        return newValidator(outerValidator, mainCondition,
                accumulatedCondition == null ? toT(predicate) : accumulatedCondition.and(predicate), false);
    }

    // PRIVATE

    private final V outerValidator;
    private final Predicate<T> mainCondition;
    private final Predicate<T> accumulatedCondition;
    private final boolean notCondition;

    /**
     * @return true if this validator is used for bracket simulation, false otherwise
     */
    private boolean hasOuterValidator() {
        return outerValidator != null;
    }

    private boolean hasAccumulatedAnyConditions() {
        return accumulatedCondition != null;
    }

    private Predicate<T> collapseCondition() {
        Predicate<T> condition = hasAccumulatedAnyConditions() ? mainCondition() : mainCondition;

        if (condition == null)
            throw new IllegalStateException("You must have at least one condition total, or between openBracket() and closeBracket()");

        return condition;
    }

    private Predicate<T> mainCondition() {
        return mainCondition == null ? accumulatedCondition : mainCondition.or(accumulatedCondition);
    }

    private static <T> Predicate<T> toT(Predicate<? super T> predicate) {
        Predicate<T> truePredicate = t -> true;
        return truePredicate.and(predicate);
    }

}
