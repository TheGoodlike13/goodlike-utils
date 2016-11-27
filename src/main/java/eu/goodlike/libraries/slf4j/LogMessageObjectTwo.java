package eu.goodlike.libraries.slf4j;

import org.slf4j.Logger;

/**
 * @see Log
 */
@FunctionalInterface
public interface LogMessageObjectTwo {

    void log(Logger logger, String format, Object arg1, Object arg2);

}
