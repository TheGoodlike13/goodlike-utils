package eu.goodlike.retry.steps;

/**
 * <pre>
 * RetryBuilder step
 *
 * Defines how many times should the task be retried
 *
 * You can skip this step and jump straight to TimeoutStep; in that case, the task will be retried indefinitely
 * </pre>
 */
public interface TimesStep<T> extends TimeoutStep<T> {

    /**
     * Sets the amount of times to repeat the task
     * @throws IllegalArgumentException if times is negative
     */
    TimeoutStep<T> maxTimes(long times);

    /**
     * Sets the amount of tries to repeat the task to Long.MAX_VALUE (2^63-1)
     */
    TimeoutStep<T> untilComplete();

}
