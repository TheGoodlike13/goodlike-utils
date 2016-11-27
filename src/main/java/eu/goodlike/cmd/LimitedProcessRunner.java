package eu.goodlike.cmd;

import eu.goodlike.neat.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;

/**
 * Limits the amount of concurrent processes that can be spawned
 */
public final class LimitedProcessRunner implements ProcessRunner {

    @Override
    public Optional<Process> execute(String command, String... args) {
        return acquirePermitToSpawnAnAdditionalProcess()
                .flatMap(permit -> processRunner.execute(command, args))
                .map(process -> processHookAttacher.attachAfter(process, any -> parallelExecutionLimiter.release()));
    }

    @Override
    public Optional<Process> execute(String command, List<String> args) {
        return acquirePermitToSpawnAnAdditionalProcess()
                .flatMap(permit -> processRunner.execute(command, args))
                .map(process -> processHookAttacher.attachAfter(process, any -> parallelExecutionLimiter.release()));
    }

    @Override
    public void close() throws Exception {
        processHookAttacher.close();
        processRunner.close();
    }

    // CONSTRUCTORS

    /**
     * Creates a {@link LimitedProcessRunner} which limits the spawning of processes to given amount, using given
     * runner as a process spawner
     *
     * @param processRunner runner to spawn processes with
     * @param maxConcurrentProcesses max concurrent processes this runner can spawn
     * @param processHookAttacher attacher of hooks that this runner will use to ensure that parallel execution limit
     *                            is correctly released after a process finishes
     * @throws NullPointerException if processRunner or processHookAttacher is null
     * @throws IllegalArgumentException if maxConcurrentProcesses < 1
     */
    public LimitedProcessRunner(ProcessRunner processRunner, int maxConcurrentProcesses, ProcessHookAttacher processHookAttacher) {
        Null.check(processRunner, processHookAttacher).as("processRunner, processHookAttacher");
        if (maxConcurrentProcesses < 1)
            throw new IllegalArgumentException("Cannot create fewer than 1 process: " + maxConcurrentProcesses);

        this.processRunner = processRunner;
        this.parallelExecutionLimiter = new Semaphore(maxConcurrentProcesses);
        this.processHookAttacher = processHookAttacher;
    }

    // PRIVATE

    private final ProcessRunner processRunner;
    private final Semaphore parallelExecutionLimiter;
    private final ProcessHookAttacher processHookAttacher;

    private Optional<Object> acquirePermitToSpawnAnAdditionalProcess() {
        try {
            parallelExecutionLimiter.acquire();
            return PERMIT;
        } catch (InterruptedException e) {
            LOG.error("Waiting to acquire permit to spawn additional process has been interrupted", e);
            return Optional.empty();
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static final Optional<Object> PERMIT = Optional.of(new Object());

    private static final Logger LOG = LoggerFactory.getLogger(LimitedProcessRunner.class);

}
