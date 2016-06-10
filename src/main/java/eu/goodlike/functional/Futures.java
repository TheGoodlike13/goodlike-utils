package eu.goodlike.functional;

import eu.goodlike.neat.Null;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Utility methods to work with CompletableFutures
 */
public final class Futures {

    /**
     * <pre>
     * Suppose that you need to execute some kind of computation in a particular Thread, but the context of this Thread
     * would only accept Runnables, and you required it to return a value - this method does exactly that
     *
     * Inspired by Android's runOnUiThread(Runnable)
     * </pre>
     * @return value of a supplier, executed in given context, wrapped in a CompletableFuture
     */
    public static <T> CompletableFuture<T> valueFromSupplierAsRunnable(Consumer<Runnable> context, Supplier<T> supplier) {
        Null.check(context, supplier).ifAny("Context and supplier cannot be null");

        CompletableFuture<T> future = new CompletableFuture<>();
        context.accept(() -> future.complete(supplier.get()));
        return future;
    }

    // PRIVATE

    private Futures() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
