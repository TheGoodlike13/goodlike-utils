package eu.goodlike.v2.validate;

import eu.goodlike.functional.Action;
import eu.goodlike.neat.Null;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Actor which allows to specify what to do when an invalid value is passed to a validator
 * @param <T> type being validated
 */
public final class ValidationActor<T, E extends T, V extends Validate<T, V>> {

    /**
     * Executes an arbitrary action if and only if an invalid value is passed
     * @throws NullPointerException if someAction is null
     */
    public V thenDo(Action someAction) {
        return validator.ifInvalid(value, someAction);
    }

    /**
     * Consumes the value if and only if an invalid value is passed
     * @throws NullPointerException if valueConsumer is null
     */
    public V thenDo(Consumer<? super E> valueConsumer) {
        return validator.ifInvalid(value, valueConsumer);
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
    public <X extends Throwable> V thenThrow(Function<? super E, ? extends X> exceptionFunction) throws X {
        return validator.ifInvalidThrow(value, exceptionFunction);
    }

    // CONSTRUCTORS

    public static <T, E extends T, V extends Validate<T, V>> ValidationActor<T, E, V> of(E value, V validator) {
        Null.check(validator).ifAny("Validator cannot be null");
        return new ValidationActor<>(value, validator);
    }

    private ValidationActor(E value, V validator) {
        this.value = value;
        this.validator = validator;
    }

    // PRIVATE

    private final E value;
    private final V validator;

}
