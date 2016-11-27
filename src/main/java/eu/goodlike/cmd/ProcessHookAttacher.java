package eu.goodlike.cmd;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import eu.goodlike.neat.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * Handles attachment of custom behaviour during and after {@link Process} execution
 */
public final class ProcessHookAttacher implements AutoCloseable {

    /**
     * Attaches a {@link DuringProcessHook} to a {@link Process}
     *
     * @param process process for which to execute custom behaviour during and after execution
     * @param processHook custom behaviour to execute
     * @return given process
     */
    public Process attachDuring(Process process, DuringProcessHook processHook) {
        executionService.submit(() -> processHook.doDuringProcess(process));

        return process;
    }

    /**
     * Attaches a {@link AfterProcessHook} to a {@link Process}
     *
     * @param process process for which to execute custom behaviour during and after execution
     * @param processHook custom behaviour to execute
     * @return given process
     */
    public Process attachAfter(Process process, AfterProcessHook processHook) {
        processFinishCache.get(process, this::hookWaitingForFinish)
                .thenAcceptAsync(any -> processHook.doAfterProcess(process), executionService);

        return process;
    }

    @Override
    public void close() throws Exception {
        executionService.shutdown();
        processFinishService.shutdown();
        processFinishCache.invalidateAll();
    }

    // CONSTRUCTORS

    /**
     * Creates a new {@link ProcessHookAttacher}
     *
     * @param executionService executor which will be executing the behaviour; in general, this executor should not
     *                         have an upper thread limit, as it is likely invoked multiple times for a single process,
     *                         though many of the threads are likely to be short lived
     * @param processFinishService executor which will be waiting for processes to finish; make sure it can create
     *                             sufficient threads if {@link LimitedProcessRunner} is used!
     * @throws NullPointerException if executionService or processFinishService is null
     */
    public ProcessHookAttacher(ExecutorService executionService, ExecutorService processFinishService) {
        Null.check(executionService, processFinishService).as("executionService, processFinishService");

        this.executionService = executionService;
        this.processFinishService = processFinishService;

        this.processFinishCache = Caffeine.newBuilder().build();
    }

    // PRIVATE

    private final ExecutorService executionService;
    private final ExecutorService processFinishService;

    private final Cache<Process, CompletableFuture<Void>> processFinishCache;

    private CompletableFuture<Void> hookWaitingForFinish(Process process) {
        return CompletableFuture.runAsync(() -> waitFor(process), processFinishService);
    }

    private void waitFor(Process process) {
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            LOG.error("Waiting for process to finish has been interrupted", e);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(ProcessHookAttacher.class);

}
