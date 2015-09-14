package eu.goodlike.retry.steps;

/**
 * <pre>
 * RetryBuilder step
 *
 * Defines how long should be the gap between retries
 *
 * This step allows to specify the specific amount of time units for the timeout in the scenario where static
 * timeout was chosen
 * </pre>
 */
public interface TimeoutValueStep<T> {

    /**
     * Sets the amount of time units for the timeout
     * @throws IllegalArgumentException if value is negative
     */
    TimeUnitStep<T> For(long value);

}
