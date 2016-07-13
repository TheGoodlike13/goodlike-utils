package eu.goodlike.test;

import java.util.concurrent.atomic.AtomicInteger;

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
    public int totalTimesRun() {
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

    public TestableRunnable(int timesAlreadyRun) {
        this.timesRun = new AtomicInteger(timesAlreadyRun);
    }

    // PRIVATE

    private final AtomicInteger timesRun;

}
