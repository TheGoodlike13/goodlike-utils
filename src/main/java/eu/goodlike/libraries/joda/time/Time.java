package eu.goodlike.libraries.joda.time;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

/**
 * <pre>
 * Converts between various time representations
 *
 * The TimeHandlers returned by this class are cached using Guava's LoadingCache
 * </pre>
 */
public final class Time {

    /**
     * @return lazily cached TimeHandler for a given joda timezone
     */
    public static TimeHandler forZone(DateTimeZone timeZone) {
        try {
            return HANDLER_CACHE.get(timeZone);
        } catch (ExecutionException e) {
            throw new AssertionError("Some unexpected error happened while caching TimeHandler", e);
        }
    }

    /**
     * @return default TimeHandler, which uses UTC
     */
    public static TimeHandler getDefault() {
        return forZone(defaultTimeZone());
    }

    /**
     * @return lazily cached TimeHandler for a given joda timezone id; check this link for values:
     *      http://joda-time.sourceforge.net/timezones.html
     */
    public static TimeHandler forZoneId(String timeZoneId) {
        return timeZoneId == null
                ? getDefault()
                : forZone(DateTimeZone.forID(timeZoneId));
    }

    /**
     * @return lazily cached TimeHandler for a given java timezone
     */
    public static TimeHandler forZone(TimeZone timeZone) {
        return forZone(DateTimeZone.forTimeZone(timeZone));
    }

    /**
     * @return UTC timezone
     */
    public static DateTimeZone defaultTimeZone() {
        return DEFAULT_TIME_ZONE;
    }

    /**
     * @return Date to Date converter for a java.sql.Date
     */
    public static DateConverter convert(java.sql.Date date) {
        return getDefault().from(date);
    }

    /**
     * @return Date to Date converter for a joda LocalDate
     */
    public static DateConverter convert(LocalDate date) {
        return getDefault().from(date);
    }

    /**
     * @return millis to Date converter; uses UTC
     */
    public static DateConverter convert(long millis) {
        return getDefault().from(millis);
    }

    // PRIVATE

    private static final DateTimeZone DEFAULT_TIME_ZONE = DateTimeZone.forID("UTC");
    private static final LoadingCache<DateTimeZone, TimeHandler> HANDLER_CACHE = CacheBuilder.newBuilder()
            .build(CacheLoader.from(TimeHandler::new));

    private Time() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
