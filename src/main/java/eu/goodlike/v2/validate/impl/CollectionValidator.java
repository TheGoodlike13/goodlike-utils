package eu.goodlike.v2.validate.impl;

import eu.goodlike.functional.Action;
import eu.goodlike.neat.Null;
import eu.goodlike.v2.validate.Validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
     */
    public CollectionValidator<T> allMatch(Predicate<T> elementPredicate) {
        Null.check(elementPredicate).ifAny("Predicate cannot be null");
        return registerCondition(collection -> collection.stream().allMatch(elementPredicate));
    }

    /**
     * Adds a predicate which checks if any char in the array passes the predicate
     */
    public CollectionValidator<T> anyMatch(Predicate<T> elementPredicate) {
        Null.check(elementPredicate).ifAny("Predicate cannot be null");
        return registerCondition(collection -> collection.stream().anyMatch(elementPredicate));
    }

    /**
     * <pre>
     * Adds a validation check for every element, with a custom action for every invalid element
     *
     * The action will be executed for every invalid element, not just the first one found
     * </pre>
     */
    public <V extends Validate<T, V>> CollectionValidator<T> forEachIf(V elementValidator, Action customAction) {
        Null.check(elementValidator, customAction).ifAny("Predicate and action cannot be null");
        return registerCondition(collection -> forEach(collection, elementValidator, customAction));
    }

    /**
     * <pre>
     * Adds a validation check for every element, with a custom consumer for every invalid element
     *
     * The consumer will be executed for every invalid element, not just the first one found
     * </pre>
     */
    public <V extends Validate<T, V>> CollectionValidator<T> forEachIf(V elementValidator, Consumer<T> customConsumer) {
        Null.check(elementValidator, customConsumer).ifAny("Predicate and consumer cannot be null");
        return registerCondition(collection -> forEach(collection, elementValidator, customConsumer));
    }

    /**
     * <pre>
     * Adds a validation check for every element, with a custom exception thrown if any of them is invalid
     *
     * The exception will be thrown just for the first invalid element
     * </pre>
     */
    public <V extends Validate<T, V>, X extends RuntimeException> CollectionValidator<T> forEachThrowIf(V elementValidator, Supplier<X> customException) {
        Null.check(elementValidator, customException).ifAny("Predicate and exception supplier cannot be null");
        return registerCondition(collection -> forEach(collection, elementValidator, customException));
    }

    /**
     * <pre>
     * Adds a validation check for every element, with a custom exception thrown if any of them is invalid
     *
     * The exception will be thrown just for the first invalid element
     * </pre>
     */
    public <V extends Validate<T, V>, X extends RuntimeException> CollectionValidator<T> forEachThrowIf(V elementValidator, Function<T, X> customException) {
        Null.check(elementValidator, customException).ifAny("Predicate and exception supplier cannot be null");
        return registerCondition(collection -> forEach(collection, elementValidator, customException));
    }

    // CONSTRUCTORS

    public CollectionValidator() {
        this(null, null, new ArrayList<>(), false);
    }

    protected CollectionValidator(CollectionValidator<T> outerValidator, Predicate<Collection<T>> condition, List<Predicate<Collection<T>>> subConditions, boolean notCondition) {
        super(outerValidator, condition, subConditions, notCondition);
    }

    // PROTECTED

    @Override
    protected CollectionValidator<T> thisValidator() {
        return this;
    }

    @Override
    protected CollectionValidator<T> newValidator(CollectionValidator<T> outerValidator, Predicate<Collection<T>> condition, List<Predicate<Collection<T>>> subConditions, boolean notCondition) {
        return new CollectionValidator<>(outerValidator, condition, subConditions, notCondition);
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

}
