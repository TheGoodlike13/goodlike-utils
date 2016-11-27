package eu.goodlike.cmd;

import java.util.List;
import java.util.Optional;

/**
 * <pre>
 * Defines how to execute commands as a separate process
 *
 * Closing this runner will not end the processes, but can prevent additional processes from being started (in the case
 * where resources are necessary to start the process). If you wish to end a process, destroy the process returned by
 * {@link ProcessRunner#execute(String, String...)} or {@link ProcessRunner#execute(String, List)} methods
 * </pre>
 */
public interface ProcessRunner extends AutoCloseable {

    /**
     * <pre>
     * Executes given command with args as a new process
     *
     * Empty arguments (whitespace only) are ignored
     * </pre>
     * @param command command to execute
     * @param args arguments to pass to the command
     * @return process which is running as a result of execution; {@link Optional#empty()} if process could not be started
     * @throws NullPointerException if command is null
     * @throws NullPointerException if args is or contains null
     * @throws IllegalArgumentException if command is empty or whitespace
     */
    Optional<Process> execute(String command, String... args);

    /**
     * <pre>
     * Executes given command with args as a new process
     *
     * Empty arguments (whitespace only) are ignored
     * </pre>
     * @param command command to execute
     * @param args arguments to pass to the command
     * @return process which is running as a result of execution; {@link Optional#empty()} if process could not be started
     * @throws NullPointerException if command is null
     * @throws NullPointerException if args is or contains null
     * @throws IllegalArgumentException if command is empty or whitespace
     */
    Optional<Process> execute(String command, List<String> args);

}
