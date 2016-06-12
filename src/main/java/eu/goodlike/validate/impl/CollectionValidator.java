package eu.goodlike.validate.impl;

import com.google.common.collect.Sets;
import eu.goodlike.neat.Null;
import eu.goodlike.validate.Validator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Validator implementation for any Collection
 */
public final class CollectionValidator<T> extends Validator<Collection<T>, CollectionValidator<T>> {

    /**
     * Adds a predicate which tests if the collection being validated is empty
     */
    public CollectionValidator<T> isEmpty() {
        return registerCondition(Collection::isEmpty);
    }

    /**
     * Adds a predicate which tests if the collection being validated has given amount of elements
     */
    public CollectionValidator<T> hasSizeOf(int size) {
        return hasSizeMatching(i -> i == size);
    }

    /**
     * Adds a predicate which tests if the collection being validated has more than given amount of elements
     */
    public CollectionValidator<T> hasSizeLargerThan(int size) {
        return hasSizeMatching(i -> i > size);
    }

    /**
     * Adds a predicate which tests if the collection being validated has less than given amount of elements
     */
    public CollectionValidator<T> hasSizeSmallerThan(int size) {
        return hasSizeMatching(i -> i < size);
    }

    /**
     * Adds a predicate which tests if the collection being validated has at most given amount of elements
     */
    public CollectionValidator<T> hasSizeAtMost(int size) {
        return hasSizeMatching(i -> i <= size);
    }

    /**
     * Adds a predicate which tests if the collection being validated has at least given amount of elements
     */
    public CollectionValidator<T> hasSizeAtLeast(int size) {
        return hasSizeMatching(i -> i >= size);
    }

    /**
     * Adds a predicate which tests if the collection being validated has amount of elements, which passes given predicate
     * @throws NullPointerException if sizePredicate is null
     */
    public CollectionValidator<T> hasSizeMatching(IntPredicate sizePredicate) {
        return passesAsInt(Collection::size, sizePredicate);
    }

    /**
     * Adds a predicate which tests if the collection being validated contains given element, using the collection's
     * contains() method
     */
    public CollectionValidator<T> contains(T t) {
        return registerCondition(c -> c.contains(t));
    }

    /**
     * Adds a predicate which tests if the collection being validated contains given element, if it is present, using
     * the collection's contains() method
     * @throws NullPointerException if optional is null
     */
    public CollectionValidator<T> contains(Optional<? extends T> optional) {
        Null.check(optional).ifAny("Optional cannot be null");
        return optional.isPresent()
                ? contains(optional.get())
                : this;
    }

    /**
     * Adds a predicate which tests if the collection being validated contains given element, using the given
     * BiPredicate to establish equality
     * @throws NullPointerException if customEqualityCheck is null
     */
    public CollectionValidator<T> contains(T t, BiPredicate<? super T, ? super T> customEqualityCheck) {
        Null.check(customEqualityCheck).ifAny("Custom equality check cannot be null");
        return anyMatch(e -> customEqualityCheck.test(t, e));
    }

    /**
     * Adds a predicate which tests if the collection being validated contains given element, if it is present, using
     * the given BiPredicate to establish equality
     * @throws NullPointerException if optional or customEqualityCheck is null
     */
    public CollectionValidator<T> contains(Optional<? extends T> optional, BiPredicate<? super T, ? super T> customEqualityCheck) {
        Null.check(optional).ifAny("Optional cannot be null");
        return optional.isPresent()
                ? contains(optional.get(), customEqualityCheck)
                : this;
    }

    /**
     * Adds a predicate which tests if the collection being validated contains all given elements, using the
     * collection's containsAll() method
     * @throws NullPointerException if array is null
     */
    @SafeVarargs
    public final CollectionValidator<T> containsAll(T... array) {
        Null.checkAlone(array).ifAny("Array cannot be null");
        return containsAll(Sets.newHashSet(array));
    }

    /**
     * Adds a predicate which tests if the collection being validated contains all elements from given collection,
     * using the collection's containsAll() method
     * @throws NullPointerException if collection is null
     */
    public CollectionValidator<T> containsAll(Collection<? extends T> collection) {
        Null.check(collection).ifAny("Collection cannot be null");
        return registerCondition(c -> c.containsAll(collection));
    }

    /**
     * Adds a predicate which tests if the collection being validated contains all elements from given iterable,
     * using the collection's containsAll() method
     * @throws NullPointerException if iterable is null
     */
    public CollectionValidator<T> containsAll(Iterable<? extends T> iterable) {
        Null.check(iterable).ifAny("Iterable cannot be null");
        return containsAll(Sets.newHashSet(iterable));
    }

