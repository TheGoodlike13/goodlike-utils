package eu.goodlike.functional.impl.either;

import eu.goodlike.functional.Either;
import eu.goodlike.neat.Null;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

public final class Left<L, R> implements Either<L, R> {

    @Override
    public L getLeft() {
        return left;
    }

    @Override
    public R getRight() {
        throw new NoSuchElementException("No value present");
    }

    @Override
    public Optional<L> toOptionalLeft() {
        return Optional.of(left);
    }

    @Override
    public Optional<R> toOptionalRight() {
        return Optional.empty();
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public boolean isEither() {
        return true;
    }

    @Override
    public boolean isNeither() {
        return false;
    }

    @Override
    public Either<L, R> ifLeft(Consumer<? super L> consumer) {
        Null.check(consumer).as("consumer");
        consumer.accept(left);
        return this;
    }

    @Override
    public Either<L, R> ifRight(Consumer<? super R> consumer) {
        Null.check(consumer).as("consumer");
        return this;
    }

    @Override
    public Either<L, R> ifNeither(Runnable action) {
        Null.check(action).as("action");
        return this;
    }

    @Override
    public Either<L, R> filterLeft(Predicate<? super L> predicate) {
        Null.check(predicate).as("predicate");
        return predicate.test(left) ? this : Either.neither();
    }

    @Override
    public Either<L, R> filterRight(Predicate<? super R> predicate) {
        Null.check(predicate).as("predicate");
        return this;
    }

    @Override
    public <U1> Either<U1, R> mapLeft(Function<? super L, ? extends U1> mapper) {
        Null.check(mapper).as("mapper");
        return Either.left(mapper.apply(left));
    }

    @Override
    public <U2> Either<L, U2> mapRight(Function<? super R, ? extends U2> mapper) {
        Null.check(mapper).as("mapper");
        return self();
    }

    @Override
    public <U> Optional<U> collapse(Function<? super L, ? extends U> leftMapper, Function<? super R, ? extends U> rightMapper) {
        Null.check(leftMapper, rightMapper).as("leftMapper, rightMapper");
        return Optional.ofNullable(leftMapper.apply(left));
    }

    @Override
    public <U> Optional<U> collapse(BiFunction<? super L, ? super R, ? extends U> mapper) {
        Null.check(mapper).as("mapper");
        return Optional.ofNullable(mapper.apply(left, null));
    }

    @Override
    public <U> Either<U, R> flatMapLeft(Function<? super L, Either<U, R>> mapper) {
        Null.check(mapper).as("mapper");
        return mapper.apply(left);
    }

    @Override
    public <U> Either<L, U> flatMapRight(Function<? super R, Either<L, U>> mapper) {
        Null.check(mapper).as("mapper");
        return self();
    }

    @Override
    public <U1, U2> Either<U1, U2> flatMap(BiFunction<? super L, ? super R, Either<U1, U2>> mapper) {
        Null.check(mapper).as("mapper");
        return mapper.apply(left, null);
    }

    @Override
    public L leftOrElse(L other) {
        return left;
    }

    @Override
    public L leftOrGet(Supplier<? extends L> supplier) {
        Null.check(supplier).as("supplier");
        return left;
    }

    @Override
    public <X extends Throwable> L leftOrThrow(Supplier<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).as("exceptionSupplier");
        return left;
    }

    @Override
    public R rightOrElse(R other) {
        return other;
    }

    @Override
    public R rightOrGet(Supplier<? extends R> supplier) {
        Null.check(supplier).as("supplier");
        return supplier.get();
    }

    @Override
    public <X extends Throwable> R rightOrThrow(Supplier<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).as("exceptionSupplier");
        throw exceptionSupplier.get();
    }

    @Override
    public <X extends Throwable> Either<L, R> ifLeftThrow(Supplier<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).as("exceptionSupplier");
        throw exceptionSupplier.get();
    }

    @Override
    public <X extends Throwable> Either<L, R> ifLeftThrowPass(Function<L, ? extends X> exceptionFunction) throws X {
        Null.check(exceptionFunction).as("exceptionFunction");
        throw exceptionFunction.apply(left);
    }

    @Override
    public <X extends Throwable> Either<L, R> ifRightThrow(Supplier<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).as("exceptionSupplier");
        return this;
    }

    @Override
    public <X extends Throwable> Either<L, R> ifRightThrowPass(Function<R, ? extends X> exceptionFunction) throws X {
        Null.check(exceptionFunction).as("exceptionFunction");
        return this;
    }

    @Override
    public <X extends Throwable> Either<L, R> ifNeitherThrow(Supplier<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).as("exceptionSupplier");
        return this;
    }

    @Override
    public Either<R, L> swap() {
        return Either.right(left);
    }

    // CONSTRUCTORS

    public Left(L left) {
        Null.check(left).as("left");
        this.left = left;
    }

    // PRIVATE

    private final L left;

    private <U> Either<L, U> self() {
        @SuppressWarnings("unchecked")
        Either<L, U> self = (Either<L, U>) this;
        return self;
    }

    @Override
    public String toString() {
        return "Left[" + left + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Left)) return false;
        Left<?, ?> left1 = (Left<?, ?>) o;
        return Objects.equals(left, left1.left);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left);
    }

}
