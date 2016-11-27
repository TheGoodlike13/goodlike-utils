package eu.goodlike.libraries.slf4j;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.util.Map;

/**
 * <pre>
 * Dynamic log level implementation for {@link Logger}
 *
 * Uses all the below interfaces to construct lambdas which are then invoked for the correct level
 * </pre>
 */
public enum Log implements LogEnabledCheck, LogMessage, LogMessageObject, LogMessageObjectTwo,
        LogMessageObjectVarargs, LogMessageThrowable, MarkerLogEnabledCheck, MarkerLogMessage, MarkerLogMessageObject,
        MarkerLogMessageObjectTwo, MarkerLogMessageObjectVarargs, MarkerLogMessageThrowable {

    TRACE, DEBUG, INFO, WARN, ERROR;

    @Override
    public boolean isEnabled(Logger logger) {
        return LOG_ENABLED_CHECK_MAP.get(this).isEnabled(logger);
    }

    @Override
    public void log(Logger logger, String msg) {
        LOG_MESSAGE_MAP.get(this).log(logger, msg);
    }

    @Override
    public void log(Logger logger, String format, Object arg) {
        LOG_MESSAGE_OBJECT_MAP.get(this).log(logger, format, arg);
    }

    @Override
    public void log(Logger logger, String format, Object arg1, Object arg2) {
        LOG_MESSAGE_OBJECT_TWO_MAP.get(this).log(logger, format, arg1, arg2);
    }

    @Override
    public void log(Logger logger, String format, Object... arguments) {
        LOG_MESSAGE_VARARGS_MAP.get(this).log(logger, format, arguments);
    }

    @Override
    public void log(Logger logger, String msg, Throwable t) {
        LOG_MESSAGE_THROWABLE_MAP.get(this).log(logger, msg, t);
    }

    @Override
    public boolean isEnabled(Logger logger, Marker marker) {
        return MARKER_LOG_ENABLED_CHECK_MAP.get(this).isEnabled(logger, marker);
    }

    @Override
    public void log(Logger logger, Marker marker, String msg) {
        MARKER_LOG_MESSAGE_MAP.get(this).log(logger, marker, msg);
    }

    @Override
    public void log(Logger logger, Marker marker, String format, Object arg) {
        MARKER_LOG_MESSAGE_OBJECT_MAP.get(this).log(logger, marker, format, arg);
    }

    @Override
    public void log(Logger logger, Marker marker, String format, Object arg1, Object arg2) {
        MARKER_LOG_MESSAGE_OBJECT_TWO_MAP.get(this).log(logger, marker, format, arg1, arg2);
    }

    @Override
    public void log(Logger logger, Marker marker, String format, Object... arguments) {
        MARKER_LOG_MESSAGE_VARARGS_MAP.get(this).log(logger, marker, format, arguments);
    }

    @Override
    public void log(Logger logger, Marker marker, String msg, Throwable t) {
        MARKER_LOG_MESSAGE_THROWABLE_MAP.get(this).log(logger, marker, msg, t);
    }

    private static final Map<Log, LogEnabledCheck> LOG_ENABLED_CHECK_MAP = Maps.immutableEnumMap(ImmutableMap.of(
            TRACE, Logger::isTraceEnabled,
            DEBUG, Logger::isDebugEnabled,
            INFO, Logger::isInfoEnabled,
            WARN, Logger::isWarnEnabled,
            ERROR, Logger::isErrorEnabled
    ));

    private static final Map<Log, LogMessage> LOG_MESSAGE_MAP = Maps.immutableEnumMap(ImmutableMap.of(
            TRACE, Logger::trace,
            DEBUG, Logger::debug,
            INFO, Logger::info,
            WARN, Logger::warn,
            ERROR, Logger::error
    ));

    private static final Map<Log, LogMessageObject> LOG_MESSAGE_OBJECT_MAP = Maps.immutableEnumMap(ImmutableMap.of(
            TRACE, Logger::trace,
            DEBUG, Logger::debug,
            INFO, Logger::info,
            WARN, Logger::warn,
            ERROR, Logger::error
    ));

    private static final Map<Log, LogMessageObjectTwo> LOG_MESSAGE_OBJECT_TWO_MAP = Maps.immutableEnumMap(ImmutableMap.of(
            TRACE, Logger::trace,
            DEBUG, Logger::debug,
            INFO, Logger::info,
            WARN, Logger::warn,
            ERROR, Logger::error
    ));

    private static final Map<Log, LogMessageObjectVarargs> LOG_MESSAGE_VARARGS_MAP = Maps.immutableEnumMap(ImmutableMap.of(
            TRACE, Logger::trace,
            DEBUG, Logger::debug,
            INFO, Logger::info,
            WARN, Logger::warn,
            ERROR, Logger::error
    ));

    private static final Map<Log, LogMessageThrowable> LOG_MESSAGE_THROWABLE_MAP = Maps.immutableEnumMap(ImmutableMap.of(
            TRACE, Logger::trace,
            DEBUG, Logger::debug,
            INFO, Logger::info,
            WARN, Logger::warn,
            ERROR, Logger::error
    ));

    private static final Map<Log, MarkerLogEnabledCheck> MARKER_LOG_ENABLED_CHECK_MAP = Maps.immutableEnumMap(ImmutableMap.of(
            TRACE, Logger::isTraceEnabled,
            DEBUG, Logger::isDebugEnabled,
            INFO, Logger::isInfoEnabled,
            WARN, Logger::isWarnEnabled,
            ERROR, Logger::isErrorEnabled
    ));

    private static final Map<Log, MarkerLogMessage> MARKER_LOG_MESSAGE_MAP = Maps.immutableEnumMap(ImmutableMap.of(
            TRACE, Logger::trace,
            DEBUG, Logger::debug,
            INFO, Logger::info,
            WARN, Logger::warn,
            ERROR, Logger::error
    ));

    private static final Map<Log, MarkerLogMessageObject> MARKER_LOG_MESSAGE_OBJECT_MAP = Maps.immutableEnumMap(ImmutableMap.of(
            TRACE, Logger::trace,
            DEBUG, Logger::debug,
            INFO, Logger::info,
            WARN, Logger::warn,
            ERROR, Logger::error
    ));

    private static final Map<Log, MarkerLogMessageObjectTwo> MARKER_LOG_MESSAGE_OBJECT_TWO_MAP = Maps.immutableEnumMap(ImmutableMap.of(
            TRACE, Logger::trace,
            DEBUG, Logger::debug,
            INFO, Logger::info,
            WARN, Logger::warn,
            ERROR, Logger::error
    ));

    private static final Map<Log, MarkerLogMessageObjectVarargs> MARKER_LOG_MESSAGE_VARARGS_MAP = Maps.immutableEnumMap(ImmutableMap.of(
            TRACE, Logger::trace,
            DEBUG, Logger::debug,
            INFO, Logger::info,
            WARN, Logger::warn,
            ERROR, Logger::error
    ));

    private static final Map<Log, MarkerLogMessageThrowable> MARKER_LOG_MESSAGE_THROWABLE_MAP = Maps.immutableEnumMap(ImmutableMap.of(
            TRACE, Logger::trace,
            DEBUG, Logger::debug,
            INFO, Logger::info,
            WARN, Logger::warn,
            ERROR, Logger::error
    ));

}
