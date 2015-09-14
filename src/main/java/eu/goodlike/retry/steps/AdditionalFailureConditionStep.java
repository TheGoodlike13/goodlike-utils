package eu.goodlike.retry.steps;

import java.util.function.Predicate;

/**
 * <pre>
 * RetryBuilder step
 *
 * Defines additional conditions, besides throwing an exception or returning null, which should be considered failures
 * </pre>
 */
public interface AdditionalFailureConditionStep<T> {

    /**
     * Makes the retries fail when the result fails the given predicate; null values will be considered failures
     * BEFORE these predicates are tested, thus they need not be considered
     * @throws NullPointerException if failCondition is null
     */
    FailureActionStep<T> failWhen(Predicate<T> failCondition);

}
