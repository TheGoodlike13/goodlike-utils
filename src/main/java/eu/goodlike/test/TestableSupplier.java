package eu.goodlike.test;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public final class TestableSupplier<T> implements Supplier<T> {

    @Override
    public T get() {
        timesQueried.incrementAndGet();
        return object;
    }

    public boolean hasBeenQueried() {
        return totalTimesQueried() > 0;
    }

    public long totalTimesQueried() {
        return timesQueried.get();
    }

    // CONSTRUCTORS

    public TestableSupplier(T object) {
        this(object, 0);
    }

    public TestableSupplier(T object, long timesAlreadyQueried) {
        this.object = object;
        this.timesQueried = new AtomicLong(timesAlreadyQueried);
    }

    // PRIVATE

    private final T object;
    private final AtomicLong timesQueried;

}
