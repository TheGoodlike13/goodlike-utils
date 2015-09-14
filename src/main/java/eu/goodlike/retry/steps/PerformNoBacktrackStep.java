package eu.goodlike.retry.steps;

import java.util.Optional;
import java.util.concurrent.Future;

/**
 * <pre>
 * RetryBuilder step
 *
 * Defines how to execute the retries - on the same thread or asynchronously
 * </pre>
 */
public interface PerformNoBacktrackStep<T> {

    /**
     * @return Optional of result of retries, for given options; Optional.empty() if all the retries failed
     */
    Optional<T> doSync();

    /**
     * @return Future wrapper of doSync() method
     */
    Future<Optional<T>> doAsync();

}
