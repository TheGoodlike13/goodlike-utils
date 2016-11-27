package eu.goodlike.cmd;

/**
 * Defines behaviour to execute while a {@link Process} is working
 */
@FunctionalInterface
public interface DuringProcessHook {

    /**
     * Custom behaviour to execute after while {@link Process} is working
     *
     * @param process process for which to execute behaviour
     * @throws NullPointerException if process is null
     */
    void doDuringProcess(Process process);

}
