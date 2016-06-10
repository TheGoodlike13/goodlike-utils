package eu.goodlike.time;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import eu.goodlike.neat.Null;
import eu.goodlike.time.impl.TimeHandler;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * <pre>
 * Converts between time representations using a ZoneId where appropriate
 *
 * It seems that with the release of Java 8, joda time will no longer be updated a lot, thus I've decided to
 * move to the new standard as well
 * </pre>
 */
public final class Time {

    /**
     * @return UTC timezone
     */
    public static ZoneId UTC() {
        return DEFAULT_TIME_ZONE;
    }

    /**
     * @return lazily cached TimeHandler for a given java timezone
     * @throws NullPointerException if timezone is null
     */
    public static TimeHandler at(ZoneId timezone) {
        Null.check(timezone).ifAny("Timezone cannot be null");
        return HANDLER_CACHE.get(timezone);
    }

    /**
     * @return default TimeHandler, which uses UTC
     */
    public static TimeHandler atUTC() {
        return at(DEFAULT_TIME_ZONE);
    }

    /**
     * @return lazily cached TimeHandler for a given java timezone in String form
     * @throws NullPointerException if timezone is null
     */
    public static TimeHandler at(String timezone) {
        Null.check(timezone).ifAny("Timezone cannot be null");
        return at(ZoneId.of(timezone));
    }

    /**
     * @return Date to Date converter for a java.sql.Date
     */
    public static DateConverter convert(java.sql.Date date) {
        return atUTC().from(date);
    }

    /**
     * @return Date to Date converter for a java LocalDate
     */
    public static DateConverter convert(LocalDate date) {
        return atUTC().from(date);
    }

    /**
     * @return millis to Date converter; uses UTC
     */
    public static DateConverter convert(long millis) {
        return atUTC().from(millis);
    }

    // PRIVATE

    private static final ZoneId DEFAULT_TIME_ZONE = ZoneId.of("UTC");
    private static final LoadingCache<ZoneId, TimeHandler> HANDLER_CACHE = Caffeine.newBuilder()
            .build(TimeHandler::new);

    private Time() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
