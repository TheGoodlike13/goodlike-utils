package eu.goodlike.retry.steps;

import eu.goodlike.neat.Either;

import java.util.function.Consumer;

/**
 * <pre>
 * RetryBuilder step
 *
 * Defines actions to take when a retry fails
 *
 * You can skip this step and jump straight to PerformStep; in that case, the task will do nothing special on failures
 *
 * FailureActionNoBacktrackStep is explicitly overridden so the IDEs would highlight its methods
 * </pre>
 */
public interface FailureActionStep<T> extends FailureActionNoBacktrackStep<T>, PerformNoBacktrackStep<T>  {

    /**
     * Backtrack for FailureConditionStep, which allows to chain multiple conditions to avoid excessively long lambdas;
     * only || operator is supported
     */
    AdditionalFailureConditionStep<T> or();

    @Override
    PerformNoBacktrackStep<T> ignoreFailures();
    @Override
    PerformStep<T> onFail(Consumer<Either<T, Exception>> failAction);

}
