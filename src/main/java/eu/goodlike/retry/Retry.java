package eu.goodlike.retry;

import eu.goodlike.neat.Null;
import eu.goodlike.retry.steps.TimesStep;

import java.util.concurrent.Callable;

/**
 * <pre>
 * Allows customized retrying of a Callable (a Supplier which throws an exception)
 *
 * Supports:
 *      maximum attempts to retry
 *      timeouts:
 *          constant
 *          exponentially increasing, with a ceiling
 *      custom failure scenarios
 *      custom failure actions
 *      sync or async execution
 * </pre>
 */
public final class Retry {

    /**
     * @return a step builder for options for given Callable to retry
     * @throws NullPointerException if callableToRetry is null
     */
    public static <T> TimesStep<T> This(Callable<T> callableToRetry) {
        Null.check(callableToRetry).ifAny("Null callables not allowed");
        return new RetryBuilder<>(callableToRetry);
    }

    // PRIVATE

    private Retry() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
