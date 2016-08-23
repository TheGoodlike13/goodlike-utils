package eu.goodlike.test;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <pre>
 * Runnable which counts how many times it has been run
 *
 * You should verify that all threads that need to access this runnable have finished before asserting using its methods,
 * otherwise the results will not be consistent
 * </pre>
 */
public final class TestableRunnable implements Runnable {

    /**
     * @return true if this Runnable has been run at least once, false otherwise
     */
    public boolean hasBeenRun() {
        return totalTimesRun() > 0;
    }

    /**
     * @return amount of times this Runnable has been run
     */
    public long totalTimesRun() {
        return timesRun.get();
    }

    @Override
    public void run() {
        timesRun.incrementAndGet();
    }

    // CONSTRUCTORS

    public TestableRunnable() {
        this(0);
    }

    public TestableRunnable(long timesAlreadyRun) {
        this.timesRun = new AtomicLong(timesAlreadyRun);
    }

    // PRIVATE

    private final AtomicLong timesRun;

}
