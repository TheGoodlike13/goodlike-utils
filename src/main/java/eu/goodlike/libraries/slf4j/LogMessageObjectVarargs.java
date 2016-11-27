package eu.goodlike.libraries.slf4j;

import org.slf4j.Logger;

/**
 * @see Log
 */
@FunctionalInterface
public interface LogMessageObjectVarargs {

    void log(Logger logger, String format, Object... arguments);

}
