package eu.goodlike.v2.validate.impl;

import eu.goodlike.functional.Action;
import eu.goodlike.neat.Null;
import eu.goodlike.v2.validate.Validate;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Collection validator implementation
 */
public final class CollectionValidator<T> extends Validate<Collection<T>, CollectionValidator<T>> {

    /**
     * Adds a predicate which tests if the collection being validated is empty
     */
    public CollectionValidator<T> empty() {
        return registerCondition(Collection::isEmpty);
    }

    /**
     * Adds a predicate which checks if every char in the array passes the predicate
     * @throws NullPointerException is elementPredicate is null
     */
    public CollectionValidator<T> allMatch(Predicate<T> elementPredicate) {
        Null.check(elementPredicate).ifAny("Predicate cannot be null");
        return registerCondition(collection -> collection.stream().allMatch(elementPredicate));
    }

    /**
     * Adds a predicate which checks if any char in the array passes the predicate
     * @throws NullPointerException is elementPredicate is null
     */
    public CollectionValidator<T> anyMatch(Predicate<T> elementPredicate) {
        Null.check(elementPredicate).ifAny("Predicate cannot be null");
        return registerCondition(collection -> collection.stream().anyMatch(elementPredicate));
    }

    /**
     * @return validator actor, which allows specifying an action if an element of collection is invalid
     * @throws NullPointerException is elementValidator is null
     */
    public <V extends Validate<T, V>> ElementValidatorActor<T, V> forEachIfNot(V elementValidator) {
        return new ElementValidatorActor<>(this, elementValidator);
    }

    /**
     * <pre>
     * Adds a validation check for every element, with a custom action for every invalid element
     *
     * The action will be executed for every invalid element, not just the first one found
     * </pre>
     * @throws NullPointerException is elementValidator or customAction is null
     */
    public <V extends Validate<T, V>> CollectionValidator<T> forEachIfNot(V elementValidator, Action customAction) {
        Null.check(elementValidator, customAction).ifAny("Element validator and action cannot be null");
        return registerCondition(collection -> forEach(collection, elementValidator, customAction));
    }

    /**
     * <pre>
     * Adds a validation check for every element, with a custom consumer for every invalid element
     *
     * The consumer will be executed for every invalid element, not just the first one found
     * </pre>
     * @throws NullPointerException is elementValidator or customConsumer is null
     */
    public <V extends Validate<T, V>> CollectionValidator<T> forEachIfNot(V elementValidator, Consumer<T> customConsumer) {
        Null.check(elementValidator, customConsumer).ifAny("Element validator and consumer cannot be null");
        return registerCondition(collection -> forEach(collection, elementValidator, customConsumer));
    }

    /**
     * <pre>
     * Adds a validation check for every element, with a custom exception thrown if any of them is invalid
     *
     * The exception will be thrown just for the first invalid element
     * </pre>
     * @throws NullPointerException is elementValidator or customException is null
     */
    public <V extends Validate<T, V>, X extends RuntimeException> CollectionValidator<T> forEachInvalidThrow(V elementValidator, Supplier<X> customException) {
        Null.check(elementValidator, customException).ifAny("Element validator and exception supplier cannot be null");
        return registerCondition(collection -> forEach(collection, elementValidator, customException));
    }

    /**
     * <pre>
     * Adds a validation check for every element, with a custom exception thrown if any of them is invalid
     *
     * The exception will be thrown just for the first invalid element
     * </pre>
     * @throws NullPointerException is elementValidator or customException is null
     */
    public <V extends Validate<T, V>, X extends RuntimeException> CollectionValidator<T> forEachInvalidThrow(V elementValidator, Function<T, X> customException) {
        Null.check(elementValidator, customException).ifAny("Element validator and exception supplier cannot be null");
        return registerCondition(collection -> forEach(collection, elementValidator, customException));
    }

    // CONSTRUCTORS

    public CollectionValidator() {
        this(null, null, null, false);
    }

    protected CollectionValidator(CollectionValidator<T> outerValidator, Predicate<Collection<T>> mainCondition, Predicate<Collection<T>> accumulatedCondition, boolean notCondition) {
        super(outerValidator, mainCondition, accumulatedCondition, notCondition);
    }

    // PROTECTED

    @Override
    protected CollectionValidator<T> thisValidator() {
        return this;
    }

    @Override
    protected CollectionValidator<T> newValidator(CollectionValidator<T> outerValidator, Predicate<Collection<T>> mainCondition, Predicate<Collection<T>> accumulatedCondition, boolean notCondition) {
        return new CollectionValidator<>(outerValidator, mainCondition, accumulatedCondition, notCondition);
    }

    // PRIVATE

    private static <T, V extends Validate<T, V>> boolean forEach(Collection<T> collection, V elementValidator, Action customAction) {
        boolean result = true;
        for (T e : collection) {
            elementValidator.ifInvalid(e, customAction);
            result = false;
        }
        return result;
    }

    private static <T, V extends Validate<T, V>> boolean forEach(Collection<T> collection, V elementValidator, Consumer<T> customConsumer) {
        boolean result = true;
        for (T e : collection) {
            elementValidator.ifInvalid(e, customConsumer);
            result = false;
        }
        return result;
    }

    private static <T, V extends Validate<T, V>, X extends RuntimeException> boolean forEach(Collection<T> collection, V elementValidator, Supplier<X> customException) {
        collection.forEach(e -> elementValidator.ifInvalidThrow(e, customException));
        return true;
    }

    private static <T, V extends Validate<T, V>, X extends RuntimeException> boolean forEach(Collection<T> collection, V elementValidator, Function<T, X> customException) {
        collection.forEach(e -> elementValidator.ifInvalidThrow(e, customException));
        return true;
    }

    /**
     * <pre>
     * Used by forEachIfNot() method, to postpone defining the action into a separate method call, i.e.
     *      collectionValidator.forEachIfNot(someValidator, someAction);
     * becomes
     *      collectionValidator.forEachIfNot(someValidator).Do(someAction);
     * </pre>
     */
    public static final class ElementValidatorActor<T, V extends Validate<T, V>> {
        public CollectionValidator<T> Do(Action action) {
            return collectionValidator.forEachIfNot(elementValidator, action);
        }

        public CollectionValidator<T> Do(Consumer<T> elementConsumer) {
            return collectionValidator.forEachIfNot(elementValidator, elementConsumer);
        }

        public <X extends RuntimeException> CollectionValidator<T> Throw(Supplier<X> exceptionSupplier) {
            return collectionValidator.forEachInvalidThrow(elementValidator, exceptionSupplier);
        }

        public <X extends RuntimeException> CollectionValidator<T> Throw(Function<T, X> exceptionFunction) {
            return collectionValidator.forEachInvalidThrow(elementValidator, exceptionFunction);
        }

        // CONSTRUCTORS

        private ElementValidatorActor(CollectionValidator<T> collectionValidator, V elementValidator) {
            this.collectionValidator = collectionValidator;
            this.elementValidator = elementValidator;
        }

        // PRIVATE

        private final CollectionValidator<T> collectionValidator;
        private final V elementValidator;
    }

}
