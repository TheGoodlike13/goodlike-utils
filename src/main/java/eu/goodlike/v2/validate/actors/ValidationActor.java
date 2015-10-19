package eu.goodlike.v2.validate.actors;

import eu.goodlike.functional.Action;
import eu.goodlike.neat.Null;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Actor which allows to specify what to do when an invalid value is passed to a validator
 * @param <T> type being validated
 */
public abstract class ValidationActor<T> {

    /**
     * Executes an arbitrary action if and only if an invalid value is passed
     * @throws NullPointerException if someAction is null
     */
    public abstract void thenDo(Action someAction);

    /**
     * Consumes the value if and only if an invalid value is passed
     * @throws NullPointerException if valueConsumer is null
     */
    public abstract void thenDo(Consumer<T> valueConsumer);

    /**
     * Throws an arbitrary exception if and only if an invalid value is passed
     * @throws NullPointerException if exceptionSupplier is null
     */
    public abstract <X extends Throwable> void thenThrow(Supplier<X> exceptionSupplier) throws X;

    /**
     * Throws an exception using the value if and only if an invalid value is passed
     * @throws NullPointerException if exceptionFunction is null
     */
    public abstract <X extends Throwable> void thenThrow(Function<T, X> exceptionFunction) throws X;

    // CONSTRUCTORS

    /**
     * Evaluates the predicate using given value and returns an appropriate validator actor
     * @throws NullPointerException if validationPredicate is null
     */
    public static <T> ValidationActor<T> of(Predicate<T> validationPredicate, T value) {
        Null.check(validationPredicate).ifAny("Validation predicate cannot be null");
        return validationPredicate.test(value) ? new ValidActor<>() : new InvalidActor<>(value);
    }

    protected ValidationActor(T value) {
        this.value = value;
    }

    // PROTECTED

    protected final T value;

    /**
     * Validator for invalid values; always executes methods
     * @param <T> type being validated
     */
    private static final class InvalidActor<T> extends ValidationActor<T> {
        @Override
        public void thenDo(Action someAction) {
            Null.check(someAction).ifAny("Action cannot be null");
            someAction.doIt();
        }

        @Override
        public void thenDo(Consumer<T> valueConsumer) {
            Null.check(valueConsumer).ifAny("Consumer cannot be null");
            valueConsumer.accept(value);
        }

        @Override
        public <X extends Throwable> void thenThrow(Supplier<X> exceptionSupplier) throws X {
            Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");
            throw exceptionSupplier.get();
        }

        @Override
        public <X extends Throwable> void thenThrow(Function<T, X> exceptionFunction) throws X {
            Null.check(exceptionFunction).ifAny("Exception function cannot be null");
            throw exceptionFunction.apply(value);
        }

        // CONSTRUCTORS

        private InvalidActor(T value) {
            super(value);
        }
    }

    /**
     * Validator for valid values; never executes methods
     * @param <T> type being validated
     */
    private static final class ValidActor<T> extends ValidationActor<T> {
        @Override
        public void thenDo(Action someAction) {
            Null.check(someAction).ifAny("Action cannot be null");
        }

        @Override
        public void thenDo(Consumer<T> valueConsumer) {
            Null.check(valueConsumer).ifAny("Consumer cannot be null");
        }

        @Override
        public <X extends Throwable> void thenThrow(Supplier<X> exceptionSupplier) throws X {
            Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");
        }

        @Override
        public <X extends Throwable> void thenThrow(Function<T, X> exceptionFunction) throws X {
            Null.check(exceptionFunction).ifAny("Exception function cannot be null");
        }

        // CONSTRUCTORS

        private ValidActor() {
            super(null);
        }
    }

}