    /**
     * Adds a predicate which tests if the collection being validated contains all given elements, using the
     * given BiPredicate to establish equality
     * @throws NullPointerException if array or customEqualityCheck is null
     */
    @SafeVarargs
    public final CollectionValidator<T> containsAll(BiPredicate<? super T, ? super T> customEqualityCheck, T... array) {
        Null.check(array, customEqualityCheck).ifAny("Array and custom equality check cannot be null");
        return registerCondition(c -> Stream.of(array).allMatch(
                givenElement -> c.stream().anyMatch(
                        collectionElement -> customEqualityCheck.test(collectionElement, givenElement))));
    }

    /**
     * Adds a predicate which tests if the collection being validated contains all elements from given collection,
     * using the given BiPredicate to establish equality
     * @throws NullPointerException if array or customEqualityCheck is null
     */
    public CollectionValidator<T> containsAll(Collection<? extends T> collection, BiPredicate<? super T, ? super T> customEqualityCheck) {
        Null.check(collection, customEqualityCheck).ifAny("Collection and custom equality check cannot be null");
        return registerCondition(c -> collection.stream().allMatch(
                givenElement -> c.stream().anyMatch(
                        collectionElement -> customEqualityCheck.test(collectionElement, givenElement))));
    }

    /**
     * Adds a predicate which tests if the collection being validated contains all elements from given iterable,
     * using the given BiPredicate to establish equality
     * @throws NullPointerException if array or customEqualityCheck is null
     */
    public CollectionValidator<T> containsAll(Iterable<? extends T> iterable, BiPredicate<? super T, ? super T> customEqualityCheck) {
        Null.check(iterable, customEqualityCheck).ifAny("Iterable and custom equality check cannot be null");
        return registerCondition(c -> StreamSupport.stream(iterable.spliterator(), false).allMatch(
                givenElement -> c.stream().anyMatch(
                        collectionElement -> customEqualityCheck.test(collectionElement, givenElement))));
    }

    /**
     * Adds a predicate which tests if the collection being validated contains any given elements, using the
     * collection's contains() method
     * @throws NullPointerException if array is null
     */
    @SafeVarargs
    public final CollectionValidator<T> containsAny(T... array) {
        Null.checkAlone(array).ifAny("Array cannot be null");
        return registerCondition(c -> Stream.of(array).anyMatch(c::contains));
    }

    /**
     * Adds a predicate which tests if the collection being validated contains any elements from given collection,
     * using the collection's contains() method
     * @throws NullPointerException if array is null
     */
    public CollectionValidator<T> containsAny(Collection<? extends T> collection) {
        Null.checkAlone(collection).ifAny("Collection cannot be null");
        return registerCondition(c -> collection.stream().anyMatch(c::contains));
    }

    /**
     * Adds a predicate which tests if the collection being validated contains any elements from given iterable,
     * using the collection's contains() method
     * @throws NullPointerException if array is null
     */
    public CollectionValidator<T> containsAny(Iterable<? extends T> iterable) {
        Null.checkAlone(iterable).ifAny("Iterable cannot be null");
        return registerCondition(c -> StreamSupport.stream(iterable.spliterator(), false).anyMatch(c::contains));
    }

    /**
     * Adds a predicate which tests if the collection being validated contains any given elements, using the given
     * BiPredicate to establish equality
     * @throws NullPointerException if array or customEqualityCheck is null
     */
    @SafeVarargs
    public final CollectionValidator<T> containsAny(BiPredicate<? super T, ? super T> customEqualityCheck, T... array) {
        Null.check(array, customEqualityCheck).ifAny("Array and custom equality check cannot be null");
        return registerCondition(c -> Stream.of(array).anyMatch(
                givenElement -> c.stream().anyMatch(
                        collectionElement -> customEqualityCheck.test(collectionElement, givenElement))));
    }

    /**
     * Adds a predicate which tests if the collection being validated contains any elements from given collection,
     * using the given BiPredicate to establish equality
     * @throws NullPointerException if collection or customEqualityCheck is null
     */
    public CollectionValidator<T> containsAny(Collection<? extends T> collection, BiPredicate<? super T, ? super T> customEqualityCheck) {
        Null.check(collection, customEqualityCheck).ifAny("Collection and custom equality check cannot be null");
        return registerCondition(c -> collection.stream().anyMatch(
                givenElement -> c.stream().anyMatch(
                        collectionElement -> customEqualityCheck.test(collectionElement, givenElement))));
    }

