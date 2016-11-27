package eu.goodlike.libraries.slf4j;

import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * @see Log
 */
@FunctionalInterface
public interface MarkerLogMessageThrowable {

    void log(Logger logger, Marker marker, String msg, Throwable t);

}
