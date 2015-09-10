package eu.goodlike.neat;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.*;

/**
 * <pre>
 * Contains one of two possible type of objects, never both; it can, however, contain neither
 *
 * This class mimics java.util.Optional behaviour, and uses it internally wherever possible, since most
 * of the logic is quite the same;
 * </pre>
 */
public final class Either<T1, T2> {

    /**
     * @return value of first kind if this Either is of first kind
     * @throws NoSuchElementException if this Either is of second kind
     */
    public T1 getFirstKind() {
        return firstKind.get();
    }

    /**
     * @return value of second kind if this Either is of second kind
     * @throws NoSuchElementException if this Either is of first kind
     */
    public T2 getSecondKind() {
        return secondKind.get();
    }

    /**
     * @return optional value of first kind if this Either is of first kind
     */
    public Optional<T1> getFirstOptional() {
        return firstKind;
    }

    /**
     * @return optional value of second kind if this Either is of second kind
     */
    public Optional<T2> getSecondOptional() {
        return secondKind;
    }

    /**
     * @return true if this Either is of first kind
     */
    public boolean isFirstKind() {
        return firstKind.isPresent();
    }

    /**
     * @return true if this Either is of second kind
     */
    public boolean isSecondKind() {
        return secondKind.isPresent();
    }

    /**
     * @return true if this Either is of neither kind
     */
    public boolean isNeitherKind() {
        return !isFirstKind() && !isSecondKind();
    }

    /**
     * Invokes the consumer if this Either is of first kind, otherwise does nothing
     * @throws NullPointerException if consumer is null
     */
    public void ifFirstKind(Consumer<? super T1> consumer) {
        Null.check(consumer).ifAny("Null consumers not allowed");
        firstKind.ifPresent(consumer);
    }

    /**
     * Invokes the consumer if this Either is of second kind, otherwise does nothing
     * @throws NullPointerException if consumer is null
     */
    public void ifSecondKind(Consumer<? super T2> consumer) {
        Null.check(consumer).ifAny("Null consumers not allowed");
        secondKind.ifPresent(consumer);
    }

    /**
     * Invokes the first consumer if this Either is of first kind, and the second consumer if it is of second kind
     * @throws NullPointerException if any of the consumers are null
     */
    public void ifEitherKind(Consumer<? super T1> consumer1, Consumer<? super T2> consumer2) {
        Null.check(consumer1, consumer2).ifAny("Null consumers not allowed");
        ifFirstKind(consumer1);
        ifSecondKind(consumer2);
    }

    /**
     * @return Either with its first value filtered using the predicate; this only has an effect if this Either
     * is of first kind; this will make the Either lose its kind if the predicate does not hold for the present
     * value, otherwise the kind is retained
     * @throws NullPointerException if predicate is null
     */
    public Either<T1, T2> filterFirstKind(Predicate<? super T1> predicate) {
        Null.check(predicate).ifAny("Null predicates not allowed");
        return of(firstKind.filter(predicate), secondKind);
    }

    /**
     * @return Either with its second value filtered using the predicate; this only has an effect if this Either
     * is of second kind; this will make the Either lose its kind if the predicate does not hold for the present
     * value, otherwise the kind is retained
     * @throws NullPointerException if predicate is null
     */
    public Either<T1, T2> filterSecondKind(Predicate<? super T2> predicate) {
        Null.check(predicate).ifAny("Null predicates not allowed");
        return of(firstKind, secondKind.filter(predicate));
    }

    /**
     * @return Either with its first value filtered using the first predicate, and its second value filtered using
     * the second predicate; this will make the Either lose its kind if the predicate does not hold for the present
     * value, otherwise the kind is retained
     * @throws NullPointerException if any of the predicates are null
     */
    public Either<T1, T2> filter(Predicate<? super T1> predicate1, Predicate<? super T2> predicate2) {
        Null.check(predicate1, predicate2).ifAny("Null predicates not allowed");
        return of(firstKind.filter(predicate1), secondKind.filter(predicate2));
    }

    /**
     * @return Either with its first value mapped using the mapper; this only has an effect if this Either
     * is of the first kind
     * @throws NullPointerException if mapper is null
     */
    public <U1> Either<U1, T2> mapFirstKind(Function<? super T1, ? extends U1> mapper) {
        Null.check(mapper).ifAny("Null mappers not allowed");
        return of(firstKind.map(mapper), secondKind);
    }

    /**
     * @return Either with its second value mapped using the mapper; this only has an effect if this Either
     * is of the second kind
     * @throws NullPointerException if mapper is null
     */
    public <U2> Either<T1, U2> mapSecondKind(Function<? super T2, ? extends U2> mapper) {
        Null.check(mapper).ifAny("Null mappers not allowed");
        return of(firstKind, secondKind.map(mapper));
    }

    /**
     * @return Either with its first value mapped using the first mapper, and its second value mapped using
     * the second mapper
     * @throws NullPointerException if any of the mappers are null
     */
    public <U1, U2> Either<U1, U2> map(Function<? super T1, ? extends U1> mapper1,
                                       Function<? super T2, ? extends U2> mapper2) {
        Null.check(mapper1, mapper2).ifAny("Null mappers not allowed");
        return of(firstKind.map(mapper1), secondKind.map(mapper2));
    }

    /**
     * @return Optional of this Either's first value, mapped using mapper
     * @throws NullPointerException if mapper is null
     */
    public <U> Optional<U> mapFirstKindInto(Function<? super T1, ? extends U> mapper) {
        Null.check(mapper).ifAny("Null mappers not allowed");
        return firstKind.map(mapper);
    }