    /**
     * Adds a predicate which tests if the collection being validated contains any elements from given iterable,
     * using the given BiPredicate to establish equality
     * @throws NullPointerException if iterable or customEqualityCheck is null
     */
    public CollectionValidator<T> containsAny(Iterable<? extends T> iterable, BiPredicate<? super T, ? super T> customEqualityCheck) {
        Null.check(iterable, customEqualityCheck).ifAny("Iterable and custom equality check cannot be null");
        return registerCondition(c -> StreamSupport.stream(iterable.spliterator(), false).anyMatch(
                givenElement -> c.stream().anyMatch(
                        collectionElement -> customEqualityCheck.test(collectionElement, givenElement))));
    }

    /**
     * Adds a predicate which tests if the elements of collection being validated are all contained in given array,
     * viewing it as a HashSet
     * @throws NullPointerException if array is null
     */
    @SafeVarargs
    public final CollectionValidator<T> isSubsetOf(T... array) {
        Null.checkAlone(array).ifAny("Array cannot be null");
        return isSubsetOf(Sets.newHashSet(array));
    }

    /**
     * Adds a predicate which tests if the elements of collection being validated are all contained in given collection,
     * viewing it as a HashSet
     * @throws NullPointerException if collection is null
     */
    public CollectionValidator<T> isSubsetOf(Collection<? extends T> collection) {
        Null.check(collection).ifAny("Collection cannot be null");
        return isSubsetOf(Sets.newHashSet(collection));
    }

    /**
     * Adds a predicate which tests if the elements of collection being validated are all contained in given iterable,
     * viewing it as a HashSet
     * @throws NullPointerException if iterable is null
     */
    public CollectionValidator<T> isSubsetOf(Iterable<? extends T> iterable) {
        Null.check(iterable).ifAny("Iterable cannot be null");
        return isSubsetOf(Sets.newHashSet(iterable));
    }

    /**
     * Adds a predicate which tests if the elements of collection being validated are all contained in given HashSet,
     * using its containsAll() method
     * @throws NullPointerException if hashSet is null
     */
    public CollectionValidator<T> isSubsetOf(HashSet<? extends T> hashSet) {
        Null.check(hashSet).ifAny("Hash set cannot be null");
        return registerCondition(hashSet::containsAll);
    }

    /**
     * Adds a predicate which tests if the elements of collection being validated are all contained in given array,
     * viewing it as a HashSet
     * @throws NullPointerException if array is null
     */
    @SafeVarargs
    public final CollectionValidator<T> isSubsetOf(BiPredicate<? super T, ? super T> customEqualityCheck, T... array) {
        Null.check(array, customEqualityCheck).ifAny("Array and custom equality check cannot be null");
        return registerCondition(c -> c.stream().allMatch(
                collectionElement -> Stream.of(array).anyMatch(
                        givenElement -> customEqualityCheck.test(collectionElement, givenElement))));
    }

    /**
     * Adds a predicate which tests if the elements of collection being validated are all contained in given collection,
     * viewing it as a HashSet
     * @throws NullPointerException if collection is null
     */
    public CollectionValidator<T> isSubsetOf(Collection<? extends T> collection, BiPredicate<? super T, ? super T> customEqualityCheck) {
        Null.check(collection, customEqualityCheck).ifAny("Collection and custom equality check cannot be null");
        return registerCondition(c -> c.stream().allMatch(
                collectionElement -> collection.stream().anyMatch(
                        givenElement -> customEqualityCheck.test(collectionElement, givenElement))));
    }

    /**
     * Adds a predicate which tests if the elements of collection being validated are all contained in given iterable,
     * viewing it as a HashSet
     * @throws NullPointerException if iterable is null
     */
    public CollectionValidator<T> isSubsetOf(Iterable<? extends T> iterable, BiPredicate<? super T, ? super T> customEqualityCheck) {
        Null.check(iterable, customEqualityCheck).ifAny("Iterable and custom equality check cannot be null");
        return registerCondition(c -> c.stream().allMatch(
                collectionElement -> StreamSupport.stream(iterable.spliterator(), false).anyMatch(
                        givenElement -> customEqualityCheck.test(collectionElement, givenElement))));
    }

    /**
     * Adds a predicate which tests if all the elements of the collection being validated pass given predicate
     * @throws NullPointerException if predicate is null
     */
    public CollectionValidator<T> allMatch(Predicate<? super T> predicate) {
        Null.check(predicate).ifAny("Predicate cannot be null");
        return registerCondition(c -> c.stream().allMatch(predicate));
    }

