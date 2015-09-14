package eu.goodlike.retry.steps;

/**
 * <pre>
 * RetryBuilder step
 *
 * Defines how long should be the gap between retries
 *
 * You can skip this step and jump straight to FailureConditionStep; in that case, there will be no timeout
 * </pre>
 */
public interface TimeoutStep<T> extends FailureConditionStep<T> {

    /**
     * Initiates the timeout step; once this step is called, the interfaces will suppress other methods
     */
    TimeoutValueWithTypeStep<T> timeout();

    /**
     * Sets the timeout to 0
     */
    FailureConditionStep<T> noTimeout();

}
