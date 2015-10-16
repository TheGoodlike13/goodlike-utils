package eu.goodlike.time;

import com.google.common.base.MoreObjects;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

/**
 * <pre>
 * Resolves epoch milliseconds from various formats
 *
 * Intended to reduce code duplication when checking for variables, etc
 * </pre>
 */
public final class TimeResolver {

    /**
     * @return epoch milliseconds of the starting time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @return epoch milliseconds of the ending time
     */
    public long getEndTime() {
        return endTime;
    }

    // CONSTRUCTORS

    /**
     * <pre>
     * Resolves the time using the following logic:
     *
     * 1) if start and end are set, use those;
     * 2) if at least one of them is not set, resolve the time from remaining parameters, then use that time
     * to set start, end or both and use those;
     * </pre>
     */
    public static TimeResolver from(String timezone, String startDate, String endDate, Long start, Long end) {
        if (start == null || end == null) {
            TimeResolver step = TimeResolver.from(timezone, startDate, endDate);
            if (start == null)
                start = step.startTime;
            if (end == null)
                end = step.endTime;
        }
        return from(start, end);
    }

    /**
     * <pre>
     * Resolves the time using the following logic:
     *
     * 1) if timeZone is set, parse it, otherwise use default (UTC)
     * 2) if startDate is set, parse it to LocalDate using the timezone, otherwise set to current date using the timezone;
     * 3) if endDate is set, parse it to LocalDate using the timezone, otherwise set to startDate
     * 4) Resolve time from resulting ZoneId, start and end LocalDates
     * </pre>
     */
    public static TimeResolver from(String timezone, String startDate, String endDate) {
        ZoneId zoneId = timezone == null
                ? Time.UTC()
                : ZoneId.of(timezone);
        LocalDate localStartDate = startDate == null
                ? LocalDate.now(zoneId)
                : LocalDate.parse(startDate);
        LocalDate localEndDate = endDate == null
                ? localStartDate
                : LocalDate.parse(endDate);
        return from(zoneId, localStartDate, localEndDate);
    }

    /**
     * Resolves the time using JodaTimeZoneHandler for given zoneId and given LocalDates
     */
    public static TimeResolver from(ZoneId zoneId, LocalDate localStartDate, LocalDate localEndDate) {
        return from(Time.at(zoneId), localStartDate, localEndDate);
    }

    /**
     * <pre>
     * Resolves the time using the following logic:
     *
     * 1) get start epoch milliseconds from localStartDate;
     * 2) get end epoch milliseconds from localEndDate + 1 day (so it is inclusive);
     * 3) use start and end to resolve time
     * </pre>
     */
    public static TimeResolver from(TimeHandler handler, LocalDate localStartDate, LocalDate localEndDate) {
        long start = handler.from(localStartDate).toEpochMilli();
        long end = handler.from(localEndDate.plusDays(1)).toEpochMilli();
        return from(start, end);
    }

    /**
     * @return TimeResolver for already known start/end epoch millisecond values; fairly pointless, exists for
     * compatibility only
     */
    public static TimeResolver from(long start, long end) {
        return new TimeResolver(start, end);
    }

    public TimeResolver(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // PRIVATE

    private final long startTime;
    private final long endTime;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("startTime", startTime)
                .add("endTime", endTime)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeResolver)) return false;
        TimeResolver that = (TimeResolver) o;
        return Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

}
