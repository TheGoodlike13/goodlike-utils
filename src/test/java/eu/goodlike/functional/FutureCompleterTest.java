package eu.goodlike.functional;

import org.junit.After;
import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class FutureCompleterTest {

    private FutureCompleter futureCompleter;

    @After
    public void tearDown() throws Exception {
        if (futureCompleter != null)
            futureCompleter.close();
    }

    private void setUpFutureCompleter(Duration duration) {
        futureCompleter = new FutureCompleter(duration, Executors.newSingleThreadExecutor());
    }

    @Test
    public void hangingFutureIsCompleted() throws Exception {
        setUpFutureCompleter(Duration.ofMillis(1));

        CompletableFuture<?> hanging = new CompletableFuture<>();
        futureCompleter.ensureCompletion(hanging, "It is impossible for the future to complete by itself");

        assertThatExceptionOfType(CompletionException.class)
                .isThrownBy(hanging::join);
    }

    @Test
    public void completedFutureCompletesNormally() throws Exception {
        setUpFutureCompleter(Duration.ofSeconds(1));

        CompletableFuture<?> hanging = new CompletableFuture<>();
        futureCompleter.ensureCompletion(hanging, "One second should have been enough for this...");
        hanging.complete(null);

        assertThat(hanging.join())
                .isNull();
    }

}
