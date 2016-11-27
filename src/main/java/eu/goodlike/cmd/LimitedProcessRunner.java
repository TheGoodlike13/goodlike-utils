package eu.goodlike.cmd;

import eu.goodlike.neat.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Limits the amount of concurrent processes that can be spawned
 */
public final class LimitedProcessRunner implements ProcessRunner {

    @Override
    public Optional<Process> execute(String command, String... args) {
        return acquirePermitToSpawnAnAdditionalProcess()
                .flatMap(permit -> processRunner.execute(command, args))
                .map(this::attachHook);
    }

    @Override
    public Optional<Process> execute(String command, List<String> args) {
        return acquirePermitToSpawnAnAdditionalProcess()
                .flatMap(permit -> processRunner.execute(command, args))
                .map(this::attachHook);
    }

    @Override
    public void close() throws Exception {
        processHook.shutdown();
        processRunner.close();
    }

    // CONSTRUCTORS

    /**
     * Creates a {@link LimitedProcessRunner} which limits the spawning of processes to given amount, using given
     * runner as a process spawner
     *
     * @param processRunner runner to spawn processes with
     * @param maxConcurrentProcesses max concurrent processes this runner can spawn
     * @throws NullPointerException if processRunner is null
     * @throws IllegalArgumentException if maxConcurrentProcesses < 1
     */
    public LimitedProcessRunner(ProcessRunner processRunner, int maxConcurrentProcesses) {
        this(processRunner, maxConcurrentProcesses, Executors.newFixedThreadPool(maxConcurrentProcesses));
    }

    /**
     * Creates a {@link LimitedProcessRunner} which limits the spawning of processes to given amount, using given
     * runner as a process spawner
     *
     * @param processRunner runner to spawn processes with
     * @param maxConcurrentProcesses max concurrent processes this runner can spawn
     * @param customProcessHook custom hook for checking if a process has ended; if this hook cannot spawn sufficient
     *                          threads (i.e. < maxConcurrentProcesses), behaviour of LimitedProcessRunner is undefined
     * @throws NullPointerException if processRunner or customProcessHook is null
     * @throws IllegalArgumentException if maxConcurrentProcesses < 1
     */
    public LimitedProcessRunner(ProcessRunner processRunner, int maxConcurrentProcesses, ExecutorService customProcessHook) {
        Null.check(processRunner, customProcessHook).as("processRunner, customProcessHook");
        if (maxConcurrentProcesses < 1)
            throw new IllegalArgumentException("Cannot create fewer than 1 process: " + maxConcurrentProcesses);

        this.processRunner = processRunner;
        this.parallelExecutionLimiter = new Semaphore(maxConcurrentProcesses);
        this.processHook = customProcessHook;
    }

    // PRIVATE

    private final ProcessRunner processRunner;
    private final Semaphore parallelExecutionLimiter;
    private final ExecutorService processHook;

    private Optional<Object> acquirePermitToSpawnAnAdditionalProcess() {
        try {
            parallelExecutionLimiter.acquire();
            return PERMIT;
        } catch (InterruptedException e) {
            LOG.error("Waiting to acquire permit to spawn additional process has been interrupted", e);
            return Optional.empty();
        }
    }

    private Process attachHook(Process process) {
        processHook.submit(() -> releaseAfterExecution(process));
        return process;
    }

    private void releaseAfterExecution(Process process) {
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            LOG.error("Waiting for process to finish has been interrupted", e);
        }

        parallelExecutionLimiter.release();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static final Optional<Object> PERMIT = Optional.of(new Object());

    private static final Logger LOG = LoggerFactory.getLogger(LimitedProcessRunner.class);

}
