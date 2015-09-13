package eu.goodlike.libraries.jodatime;

import org.joda.time.*;

/**
 * <pre>
 * Converts to and from any of the following formats:
 * org.joda.time.Instant
 * org.joda.time.DateTime;
 * org.joda.time.LocalDate;
 * org.joda.time.LocalTime; (+ hour, minute, second, millisecond)
 * java.util.Instant;
 * java.util.Date;
 * java.sql.Date;
 * epoch milliseconds;
 *
 * Uses epoch milliseconds as the base for converting; uses the timezone of this handler;
 *
 * When converting to and from LocalDate/LocalTime/SqlDate assumes time 00:00:00.000
 *
 * The handler class itself is thread-safe;
 * the Converter class returned by its methods is mutable, and not thread-safe; however, it is not intended to be used
 * across threads anyway (in general, if you want to convert a value, you want to do it at once, not store it for use
 * by multiple threads)
 * </pre>
 */
public final class TimeHandler {

    public TimeConverter from(long epochMillis) {
        return new TimeConverter(timeZone, epochMillis, null, null, null, null, null, null);
    }

    public TimeConverter from(Instant instant) {
        return new TimeConverter(timeZone, instant.getMillis(), instant, null, null, null, null, null);
    }

    public TimeConverter from(DateTime dateTime) {
        return new TimeConverter(timeZone, dateTime.getMillis(), null, dateTime, null, null, null, null);
    }

    /**
     * The instance created by this method will assume that the time was 00:00:00.000, using the handler's timezone
     */
    public TimeConverter from(LocalDate localDate) {
        DateTime dateTime = localDate.toDateTimeAtStartOfDay(timeZone);
        return new TimeConverter(timeZone, dateTime.getMillis(), null, dateTime, localDate, null, null, null);
    }

    public TimeConverter from(LocalDate localDate, LocalTime localTime) {
        DateTime dateTime = localDate.toDateTime(localTime, timeZone);
        return new TimeConverter(timeZone, dateTime.getMillis(), null, dateTime, localDate, localTime, null, null);
    }

    /**
     * @throws IllegalArgumentException if the total time period of hours, minutes, seconds and milliseconds
     *          exceeds a day's worth of time (essentially, overflowing into the next day)
     */
    public TimeConverter from(LocalDate localDate, int hours, int minutes, int seconds, int milliseconds) {
        DateTime dateTimeAtStartOfDay = localDate.toDateTimeAtStartOfDay(timeZone);
        DateTime dateTime = dateTimeAtStartOfDay.plusHours(hours).plusMinutes(minutes).plusSeconds(seconds).plusMillis(milliseconds);

        if (!dateTimeAtStartOfDay.toLocalDate().equals(dateTime.toLocalDate()))
            throw new IllegalArgumentException("The given values exceed a day's worth of time!");

        return new TimeConverter(timeZone, dateTime.getMillis(), null, dateTime, localDate, null, null, null);
    }

    /**
     * @throws IllegalArgumentException if the total time period of hours, minutes and seconds
     *          exceeds a day's worth of time (essentially, overflowing into the next day)
     */
    public TimeConverter from(LocalDate localDate, int hours, int minutes, int seconds) {
        return from(localDate, hours, minutes, seconds, 0);
    }

    /**
     * @throws IllegalArgumentException if the total time period of hours and minutes
     *          exceeds a day's worth of time (essentially, overflowing into the next day)
     */
    public TimeConverter from(LocalDate localDate, int hours, int minutes) {
        return from(localDate, hours, minutes, 0, 0);
    }

    /**
     * @throws IllegalArgumentException if the total time period of hours
     *          exceeds a day's worth of time (essentially, overflowing into the next day)
     */
    public TimeConverter from(LocalDate localDate, int hours) {
        return from(localDate, hours, 0, 0, 0);
    }

    public TimeConverter from(java.time.Instant instant) {
        return new TimeConverter(timeZone, instant.toEpochMilli(), null, null, null, null, instant, null);
    }

    /**
     * <pre>
     * This method explicitly checks if the Date instance is a java.sql.Date or not
     * Please refer to "from(java.sql.Date)" method for reasons
     *
     * The assumption behind java.util.Date is that it was created using a long value, i.e.
     *      new java.util.Date(long);
     * </pre>
     */
    public TimeConverter from(java.util.Date date) {
        if (date instanceof java.sql.Date)
            return from((java.sql.Date) date);

        return new TimeConverter(timeZone, date.getTime(), null, null, null, null, null, date);
    }

    /**
     * <pre>
     * The assumption behind java.sql.Date is that it was created using a String value, i.e.
     *      java.sql.Date.valueOf("yyyy-MM-dd");
     * This uses the JAVA timezone to create a localized instance; therefore, the epoch milliseconds value
     * refers to the start of day at JAVA timezone, and the String value of the Date can get altered during conversion;
     * using toString() method, however, once again uses JAVA timezone, essentially allowing the value to remain correct
     *
     * The instance created by this method will assume that the time was 00:00:00.000, using the handler's timezone
     * </pre>
     */
    public TimeConverter from(java.sql.Date date) {
        return from(date.toString());
    }

    /**
     * <pre>
     * This method expects the following date format:
     *      "yyyy-MM-dd"; for example: "2015-08-05"
     *
     * The instance created by this method will assume that the time was 00:00:00.000, using the handler's timezone
     * </pre>
     */
    public TimeConverter from(String dateString) {
        return from(LocalDate.parse(dateString));
    }

    // CONSTRUCTORS

    public TimeHandler(DateTimeZone timeZone) {
        this.timeZone = timeZone;
    }

    // PRIVATE

    private final DateTimeZone timeZone;

}
