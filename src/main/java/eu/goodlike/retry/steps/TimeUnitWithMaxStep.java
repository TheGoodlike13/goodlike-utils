package eu.goodlike.retry.steps;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * RetryBuilder step
 *
 * Defines how long should be the gap between retries
 *
 * This step allows to optionally specify a ceiling for exponentially increasing timeout
 *
 * TimeUnitStep is explicitly overridden so the IDEs would highlight its methods
 * </pre>
 */
public interface TimeUnitWithMaxStep<T> extends TimeUnitStep<T> {

    /**
     * Sets the maximum amount of time units for the timeout
     * @throws IllegalArgumentException if value is less than the initial value set in the previous step
     */
    TimeUnitStep<T> upTo(long value);

    /**
     * Sets the maximum amount of time units for the timeout to Long.MAX_VALUE (2^63-1)
     */
    TimeUnitStep<T> unbounded();

    @Override
    FailureConditionStep<T> nanos();
    @Override
    FailureConditionStep<T> micros();
    @Override
    FailureConditionStep<T> millis();
    @Override
    FailureConditionStep<T> seconds();
    @Override
    FailureConditionStep<T> minutes();
    @Override
    FailureConditionStep<T> hours();
    @Override
    FailureConditionStep<T> of(TimeUnit unit);

}