    /**
     * Adds a predicate which tests if any of the elements of the collection being validated pass given predicate
     * @throws NullPointerException if predicate is null
     */
    public CollectionValidator<T> anyMatch(Predicate<? super T> predicate) {
        Null.check(predicate).ifAny("Predicate cannot be null");
        return registerCondition(c -> c.stream().anyMatch(predicate));
    }

    /**
     * Adds a predicate which tests if the amount of the elements of the collection being validated, which pass given
     * predicate, pass given IntPredicate
     * @throws NullPointerException if predicate is null
     */
    public CollectionValidator<T> hasAmountMatching(IntPredicate amountPredicate, Predicate<? super T> predicate) {
        Null.check(amountPredicate, predicate).ifAny("Predicates cannot be null");
        return passesAsInt(c -> (int) c.stream().filter(predicate).count(), amountPredicate);
    }

    /**
     * Adds a predicate which tests if given amount of the elements of the collection being validated pass given predicate
     * @throws NullPointerException if predicate is null
     */
    public CollectionValidator<T> hasExactly(int amount, Predicate<? super T> predicate) {
        return hasAmountMatching(i -> i == amount, predicate);
    }

    /**
     * Adds a predicate which tests if less than given amount of the elements of the collection being validated
     * pass given predicate
     * @throws NullPointerException if predicate is null
     */
    public CollectionValidator<T> hasLessThan(int amount, Predicate<? super T> predicate) {
        return hasAmountMatching(i -> i < amount, predicate);
    }

    /**
     * Adds a predicate which tests if more than given amount of the elements of the collection being validated
     * pass given predicate
     * @throws NullPointerException if predicate is null
     */
    public CollectionValidator<T> hasMoreThan(int amount, Predicate<? super T> predicate) {
        return hasAmountMatching(i -> i > amount, predicate);
    }

    /**
     * Adds a predicate which tests if at least given amount of the elements of the collection being validated
     * pass given predicate
     * @throws NullPointerException if predicate is null
     */
    public CollectionValidator<T> hasAtLeast(int amount, Predicate<? super T> predicate) {
        return hasAmountMatching(i -> i >= amount, predicate);
    }

    /**
     * Adds a predicate which tests if at most given amount of the elements of the collection being validated
     * pass given predicate
     * @throws NullPointerException if predicate is null
     */
    public CollectionValidator<T> hasAtMost(int amount, Predicate<? super T> predicate) {
        return hasAmountMatching(i -> i <= amount, predicate);
    }

    /**
     * Adds a predicate which tests if the collection being validated contains any duplicates using Object.equals()
     * to determine equality
     */
    public CollectionValidator<T> containsDuplicates() {
        return registerCondition(c -> c.stream().distinct().count() == c.size());
    }

    /**
     * @return validator actor, which allows to specify actions to do for elements which do not pass given predicate
     * @throws NullPointerException if predicate is null
     */
    public CollectionValidatorActor<T> forFailing(Predicate<? super T> predicate) {
        return CollectionValidatorActor.of(predicate, thisValidator());
    }

    /**
     * Adds a predicate which tests all the elements of this collection, and executes given action for every element
     * that does not pass it
     * @throws NullPointerException if predicate or customAction is null
     */
    public CollectionValidator<T> forEachFailingRun(Predicate<? super T> predicate, Runnable customAction) {
        Null.check(predicate, customAction).ifAny("Predicate and runnable cannot be null");
        return registerCondition(c -> forEachRun(c, predicate, customAction));
    }

    /**
     * Adds a predicate which tests all the elements of this collection, and consumes every element that does not pass
     * with given consumer
     * @throws NullPointerException if predicate or customAction is null
     */
    public CollectionValidator<T> forEachFailingAccept(Predicate<? super T> predicate, Consumer<? super T> customConsumer) {
        Null.check(predicate, customConsumer).ifAny("Predicate and consumer cannot be null");
        return registerCondition(c -> forEachAccept(c, predicate, customConsumer));
    }

    /**
     * Adds a predicate which tests all the elements of this collection, and throws an exception from given supplier
     * if any element does not pass it
     * @throws NullPointerException if predicate or customAction is null
     */
    public <X extends RuntimeException> CollectionValidator<T> forFirstFailingThrow(Predicate<? super T> predicate,
                                                                                    Supplier<? extends X> customException) {
        Null.check(predicate, customException).ifAny("Predicate and supplier cannot be null");
        return registerCondition(c -> forFirstThrow(c, predicate, customException));
    }

