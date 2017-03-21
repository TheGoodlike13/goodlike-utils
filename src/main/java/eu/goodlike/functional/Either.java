package eu.goodlike.functional;

import eu.goodlike.functional.impl.either.Left;
import eu.goodlike.functional.impl.either.Neither;
import eu.goodlike.functional.impl.either.Right;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.*;

/**
 * Contains one of two possible type of objects, left or right, never both; it can, however, contain neither
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface Either<L, R> {

    /**
     * @return left value
     * @throws NoSuchElementException if this Either is right or neither
     */
    L getLeft();

    /**
     * @return right value
     * @throws NoSuchElementException if this Either is left or neither
     */
    R getRight();

    /**
     * @return optional value of left
     */
    Optional<L> toOptionalLeft();

    /**
     * @return optional value of right
     */
    Optional<R> toOptionalRight();

    /**
     * @return true if this Either is left
     */
    boolean isLeft();

    /**
     * @return true if this Either is right
     */
    boolean isRight();

    /**
     * @return true if this Either is left or right
     */
    boolean isEither();

    /**
     * @return true if this Either is neither
     */
    boolean isNeither();

    /**
     * Invokes the consumer if this Either is left, otherwise does nothing
     * @throws NullPointerException if consumer is null
     */
    Either<L, R> ifLeft(Consumer<? super L> consumer);

    /**
     * Invokes the consumer if this Either is right, otherwise does nothing
     * @throws NullPointerException if consumer is null
     */
    Either<L, R> ifRight(Consumer<? super R> consumer);

    /**
     * Invokes an action if this Either is neither, otherwise does nothing
     * @throws NullPointerException if action is null
     */
    Either<L, R> ifNeither(Runnable action);

    /**
     * @return Either which only retains the left value if it passes the predicate
     * @throws NullPointerException if predicate is null
     */
    Either<L, R> filterLeft(Predicate<? super L> predicate);

    /**
     * @return Either which only retains the right value if it passes the predicate
     * @throws NullPointerException if predicate is null
     */
    Either<L, R> filterRight(Predicate<? super R> predicate);

    /**
     * @return Either which has its left value replaced by the result of mapper
     * @throws NullPointerException if mapper is null
     */
    <U1> Either<U1, R> mapLeft(Function<? super L, ? extends U1> mapper);

    /**
     * @return Either which has its right value replaced by the result of mapper
     * @throws NullPointerException if mapper is null
     */
    <U2> Either<L, U2> mapRight(Function<? super R, ? extends U2> mapper);

    /**
     * @return Optional which has the result of leftMapper if this Either is left, the result of rightMapper if this
     * Either is right and empty if this Either is neither
     * @throws NullPointerException if leftMapper or rightMapper is null
     */
    <U> Optional<U> collapse(Function<? super L, ? extends U> leftMapper,
                             Function<? super R, ? extends U> rightMapper);

    /**
     * @return Optional which has the result of mapper; missing values will be passed in as null
     * @throws NullPointerException if mapper is null
     */
    <U> Optional<U> collapse(BiFunction<? super L, ? super R, ? extends U> mapper);

    /**
     * @return similar to mapLeft(), except that the result is already an Either
     * @throws NullPointerException if mapper is null
     */
    <U> Either<U, R> flatMapLeft(Function<? super L, Either<U, R>> mapper);

    /**
     * @return similar to mapRight(), except that the result is already an Either
     * @throws NullPointerException if mapper is null
     */
    <U> Either<L, U> flatMapRight(Function<? super R, Either<L, U>> mapper);

    /**
     * @return Either which has the result of mapper; missing values will be passed in as null
     * @throws NullPointerException if mapper is null
     */
    <U1, U2> Either<U1, U2> flatMap(BiFunction<? super L, ? super R, Either<U1, U2>> mapper);

    /**
     * @return left value of this either; if it is null, other is returned
     */
    L leftOrElse(L other);

    /**
     * @return left value of this either; if it is null, supplier result is returned
     * @throws NullPointerException if supplier is null
     */
    L leftOrGet(Supplier<? extends L> supplier);

    /**
     * @return left value of this either
     * @throws X if this Either is not left
     * @throws NullPointerException if exceptionSupplier is null
     */
    <X extends Throwable> L leftOrThrow(Supplier<? extends X> exceptionSupplier) throws X;

    /**
     * @return right value of this either; if it is null, other is returned
     */
    R rightOrElse(R other);

    /**
     * @return right value of this either; if it is null, supplier result is returned
     * @throws NullPointerException if supplier is null
     */
    R rightOrGet(Supplier<? extends R> supplier);

    /**
     * @return right value of this either
     * @throws X if this Either is not right
     * @throws NullPointerException if exceptionSupplier is null
     */
    <X extends Throwable> R rightOrThrow(Supplier<? extends X> exceptionSupplier) throws X;

    /**
     * @return this Either
     * @throws X if this Either is left
     * @throws NullPointerException if exceptionSupplier is null
     */
    <X extends Throwable> Either<L, R> ifLeftThrow(Supplier<? extends X> exceptionSupplier) throws X;

    /**
     * @return this Either
     * @throws X if this Either is left
     * @throws NullPointerException if exceptionFunction is null
     */
    <X extends Throwable> Either<L, R> ifLeftThrowPass(Function<L, ? extends X> exceptionFunction) throws X;

    /**
     * @return this Either
     * @throws X if this Either is right
     * @throws NullPointerException if exceptionSupplier is null
     */
    <X extends Throwable> Either<L, R> ifRightThrow(Supplier<? extends X> exceptionSupplier) throws X;

    /**
     * @return this Either
     * @throws X if this Either is right
     * @throws NullPointerException if exceptionFunction is null
     */
    <X extends Throwable> Either<L, R> ifRightThrowPass(Function<R, ? extends X> exceptionFunction) throws X;

    /**
     * @return this Either
     * @throws X if this Either is neither
     * @throws NullPointerException if exceptionSupplier is null
     */
    <X extends Throwable> Either<L, R> ifNeitherThrow(Supplier<? extends X> exceptionSupplier) throws X;

    /**
     * @return Either with its types and value swapped around
     */
    Either<R, L> swap();

    // CONSTRUCTORS

    static <L, R> Either<L, R> neither() {
        @SuppressWarnings("unchecked")
        Either<L, R> empty = (Either<L, R>)NEITHER;
        return empty;
    }

    static <L, R> Either<L, R> left(L left) {
        return left == null ? neither() : new Left<>(left);
    }

    static <L, R> Either<L, R> left(Optional<L> left) {
        return left == null || !left.isPresent() ? neither() : new Left<>(left.get());
    }

    static <L, R> Either<L, R> right(R right) {
        return right == null ? neither() : new Right<>(right);
    }

    static <L, R> Either<L, R> right(Optional<R> right) {
        return right == null || !right.isPresent() ? neither() : new Right<>(right.get());
    }

    static <L, R> Either<L, R> of(L left, R right) {
        if (left != null && right != null)
            throw new IllegalArgumentException("At least one of arguments has to be null");

        return right == null ? left(left) : right(right);
    }

    static <L, R> Either<L, R> of(Optional<L> left, Optional<R> right) {
        if (left != null && right != null && left.isPresent() && right.isPresent())
            throw new IllegalArgumentException("At least one of arguments has to be null");

        return right == null || !right.isPresent() ? left(left) : right(right);
    }

    Either NEITHER = new Neither();

}
