package eu.goodlike.retry.steps;

import eu.goodlike.neat.Either;

import java.util.function.Consumer;

/**
 * <pre>
 * RetryBuilder step
 *
 * Defines actions to take when a retry fails
 * </pre>
 */
public interface AdditionalFailureActionStep<T> {

    /**
     * <pre>
     * Makes retry execute the consumer with resulting Either of T or Exception when the retry fails
     *
     * The Either will contain Exception if one was thrown, neither T nor Exception if retry returned null and
     * T value if it failed the predicate test defined in the previous step
     * </pre>
     * @throws NullPointerException if failAction is null
     */
    PerformStep<T> onFail(Consumer<Either<T, Exception>> failAction);

}
