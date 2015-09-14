package eu.goodlike.retry.steps;

import java.util.function.Predicate;

/**
 * <pre>
 * RetryBuilder step
 *
 * Defines additional conditions, besides throwing an exception or returning null, which should be considered failures
 *
 * You can skip this step and jump straight to FailureActionNoBacktrackStep;
 * in that case, the task will be fail only when an Exception is thrown or null is returned
 *
 * AdditionalFailureConditionStep is explicitly overridden so the IDEs would highlight its methods
 * </pre>
 */
public interface FailureConditionStep<T> extends AdditionalFailureConditionStep<T>, FailureActionNoBacktrackStep<T> {

    /**
     * Makes the retries fail only when an exception is thrown or null is returned
     */
    FailureActionNoBacktrackStep<T> failErrorOnly();

    @Override
    FailureActionStep<T> failWhen(Predicate<T> failCondition);

}
