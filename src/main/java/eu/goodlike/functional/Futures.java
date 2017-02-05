package eu.goodlike.functional;

import eu.goodlike.neat.Null;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static eu.goodlike.functional.ImmutableCollectors.toList;

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
     * @throws NullPointerException if context or supplier are null
     */
    public static <T> CompletableFuture<T> valueFromSupplierAsRunnable(Consumer<Runnable> context, Supplier<T> supplier) {
        Null.check(context, supplier).ifAny("Context and supplier cannot be null");

        CompletableFuture<T> future = new CompletableFuture<>();
        context.accept(() -> future.complete(supplier.get()));
        return future;
    }

    /**
     * @return CompletableFuture which has failed using given exception
     * @throws NullPointerException if throwable is null
     */
    public static <T> CompletableFuture<T> failedFuture(Throwable throwable) {
        Null.check(throwable).ifAny("Throwable cannot be null");

        CompletableFuture<T> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(throwable);
        return failedFuture;
    }

    /**
     * @return BiConsumer, which handles the CompletableFuture::whenComplete method; if an error is present (not null)
     * the errorConsumer is called, otherwise the resultConsumer is called
     * @throws NullPointerException if resultConsumer or errorConsumer are null
     */
    public static <Result> BiConsumer<Result, Throwable> completionHandler(Consumer<Result> resultConsumer,
                                                                              Consumer<Throwable> errorConsumer) {
        Null.check(resultConsumer, errorConsumer).ifAny("Result consumer and error consumer cannot be null");

        return (result, error) ->  {
            if (error == null)
                resultConsumer.accept(result);
            else
                errorConsumer.accept(error);
        };
    }

    /**
     * @return CompletableFuture, completed with value from the optional, or Throwable if optional is empty
     * @throws NullPointerException if optional or onEmpty is null
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> CompletableFuture<T> fromOptional(Optional<T> optional, Supplier<Throwable> onEmpty) {
        Null.check(optional, onEmpty).ifAny("Optional and supplier cannot be null");

        return optional.isPresent()
                ? CompletableFuture.completedFuture(optional.get())
                : Futures.failedFuture(onEmpty.get());
    }

    /**
     * @return {@link CompletableFuture} which completes with values of given futures when they all complete
     * @throws NullPointerException if futures is or contains null
     */
    @SafeVarargs
    public static <T> CompletableFuture<List<T>> allOf(CompletableFuture<T>... futures) {
        Null.checkArray(futures).as("futures");

        return CompletableFuture.allOf(futures)
                .thenApply(all -> Arrays.stream(futures).map(CompletableFuture::join).collect(toList()));
    }

    /**
     * @return {@link CompletableFuture} which completes with the value of the first completed future
     * @throws NullPointerException if futures is or contains null
     */
    @SafeVarargs
    public static <T> CompletableFuture<T> anyOf(CompletableFuture<T>... futures) {
        Null.checkArray(futures).as("futures");

        @SuppressWarnings("unchecked")
        CompletableFuture<T> future = (CompletableFuture<T>) CompletableFuture.anyOf(futures);
        return future;
    }

    // PRIVATE

    private Futures() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