    /**
     * @return Optional of this Either's second value, mapped using mapper
     * @throws NullPointerException if mapper is null
     */
    public <U> Optional<U> mapSecondKindInto(Function<? super T2, ? extends U> mapper) {
        Null.check(mapper).ifAny("Null mappers not allowed");
        return secondKind.map(mapper);
    }

    /**
     * @return Optional of this Either's value, mapped using mapper1 if this Either is of first kind, and mapper2
     * if this Either is of second kind
     * @throws NullPointerException if any of the mappers are null
     */
    public <U> Optional<U> mapInto(Function<? super T1, ? extends U> mapper1,
                                   Function<? super T2, ? extends U> mapper2) {
        Null.check(mapper1, mapper2).ifAny("Null mappers not allowed");
        return isFirstKind()
                ? firstKind.map(mapper1)
                : secondKind.map(mapper2);
    }

    /**
     * @return Optional of this Either's value, mapped using mapper; mapper MUST expect one of the values to be null!
     * @throws NullPointerException if mapper is null
     */
    public <U> Optional<U> mapInto(BiFunction<? super T1, ? super T2, ? extends U> mapper) {
        Null.check(mapper).ifAny("Null mappers not allowed");
        return isFirstKind()
                ? Optional.ofNullable(mapper.apply(firstKind.get(), null))
                : isSecondKind() ? Optional.ofNullable(mapper.apply(null, secondKind.get())) : Optional.empty();
    }

    /**
     * @return similar to mapInto(), except that the BiFunction should return an Either (to avoid Optional of Either)
     */
    public <U1, U2> Either<U1, U2> flatMap(BiFunction<? super T1, ? super T2, Either<U1, U2>> mapper) {
        Null.check(mapper).ifAny("Null mappers not allowed");
        return isFirstKind()
                ? mapper.apply(firstKind.get(), null)
                : isSecondKind() ? mapper.apply(null, secondKind.get()) : neither();
    }

    /**
     * @return similar to mapInto(), except that the BiFunction should return an Optional (to avoid Optional of Optional)
     */
    public <U> Optional<U> flatMapInto(BiFunction<? super T1, ? super T2, Optional<U>> mapper) {
        Null.check(mapper).ifAny("Null mappers not allowed");
        return isFirstKind()
                ? mapper.apply(firstKind.get(), null)
                : isSecondKind() ? mapper.apply(null, secondKind.get()) : Optional.empty();
    }

    /**
     * @return first value if this Either is of first kind, given value otherwise
     */
    public T1 firstOrElse(T1 other) {
        return firstKind.orElse(other);
    }

    /**
     * @return first value if this Either is of first kind, result of supplier otherwise
     */
    public T1 firstOrElseGet(Supplier<? extends T1> other) {
        return firstKind.orElseGet(other);
    }

    /**
     * @return first value if this Either is of first kind
     * @throws X if this Either is not of first kind
     */
    public <X extends Throwable> T1 firstOrElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return firstKind.orElseThrow(exceptionSupplier);
    }

    /**
     * @return second value if this Either is of second kind, given value otherwise
     */
    public T2 secondOrElse(T2 other) {
        return secondKind.orElse(other);
    }

    /**
     * @return second value if this Either is of second kind, result of supplier otherwise
     */
    public T2 secondOrElseGet(Supplier<? extends T2> other) {
        return secondKind.orElseGet(other);
    }

    /**
     * @return second value if this Either is of second kind
     * @throws X if this Either is not of second kind
     */
    public <X extends Throwable> T2 secondOrElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return secondKind.orElseThrow(exceptionSupplier);
    }

    /**
     * @return this Either
     * @throws X if this Either is of neither kind
     */
    public <X extends Throwable> Either<T1, T2> ifNeitherThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isNeitherKind())
            throw exceptionSupplier.get();

        return this;
    }

    /**
     * @return Either with its types and value swapped around
     */
    public Either<T2, T1> swap() {
        return of(secondKind, firstKind);
    }

    // CONSTRUCTORS

    public static <T1, T2> Either<T1, T2> neither() {
        @SuppressWarnings("unchecked")
        Either<T1, T2> empty = (Either<T1, T2>)NEITHER;
        return empty;
    }

    public static <T1, T2> Either<T1, T2> ofFirstKind(T1 t) {
        return ofFirstKind(Optional.ofNullable(t));
    }

    public static <T1, T2> Either<T1, T2> ofFirstKind(Optional<T1> t) {
        return t == null || !t.isPresent() ? neither() : new Either<>(t, Optional.empty());
    }

    public static <T1, T2> Either<T1, T2> ofSecondKind(T2 t) {
        return ofSecondKind(Optional.ofNullable(t));
    }

    public static <T1, T2> Either<T1, T2> ofSecondKind(Optional<T2> t) {
        return t == null || !t.isPresent() ? neither() : new Either<>(Optional.empty(), t);
    }

    public static <T1, T2> Either<T1, T2> of(T1 t1, T2 t2) {
        if (t1 != null && t2 != null)
            throw new IllegalArgumentException("At least one of arguments has to be null");

        return of(Optional.ofNullable(t1), Optional.ofNullable(t2));
    }

    public static <T1, T2> Either<T1, T2> of(Optional<T1> t1, Optional<T2> t2) {
        if (t1 != null && t2 != null && t1.isPresent() && t2.isPresent())
            throw new IllegalArgumentException("At least one of arguments has to be null");

        return t1 == null || !t1.isPresent()
                ? ofSecondKind(t2)
                : ofFirstKind(t1);
    }

    private Either(Optional<T1> firstKind, Optional<T2> secondKind) {
        this.firstKind = firstKind;
        this.secondKind = secondKind;
    }

    // PRIVATE

    private final Optional<T1> firstKind;
    private final Optional<T2> secondKind;

    private static final Either<?, ?> NEITHER = new Either<>(Optional.empty(), Optional.empty());

}
