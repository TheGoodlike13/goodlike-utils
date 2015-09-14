package eu.goodlike.retry.steps;

/**
 * <pre>
 * RetryBuilder step
 *
 * Defines how long should be the gap between retries
 *
 * This step allows to pick between an exponential increase and static amount of timeout
 *
 * TimeoutValueStep is explicitly overridden so the IDEs would highlight its methods
 * </pre>
 */
public interface TimeoutValueWithTypeStep<T> extends TimeoutValueStep<T> {

    /**
     * Initiates the exponentially increasing timeout step; once this step is called,
     * the interfaces will suppress other methods
     */
    TimeoutValueIncreasingStep<T> increasing();

    @Override
    TimeUnitStep<T> For(long value);

}
