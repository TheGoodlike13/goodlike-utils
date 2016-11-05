package eu.goodlike.misc;

import eu.goodlike.functional.FutureCompleter;
import eu.goodlike.test.TestableRunnable;
import org.junit.After;
import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class SingleTimeExecutorTest {

    private FutureCompleter futureCompleter;
    private final SingleTimeExecutor singleTimeExecutor = new SingleTimeExecutor();

    @After
    public void tearDown() throws Exception {
        if (futureCompleter != null)
            futureCompleter.close();
    }

    private void setUpFutureCompleter(Duration duration) {
        futureCompleter = new FutureCompleter(duration, Executors.newSingleThreadExecutor());
    }

    @Test
    public void testIfRunnableExecutes() throws InterruptedException {
        setUpFutureCompleter(Duration.ofSeconds(1));

        TestableRunnable testableRunnable = new TestableRunnable();

        CompletableFuture<Void> testFuture = CompletableFuture.runAsync(testableRunnable, singleTimeExecutor);
        futureCompleter.ensureCompletion(testFuture, "One second should have been enough for this...");
        testFuture.join();

        assertThat(testableRunnable.hasBeenRun())
                .isTrue();
    }

    @Test
    public void testIfSecondRunnableNeverExecutes() {
        setUpFutureCompleter(Duration.ofMillis(100));

        TestableRunnable testableRunnable = new TestableRunnable();

        CompletableFuture<Void> testFuture1 = CompletableFuture.runAsync(testableRunnable, singleTimeExecutor);
        futureCompleter.ensureCompletion(testFuture1, "This one should complete without any issues");
        testFuture1.join();

        CompletableFuture<Void> testFuture2 = CompletableFuture.runAsync(testableRunnable, singleTimeExecutor);
        futureCompleter.ensureCompletion(testFuture2, "This one should hang");

        assertThatExceptionOfType(CompletionException.class)
                .isThrownBy(testFuture2::join);

        assertThat(testableRunnable.totalTimesRun())
                .isEqualTo(1);
    }

}
