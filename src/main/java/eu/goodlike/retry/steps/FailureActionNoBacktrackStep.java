package eu.goodlike.retry.steps;

import eu.goodlike.functional.Action;
import eu.goodlike.functional.Either;

import java.util.function.Consumer;

/**
 * <pre>
 * RetryBuilder step
 *
 * Defines actions to take when a retry fails
 *
 * This step is used in the same way as FailureActionStep, except it does not allow to chain any more FailureConditions,
 * because a terminal condition was chosen (such as failErrorOnly())
 *
 * You can skip this step and jump straight to PerformNoBacktrackStep;
 * in that case, the task will do nothing special on failures
 *
 * AdditionalFailureActionStep is explicitly overridden so the IDEs would highlight its methods
 * </pre>
 */
public interface FailureActionNoBacktrackStep<T> extends AdditionalFailureActionStep<T>, PerformNoBacktrackStep<T> {

    /**
     * Makes the retries do nothing special when they fail
     */
    PerformNoBacktrackStep<T> ignoreFailures();

    @Override
    PerformStep<T> onFail(Consumer<Either<T, Exception>> failAction);
    @Override
    PerformStep<T> onError(Consumer<Exception> exceptionAction);
    @Override
    PerformStep<T> onNull(Action nullAction);
    @Override
    PerformStep<T> onInvalid(Consumer<T> invalidAction);

}
