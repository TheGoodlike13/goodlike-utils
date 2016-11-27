package eu.goodlike.cmd;

import eu.goodlike.libraries.slf4j.Log;
import eu.goodlike.neat.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

/**
 * Logs all output from the process under desired logging level
 */
public final class LoggingProcessRunner implements ProcessRunner {

    @Override
    public Optional<Process> execute(String command, String... args) {
        return processRunner.execute(command, args)
                .map(process -> processHookAttacher.attachDuring(process, this::logAllOutput));
    }

    @Override
    public Optional<Process> execute(String command, List<String> args) {
        return processRunner.execute(command, args)
                .map(process -> processHookAttacher.attachDuring(process, this::logAllOutput));
    }

    @Override
    public void close() throws Exception {
        processHookAttacher.close();
        processRunner.close();
    }

    // CONSTRUCTORS

    /**
     * Creates a {@link LoggingProcessRunner} which logs the output from spawned processes
     *
     * @param processRunner runner to spawn processes with
     * @param log log level to log at
     * @param processHookAttacher attacher of hooks that this runner will use to log during execution
     * @throws NullPointerException if processRunner, log or processHookAttacher is null
     */
    public LoggingProcessRunner(ProcessRunner processRunner, Log log, ProcessHookAttacher processHookAttacher) {
        Null.check(processRunner, log, processHookAttacher).as("processRunner, log, processHookAttacher");

        this.processRunner = processRunner;
        this.log = log;
        this.processHookAttacher = processHookAttacher;
    }

    // PRIVATE

    private final ProcessRunner processRunner;
    private final Log log;
    private final ProcessHookAttacher processHookAttacher;

    private void logAllOutput(Process process) {
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        try {
            while ((line = input.readLine()) != null)
                log.log(LOGGER, line);
        } catch (IOException e) {
            LOGGER.error("Exception occurred when trying to read process outpur", e);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingProcessRunner.class);

}
