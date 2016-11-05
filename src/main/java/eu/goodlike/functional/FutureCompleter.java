package eu.goodlike.functional;

import eu.goodlike.neat.Null;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <pre>
 * Ensures a CompletableFuture is completed after a certain time, interrupting it if it is not
 *
 * This class is useful in testing; if you know that a certain CompletableFuture must terminate relatively quickly (i.e.
 * sub second), but it can hang, this class will ensure the test fails rather than blocking the test thread. Also, you
 * will only need to wait extra time in the scenario where the CompletableFuture has actually blocked the thread, so
 * consider using a reasonably long duration (whatever you consider acceptable for a failing test to stall for)
 *
 * Example usage:
 *      // @Before or @BeforeClass
 *      FutureCompleter futureCompleter = new FutureCompleter(Duration.ofSeconds(1), Executors.newSingleThreadExecutor());
 *
 *      // @Test
 *      CompletableFuture<?> futureWhichCanHang = CompletableFuture.runAsync(() -> someMethodThatCanHang());
 *      futureCompleter.ensureCompletion(futureWhichCanHang, "This future has hung because reasons");
 *      futureWhichCanHang.join();  // Normally this could block the thread of the test, but now it will complete in 1s
 *      // If the future completes forcibly, CompletionException is thrown, otherwise code executes as normal
 *
 *      // @After or @AfterClass
 *      futureCompleter.close();
 * </pre>
 */
public final class FutureCompleter implements AutoCloseable {

    /**
     * <pre>
     * Ensures that future completes in the duration setup for this FutureCompleter, completing exceptionally with the
     * hangReason if it hangs.
     *
     * This method does not block the thread; instead, if you wish to confirm that the future has completed normally or
     * exceptionally, you will need to call {@link CompletableFuture#join()} or equivalent on the future
     * </pre>
     */
    public void ensureCompletion(CompletableFuture<?> future, String hangReason) {
        Null.check(future, hangReason).ifAny("Cannot be null: future, hangReason");

        executorService.submit(() -> {
            try {
                future.get(durationInMillis, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                future.completeExceptionally(new InterruptedException(hangReason));
            }
            return null;
        });
    }

    @Override
    public void close() throws Exception {
        executorService.shutdown();
    }

    // CONSTRUCTORS

    public FutureCompleter(Duration duration, ExecutorService executorService) {
        Null.check(duration, executorService).ifAny("Cannot be null: duration, executorService");
        if (duration.minus(MINIMUM_DURATION).isNegative())
            throw new IllegalArgumentException("Duration must be at least " + MINIMUM_DURATION + ", not: " + duration);

        this.durationInMillis = duration.toMillis();
        this.executorService = executorService;
    }

    // PRIVATE

    private final long durationInMillis;
    private final ExecutorService executorService;

    private static final Duration MINIMUM_DURATION = Duration.ofMillis(1);

}
