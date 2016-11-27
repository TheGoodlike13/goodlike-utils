package eu.goodlike.libraries.slf4j;

import org.slf4j.Logger;

/**
 * @see Log
 */
@FunctionalInterface
public interface LogMessageObject {

    void log(Logger logger, String format, Object arg);

}