    /**
     * Adds a predicate which tests all the elements of this collection, and if any element does not pass it, throws an
     * exception using it in given function
     * @throws NullPointerException if predicate or customAction is null
     */
    public <X extends RuntimeException> CollectionValidator<T> forFirstFailingThrowWith(Predicate<? super T> predicate,
                                                                                        Function<? super T, ? extends X> customException) {
        Null.check(predicate, customException).ifAny("Predicate and function cannot be null");
        return registerCondition(c -> forFirstThrowWith(c, predicate, customException));
    }

    // CONSTRUCTORS

    public CollectionValidator() {
        super();
    }

    protected CollectionValidator(Predicate<Collection<T>> mainCondition, Predicate<Collection<T>> accumulatedCondition, boolean negateNext) {
        super(mainCondition, accumulatedCondition, negateNext);
    }

    // PROTECTED

    @Override
    protected CollectionValidator<T> thisValidator() {
        return this;
    }

    @Override
    protected CollectionValidator<T> newValidator(Predicate<Collection<T>> mainCondition, Predicate<Collection<T>> accumulatedCondition, boolean negateNext) {
        return new CollectionValidator<>(mainCondition, accumulatedCondition, negateNext);
    }

    // PRIVATE

    private static <T> boolean forEachRun(Collection<T> collection,
                                          Predicate<? super T> condition,
                                          Runnable customAction) {
        boolean result = true;
        for (T e : collection)
            if (!condition.test(e)) {
                customAction.run();
                result = false;
            }

        return result;
    }

    private static <T> boolean forEachAccept(Collection<T> collection,
                                             Predicate<? super T> condition,
                                             Consumer<? super T> customConsumer) {
        boolean result = true;
        for (T e : collection)
            if (!condition.test(e)) {
                customConsumer.accept(e);
                result = false;
            }

        return result;
    }

    private static <T, X extends RuntimeException> boolean forFirstThrow(Collection<T> collection,
                                                                         Predicate<? super T> condition,
                                                                         Supplier<? extends X> customException) {
        for (T e : collection)
            if (!condition.test(e))
                throw customException.get();

        return true;
    }

    private static <T, X extends RuntimeException> boolean forFirstThrowWith(Collection<T> collection,
                                                                             Predicate<? super T> condition,
                                                                             Function<? super T, ? extends X> customException) {
        for (T e : collection)
            if (!condition.test(e))
                throw customException.apply(e);

        return true;
    }

    /**
     * Actor which allows to specify what to do with invalid elements of the collection
     */
    public static final class CollectionValidatorActor<T> {
        /**
         * Executes an arbitrary action for every invalid element in the collection
         * @throws NullPointerException if someAction is null
         */
        public CollectionValidator<T> doRun(Runnable someAction) {
            return validator.forEachFailingRun(predicate, someAction);
        }

        /**
         * Consumes the value of every invalid element in the collection
         * @throws NullPointerException if valueConsumer is null
         */
        public CollectionValidator<T> doAccept(Consumer<? super T> valueConsumer) {
            return validator.forEachFailingAccept(predicate, valueConsumer);
        }

        /**
         * Throws an arbitrary exception for first invalid element in the collection
         * @throws NullPointerException if exceptionSupplier is null
         */
        public <X extends RuntimeException> CollectionValidator<T> doThrow(Supplier<? extends X> exceptionSupplier) throws X {
            return validator.forFirstFailingThrow(predicate, exceptionSupplier);
        }

        /**
         * Throws an exception using the value if and only if an invalid value is passed
         * @throws NullPointerException if exceptionFunction is null
         */
        public <X extends RuntimeException> CollectionValidator<T> doThrowWith(Function<? super T, ? extends X> exceptionFunction) throws X {
            return validator.forFirstFailingThrowWith(predicate, exceptionFunction);
        }

        // CONSTRUCTORS

        public static <T> CollectionValidatorActor<T> of(Predicate<? super T> predicate, CollectionValidator<T> validator) {
            Null.check(predicate, validator).ifAny("Predicate and validator cannot be null");
            return new CollectionValidatorActor<>(predicate, validator);
        }

        private CollectionValidatorActor(Predicate<? super T> predicate, CollectionValidator<T> validator) {
            this.predicate = predicate;
            this.validator = validator;
        }

        // PRIVATE

        private final Predicate<? super T> predicate;
        private final CollectionValidator<T> validator;
    }

}
