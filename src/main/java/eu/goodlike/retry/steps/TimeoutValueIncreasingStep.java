package eu.goodlike.retry.steps;

/**
 * <pre>
 * RetryBuilder step
 *
 * Defines how long should be the gap between retries
 *
 * This step allows to specify the starting amount of time units for the timeout in the scenario where exponentially
 * increasing timeout was chosen
 * </pre>
 */
public interface TimeoutValueIncreasingStep<T> {

    /**
     * Sets the initial amount of time units for the timeout
     * @throws IllegalArgumentException if value is negative
     */
    TimeUnitWithMaxStep<T> from(long value);

}
