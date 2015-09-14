package eu.goodlike.retry;

import eu.goodlike.neat.Either;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Result of RetryBuilder
 */
public final class SimpleRetry<T> {

    /**
     * @return Optional of the result of given callable after it has succeeded (for given parameters), Optional.empty
     * if it has failed
     */
    public Optional<T> getRetryResult() {
        long attemptsSoFar = 0;
        long timeoutNextAttempt = timeoutInMillis;

        while (attemptsSoFar < timesToRetry) {
            if (attemptsSoFar > 0)
                timeoutNextAttempt = timeout(timeoutNextAttempt);

            Either<T, Exception> either = attempt();
            if (either.isFirstKind()) {
                Optional<T> result = either.filterFirstKind(this::passesFailureTest).getFirstOptional();
                if (result.isPresent())
                    return result;
            }
            executeFailureAction(either);
            attemptsSoFar++;
        }
        return Optional.empty();
    }

    // CONSTRUCTORS

    public SimpleRetry(Callable<T> callableToRetry, long timesToRetry, long timeoutInMillis, long maxTimeoutInMillis,
                       boolean isTimeoutIncreasing, Predicate<T> failureCondition,
                       Consumer<Either<T, Exception>> failureAction) {
        this.callableToRetry = callableToRetry;
        this.timesToRetry = timesToRetry;
        this.timeoutInMillis = timeoutInMillis;
        this.maxTimeoutInMillis = maxTimeoutInMillis;
        this.isTimeoutIncreasing = isTimeoutIncreasing;
        this.failureCondition = failureCondition;
        this.failureAction = failureAction;
    }

    // PRIVATE

    private final Callable<T> callableToRetry;
    private final long timesToRetry;
    private final long timeoutInMillis;
    private final long maxTimeoutInMillis;
    private final boolean isTimeoutIncreasing;
    private final Predicate<T> failureCondition;
    private final Consumer<Either<T, Exception>> failureAction;

    private Either<T, Exception> attempt() {
        T result;
        try {
            result = callableToRetry.call();
        } catch (Exception e) {
            return Either.of(null, e);
        }
        return Either.of(result, null);
    }

    private void executeFailureAction(Either<T, Exception> either) {
        if (failureAction != null)
            failureAction.accept(either);
    }

    private boolean passesFailureTest(T result) {
        return failureCondition == null || !failureCondition.test(result);
    }

    private long timeout(long timeoutMillis) {
        try {
            Thread.sleep(timeoutMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isTimeoutIncreasing
                ? timeoutInMillis >= maxTimeoutInMillis ? maxTimeoutInMillis : nextTimeoutValue(timeoutMillis)
                : timeoutMillis;
    }

    private long nextTimeoutValue(long timeoutMillis) {
        long next = timeoutMillis << 1;
        return next >= maxTimeoutInMillis ? maxTimeoutInMillis : next;
    }

}
