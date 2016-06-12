package eu.goodlike.validate;

import eu.goodlike.neat.Null;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Actor which allows to specify what to do when an invalid value is passed to a validator
 */
public final class ValidatorActor<T, E extends T, V extends Validator<T, V>> {

    /**
     * Executes an arbitrary action if and only if an invalid value is passed
     * @throws NullPointerException if someAction is null
     */
    public V thenRun(Runnable someAction) {
        return validator.ifInvalidRun(value, someAction);
    }

    /**
     * Consumes the value if and only if an invalid value is passed
     * @throws NullPointerException if valueConsumer is null
     */
    public V thenAccept(Consumer<? super E> valueConsumer) {
        return validator.ifInvalidAccept(value, valueConsumer);
    }

    /**
     * Throws an arbitrary exception if and only if an invalid value is passed
     * @throws NullPointerException if exceptionSupplier is null
     */
    public <X extends Throwable> V thenThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return validator.ifInvalidThrow(value, exceptionSupplier);
    }

    /**
     * Throws an exception using the value if and only if an invalid value is passed
     * @throws NullPointerException if exceptionFunction is null
     */
    public <X extends Throwable> V thenThrowWith(Function<? super E, ? extends X> exceptionFunction) throws X {
        return validator.ifInvalidThrowWith(value, exceptionFunction);
    }

    // CONSTRUCTORS

    public static <T, E extends T, V extends Validator<T, V>> ValidatorActor<T, E, V> of(E value, V validator) {
        Null.check(validator).ifAny("Validator cannot be null");
        return new ValidatorActor<>(value, validator);
    }

    private ValidatorActor(E value, V validator) {
        this.value = value;
        this.validator = validator;
    }

    // PRIVATE

    private final E value;
    private final V validator;

}
