package eu.goodlike.functional;

/**
 * Runnable replacement for not necessarily threaded context; can throw a Throwable
 */
@FunctionalInterface
public interface ThrowAction {

    /**
     * Executes an arbitrary action; can throw a Throwable
     */
    void doIt() throws Throwable;

}
