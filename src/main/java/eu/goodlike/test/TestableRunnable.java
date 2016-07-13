package eu.goodlike.test;

import java.util.concurrent.atomic.AtomicInteger;

public final class TestableRunnable implements Runnable {

    @Override
    public void run() {
        timesRun.incrementAndGet();
    }

    public boolean hasBeenRun() {
        return totalTimesRun() > 0;
    }

    public int totalTimesRun() {
        return timesRun.get();
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
