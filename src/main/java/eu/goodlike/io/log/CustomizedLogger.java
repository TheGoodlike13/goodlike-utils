package eu.goodlike.io.log;

/**
 * <pre>
 * Basic logger interface
 *
 * Prefer to use mature libraries like slf4j/logback/etc; this should only be used when their specification does not
 * cover what you need (i.e. you need something simpler, like a command line program output)
 * </pre>
 */
public interface CustomizedLogger {

    /**
     * Logs given line into whatever output this logger has, if any
     */
    void logMessage(String line);

}
