package eu.goodlike.retry.steps;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * RetryBuilder step
 *
 * Defines how long should be the gap between retries
 *
 * This step asks to specify which time unit is to be used for interpreting the value given in the previous step
 * </pre>
 */
public interface TimeUnitStep<T> {

    /**
     * Sets the type of units for previously specified value to TimeUnit.NANOSECONDS
     */
    FailureConditionStep<T> nanos();

    /**
     * Sets the type of units for previously specified value to TimeUnit.MICROSECONDS
     */
    FailureConditionStep<T> micros();

    /**
     * Sets the type of units for previously specified value to TimeUnit.MILLISECONDS
     */
    FailureConditionStep<T> millis();

    /**
     * Sets the type of units for previously specified value to TimeUnit.SECONDS
     */
    FailureConditionStep<T> seconds();

    /**
     * Sets the type of units for previously specified value to TimeUnit.MINUTES
     */
    FailureConditionStep<T> minutes();

    /**
     * Sets the type of units for previously specified value to TimeUnit.HOURS
     */
    FailureConditionStep<T> hours();

    /**
     * Sets the type of units for previously specified value to the given one; in general,
     * prefer using the above methods for readability
     * @throws NullPointerException if unit is null
     */
    FailureConditionStep<T> of(TimeUnit unit);

}
