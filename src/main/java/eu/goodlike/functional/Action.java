package eu.goodlike.functional;

import java.util.function.Function;

/**
 * Runnable replacement for not necessarily threaded context
 */
@FunctionalInterface
public interface Action {

    /**
     * Executes an arbitrary action
     */
    void doIt();

    /**
     * @return Action that wraps ThrowAction with a generic RuntimeException re-throw
     */
    static <X extends RuntimeException> Action of(ThrowAction action) {
        return of(action, RuntimeException::new);
    }

    /**
     * @return Action that wraps ThrowAction with a custom RuntimeException re-throw
     */
    static <X extends RuntimeException> Action of(ThrowAction action, Function<Throwable, X> exceptionSupplier) {
        return () -> {
            try {
                action.doIt();
            } catch (Throwable t) {
                throw exceptionSupplier.apply(t);
            }
        };
    }

}
