package eu.goodlike.libraries.slf4j;

import org.slf4j.Logger;

/**
 * @see Log
 */
@FunctionalInterface
public interface LogEnabledCheck {

    boolean isEnabled(Logger logger);

}
