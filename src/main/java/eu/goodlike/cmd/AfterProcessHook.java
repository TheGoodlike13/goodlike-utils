package eu.goodlike.cmd;

/**
 * Defines behaviour to execute after a {@link Process} is done
 */
@FunctionalInterface
public interface AfterProcessHook {

    /**
     * Custom behaviour to execute after the {@link Process} is done
     *
     * @param process process for which to execute behaviour
     * @throws NullPointerException if process is null
     */
    void doAfterProcess(Process process);

}
