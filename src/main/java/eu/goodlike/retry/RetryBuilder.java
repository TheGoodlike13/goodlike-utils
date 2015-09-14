package eu.goodlike.retry;

import eu.goodlike.neat.Either;
import eu.goodlike.neat.Null;
import eu.goodlike.retry.steps.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <pre>
 * Builder class for options for given Callable to retry
 *
 * The step builder guides you along through the steps to set up complete options for the retrying process;
 * however, it is set up so you can use it partially as a normal builder; in general:
 *      expected interface methods should be highlighted by your IDE (tested on default intellij idea)
 *      next interface methods should be greyed out by your IDE (tested on default intellij idea)
 * This allows avoiding using chained methods for default values if you prefer brevity over explicitness
 *
 * DO NOT use this class directly; its methods return various interfaces, sometimes inconsistent with the ones
 * returned by the interfaces themselves
 * </pre>
 * @param <T> type parameter of given Callable
 */
public final class RetryBuilder<T> implements TimesStep<T>, TimeoutStep<T>, TimeoutValueStep<T>, TimeUnitStep<T>,
        TimeoutValueWithTypeStep<T>, TimeoutValueIncreasingStep<T>, TimeUnitWithMaxStep<T>, FailureConditionStep<T>,
        AdditionalFailureConditionStep<T>, FailureActionStep<T>, FailureActionNoBacktrackStep<T>,
        AdditionalFailureActionStep<T>, PerformStep<T>, PerformNoBacktrackStep<T> {

    @Override
    public TimeoutStep<T> maxTimes(long times) {
        if (times < 0)
            throw new IllegalArgumentException("Cannot retry a negative number of times: " + times);

        this.timesToRetry = times;
        return this;
    }

    @Override
    public TimeoutStep<T> untilComplete() {
        return this;    // use default value, MAX_LONG; makes loop terminate after 2^63-1 times
    }

    @Override
    public TimeoutValueWithTypeStep<T> timeout() {
        return this;    // step builder method, does nothing
    }

    @Override
    public FailureConditionStep<T> noTimeout() {
        return this;    // use defaults (0 millis, 0 nanos)
    }

    @Override
    public TimeoutValueIncreasingStep<T> increasing() {
        this.isTimeoutIncreasing = true;
        return this;
    }

    @Override
    public RetryBuilder<T> For(long value) {
        if (value < 0)
            throw new IllegalArgumentException("Cannot timeout a negative amount: " + value);

        this.timeoutValue = value;
        return this;
    }

    @Override
    public TimeUnitWithMaxStep<T> from(long value) {
        return For(value);  // synonym of For(), used when timeout is increasing
    }

    @Override
    public TimeUnitStep<T> upTo(long value) {
        if (value < this.timeoutValue)
            throw new IllegalArgumentException("Max timeout should be more or equal starting timeout, not: "
                    + value + " < " + this.timeoutValue);

        this.maxTimeout = value;
        return this;
    }

    @Override
    public TimeUnitStep<T> unbounded() {
        return this;    // use default value, MAX_LONG; makes timeout increase until attempts run out
    }

    @Override
    public FailureConditionStep<T> nanos() {
        this.timeoutUnit = TimeUnit.NANOSECONDS;
        return this;
    }

    @Override
    public FailureConditionStep<T> micros() {
        this.timeoutUnit = TimeUnit.MICROSECONDS;
        return this;
    }

    @Override
    public FailureConditionStep<T> millis() {
        return this;    // use default (TimeUnit.MILLISECONDS)
    }

    @Override
    public FailureConditionStep<T> seconds() {
        this.timeoutUnit = TimeUnit.SECONDS;
        return this;
    }

    @Override
    public FailureConditionStep<T> minutes() {
        this.timeoutUnit = TimeUnit.MINUTES;
        return this;
    }

    @Override
    public FailureConditionStep<T> hours() {
        this.timeoutUnit = TimeUnit.HOURS;
        return this;
    }

    @Override
    public FailureConditionStep<T> of(TimeUnit unit) {
        Null.check(unit).ifAny("Null TimeUnits not allowed");
        this.timeoutUnit = unit;
        return this;
    }

    @Override
    public FailureActionStep<T> failErrorOnly() {
        return this;    // use defaults (null condition = no condition)
    }

    @Override
    public FailureActionStep<T> failWhen(Predicate<T> failCondition) {
        Null.check(failCondition).ifAny("Null Predicates not allowed");
        this.failureCondition = this.failureCondition == null
                ? failCondition
                : this.failureCondition.or(failCondition);
        return this;
    }

    @Override
    public AdditionalFailureConditionStep<T> or() {
        return this;    // step builder method, does nothing
    }

    @Override
    public PerformStep<T> ignoreFailures() {
        return this;    // use defaults (null consumer = no consumer)
    }

    @Override
    public PerformStep<T> onFail(Consumer<Either<T, Exception>> failAction) {
        Null.check(failAction).ifAny("Null Consumers not allowed");
        this.failureAction = this.failureAction == null
                ? failAction
                : this.failureAction.andThen(failAction);
        return this;
    }

    @Override
    public AdditionalFailureActionStep<T> and() {
        return this;    // step builder method, does nothing
    }

    @Override
    public Optional<T> doSync() {
        return new SimpleRetry<>(callableToRetry, timesToRetry, timeoutUnit.toMillis(timeoutValue),
                timeoutUnit.toMillis(maxTimeout), isTimeoutIncreasing, failureCondition, failureAction)
                .getRetryResult();
    }

    @Override
    public Future<Optional<T>> doAsync() {
        return Executors.newSingleThreadExecutor().submit(this::doSync);
    }

    // CONSTRUCTORS

    public RetryBuilder(Callable<T> callableToRetry) {
        this.callableToRetry = callableToRetry;
    }

    // PRIVATE

    private final Callable<T> callableToRetry;

    private long timesToRetry = Long.MAX_VALUE;
    private long timeoutValue = 0;
    private long maxTimeout = Long.MAX_VALUE;
    private TimeUnit timeoutUnit = TimeUnit.MILLISECONDS;
    private boolean isTimeoutIncreasing = false;
    private Predicate<T> failureCondition = null;
    private Consumer<Either<T, Exception>> failureAction = null;

}
