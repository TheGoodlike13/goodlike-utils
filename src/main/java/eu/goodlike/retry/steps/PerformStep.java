package eu.goodlike.retry.steps;

import java.util.Optional;
import java.util.concurrent.Future;

/**
 * <pre>
 * RetryBuilder step
 *
 * Defines how to execute the retries - on the same thread or asynchronously
 *
 * PerformNoBacktrackStep is explicitly overridden so the IDEs would highlight its methods
 * </pre>
 */
public interface PerformStep<T> extends PerformNoBacktrackStep<T> {

    /**
     * Backtrack for FailureActionStep, which allows to chain multiple actions to avoid excessively long lambdas;
     * the actions are executed one after another in the order of definition
     */
    AdditionalFailureActionStep<T> and();

    @Override
    Optional<T> doSync();
    @Override
    Future<Optional<T>> doAsync();

}
