package eu.goodlike.misc;

import com.google.common.collect.ImmutableList;
import eu.goodlike.functional.Futures;
import eu.goodlike.functional.ImmutableCollectors;
import eu.goodlike.functional.Optionals;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread executor which only executes one Runnable or Callable. All further attempts to use Runnable or Callable on
 * this executor will be ignored. When attempting to execute multiple Runnables or Callables is done, first one, as
 * returned by Collections::stream and Stream::findFirst will be executed.
 */
public final class SingleTimeExecutor implements ExecutorService {

    @Override
    public void shutdown() {
        singleExecutorService.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return singleExecutorService.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return singleExecutorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return singleExecutorService.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return singleExecutorService.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return triggerFirstExecution()
                ? singleExecutorService.submit(task)
                : getFailedFuture();
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return triggerFirstExecution()
                ? singleExecutorService.submit(task, result)
                : getFailedFuture();
    }

    @Override
    public Future<?> submit(Runnable task) {
        return triggerFirstExecution()
                ? singleExecutorService.submit(task)
                : getFailedFuture();
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        List<Future<T>> executedFutures = triggerFirstExecution()
                ? singleExecutorService.invokeAll(getFirstAsList(tasks))
                : Collections.emptyList();

        return padWithFailedFutures(executedFutures, tasks.size());
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        List<Future<T>> executedFutures = triggerFirstExecution()
                ? singleExecutorService.invokeAll(getFirstAsList(tasks), timeout, unit)
                : Collections.emptyList();

        return padWithFailedFutures(executedFutures, tasks.size());
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return triggerFirstExecution()
                ? singleExecutorService.invokeAny(getFirstAsList(tasks))
                : SingleTimeExecutor.<T>getFailedFuture().join();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return triggerFirstExecution()
                ? singleExecutorService.invokeAny(getFirstAsList(tasks), timeout, unit)
                : SingleTimeExecutor.<T>getFailedFuture().join();
    }

    @Override
    public void execute(Runnable command) {
        if (triggerFirstExecution())
            singleExecutorService.execute(command);
    }

    // CONSTRUCTORS

    public SingleTimeExecutor() {
        this(Executors.newSingleThreadExecutor());
    }

    public SingleTimeExecutor(ThreadFactory threadFactory) {
        this(Executors.newSingleThreadExecutor(threadFactory));
    }

    private SingleTimeExecutor(ExecutorService executorService) {
        this.singleExecutorService = executorService;
        this.hasBeenExecuted = new AtomicBoolean(false);
    }

    // PRIVATE

    private final ExecutorService singleExecutorService;
    private final AtomicBoolean hasBeenExecuted;

    /**
     * Atomically checks if this executor has already been given a Runnable or Callable to execute. It is assumed that
     * the first Runnable or Callable will be sent immediately following the execution of this method, and all other
     * attempts to execute this method will return false
     *
     * @return true if this is the first time the method is called, false otherwise
     */
    private boolean triggerFirstExecution() {
        return !hasBeenExecuted.getAndSet(true);
    }

    private static final CompletableFuture<?> FAILED_FUTURE = Futures.failedFuture(
            new IllegalStateException("Cannot execute more that one Runnable or Callable with this executor."));

    private static <T> CompletableFuture<T> getFailedFuture() {
        @SuppressWarnings("unchecked")
        CompletableFuture<T> failedFuture = (CompletableFuture<T>) FAILED_FUTURE;
        return failedFuture;
    }

    private static <T> List<Future<T>> padWithFailedFutures(List<Future<T>> futureResult, int expectedSize) {
        ImmutableList.Builder<Future<T>> builder = ImmutableList.builder();
        builder.addAll(futureResult);
        for (int i = expectedSize; i > futureResult.size(); i--)
            builder.add(getFailedFuture());

        return builder.build();
    }

    private static <T> List<T> getFirstAsList(Collection<T> anyCollection) {
        return Optionals.asStream(anyCollection.stream().findFirst())
                .collect(ImmutableCollectors.toList());
    }

}
