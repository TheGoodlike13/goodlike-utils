package eu.goodlike.misc;

import eu.goodlike.neat.Null;

import java.util.function.Supplier;

/**
 * Class which wraps singleton logic; it ensures that given value or supplier will be lazily loaded (when possible)
 * and only return the same one instance of it; if value, supplier or the value from supplier returns null, a
 * NullPointerException is thrown
 */
public final class Singleton<T> implements Supplier<T> {

    @Override
    public T get() {
        ensureValueExists();
        return value;
    }

    // CONSTRUCTORS

    public static <T> Singleton<T> of(T value) {
        Null.check(value).ifAny("Value cannot be null");
        return new Singleton<>(value, null);
    }

    public static <T> Singleton<T> lazy(Supplier<T> supplier) {
        Null.check(supplier).ifAny("Supplier cannot be null");
        return new Singleton<>(null, supplier);
    }

    private Singleton(T value, Supplier<T> supplier) {
        this.value = value;
        this.supplier = supplier;
    }

    // PRIVATE

    private volatile T value;
    private final Supplier<T> supplier;

    private void ensureValueExists() {
        if (value == null) {
            synchronized (this) {
                if (value == null) {
                    value = supplier.get();
                    Null.check(value).ifAny("Value from supplier cannot be null");
                }
            }
        }
    }

}
