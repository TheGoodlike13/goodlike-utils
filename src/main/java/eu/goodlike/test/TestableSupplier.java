package eu.goodlike.test;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public final class TestableSupplier<T> implements Supplier<T> {

    @Override
    public T get() {
        timesQueried.incrementAndGet();
        return supplier.get();
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
        this(() -> object, timesAlreadyQueried);
    }

    public TestableSupplier(Supplier<T> supplier) {
        this(supplier, 0);
    }

    public TestableSupplier(Supplier<T> supplier, long timesAlreadyQueried) {
        this.supplier = supplier;
        this.timesQueried = new AtomicLong(timesAlreadyQueried);
    }



    // PRIVATE

    private final Supplier<T> supplier;
    private final AtomicLong timesQueried;

}
