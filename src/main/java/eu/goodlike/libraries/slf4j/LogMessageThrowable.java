package eu.goodlike.libraries.slf4j;

import org.slf4j.Logger;

/**
 * @see Log
 */
@FunctionalInterface
public interface LogMessageThrowable {

    void log(Logger logger, String msg, Throwable t);

}
