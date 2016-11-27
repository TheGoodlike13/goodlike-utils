package eu.goodlike.cmd;

import eu.goodlike.misc.SpecialUtils;
import eu.goodlike.neat.Null;

import java.util.List;
import java.util.Optional;

/**
 * <pre>
 * Binds all processes run by this runner to the spawning program, closing them when it exits normally
 *
 * For specific behaviour refer to {@link Runtime#addShutdownHook(Thread)}
 * </pre>
 */
public final class BoundProcessRunner implements ProcessRunner {

    @Override
    public Optional<Process> execute(String command, String... args) {
        return processRunner.execute(command, args)
                .map(this::bindToProgram);
    }

    @Override
    public Optional<Process> execute(String command, List<String> args) {
        return processRunner.execute(command, args)
                .map(this::bindToProgram);
    }

    @Override
    public void close() throws Exception {
        processRunner.close();
    }

    // CONSTRUCTORS

    /**
     * Creates a {@link BoundProcessRunner} which binds all of given runner's processes to the program
     *
     * @param processRunner runner to bind to the program
     * @throws NullPointerException if processRunner is null
     */
    public BoundProcessRunner(ProcessRunner processRunner) {
        Null.check(processRunner).as("processRunner");
        this.processRunner = processRunner;
    }

    // PRIVATE

    private final ProcessRunner processRunner;

    private Process bindToProgram(Process process) {
        SpecialUtils.runOnExit(process::destroyForcibly);
        return process;
    }

}
