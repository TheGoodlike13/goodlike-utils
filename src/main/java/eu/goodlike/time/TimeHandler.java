package eu.goodlike.time;

import eu.goodlike.neat.Null;

import java.time.*;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.MILLIS;

/**
 * <pre>
 * Immutable wrapper for a ZoneId (represents a timezone) which can be used to convert one kind of time representation
 * to another
 *
 * Notable specifics:
 *      1) java.sql.Date is handled as a String representation of some date, in format 'yyyy-MM-dd' (i.e. 2015-09-23)
 *      2) java.util.Date is handled as a timestamp, i.e. 1442994396115
 *      3) All conversions use java.time.Instant as the base class; those formats that represent a date, i.e.
 *          LocalDate are considered to have 00:00:00.000 time part, using the timezone of this handler
 *
 * All TimeConverter instances resulting from the methods will already have pre-evaluated all the in-between steps
 * of the conversion; i.e. from(someInstant).toInstant() returns the same instant; from(localDate).toLocalDate() only
 * calculates the appropriate Instant representation, but does not try to re-calculate the localDate back from it, etc
 * </pre>
 */
public final class TimeHandler {

    /**
     * @throws NullPointerException if instant is null
     */
    public TimeConverter from(Instant instant) {
        Null.check(instant).ifAny("Instant cannot be null");
        return new TimeConverter(timezone, instant, null, null, null, null, null);
    }

    public TimeConverter from(long epochMillis) {
        return new TimeConverter(timezone, Instant.ofEpochMilli(epochMillis), epochMillis, null, null, null, null);
    }

    /**
     * @throws NullPointerException if dateTime is null
     */
    public TimeConverter from(ZonedDateTime dateTime) {
        Null.check(dateTime).ifAny("DateTime cannot be null");
        return new TimeConverter(timezone, dateTime.toInstant(), null, dateTime, null, null, null);
    }

    /**
     * The instance created by this method will assume that the time was 00:00:00.000, using the handler's timezone
     * @throws NullPointerException if localDate is null
     */
    public TimeConverter from(LocalDate localDate) {
        Null.check(localDate).ifAny("LocalDate cannot be null");
        ZonedDateTime dateTime = localDate.atStartOfDay(timezone);
        return new TimeConverter(timezone, dateTime.toInstant(), null, dateTime, localDate, null, null);
    }

    /**
     * @throws NullPointerException if localDate or localTime is null
     */
    public TimeConverter from(LocalDate localDate, LocalTime localTime) {
        Null.check(localDate, localTime).ifAny("LocalDate and LocalTime cannot be null");
        ZonedDateTime dateTime = localDate.atTime(localTime).atZone(timezone);
        return new TimeConverter(timezone, dateTime.toInstant(), null, dateTime, localDate, localTime, null);
    }

    /**
     * @throws NullPointerException if localDate is null
     * @throws IllegalArgumentException if the total time period of hours, minutes, seconds and milliseconds
     *          exceeds a day's worth of time (essentially, overflowing into the next day)
     */
    public TimeConverter from(LocalDate localDate, int hours, int minutes, int seconds, int milliseconds) {
        Null.check(localDate).ifAny("LocalDate cannot be null");
        ZonedDateTime dateTimeAtStartOfDay = localDate.atStartOfDay(timezone);
        ZonedDateTime dateTime = dateTimeAtStartOfDay.plusHours(hours).plusMinutes(minutes).plusSeconds(seconds).plus(milliseconds, MILLIS);
        if (!dateTimeAtStartOfDay.toLocalDate().equals(dateTime.toLocalDate()))
            throw new IllegalArgumentException("The given values exceed a day's worth of time!");

        return new TimeConverter(timezone, dateTime.toInstant(), null, dateTime, localDate, null, null);
    }

    /**
     * @throws NullPointerException if localDate is null
     * @throws IllegalArgumentException if the total time period of hours, minutes and seconds
     *          exceeds a day's worth of time (essentially, overflowing into the next day)
     */
    public TimeConverter from(LocalDate localDate, int hours, int minutes, int seconds) {
        return from(localDate, hours, minutes, seconds, 0);
    }

    /**
     * @throws NullPointerException if localDate is null
     * @throws IllegalArgumentException if the total time period of hours and minutes
     *          exceeds a day's worth of time (essentially, overflowing into the next day)
     */
    public TimeConverter from(LocalDate localDate, int hours, int minutes) {
        return from(localDate, hours, minutes, 0, 0);
    }

    /**
     * @throws NullPointerException if localDate is null
     * @throws IllegalArgumentException if the total time period of hours
     *          exceeds a day's worth of time (essentially, overflowing into the next day)
     */
    public TimeConverter from(LocalDate localDate, int hours) {
        return from(localDate, hours, 0, 0, 0);
    }

    /**
     * <pre>
     * This method explicitly checks if the Date instance is a java.sql.Date or not
     * Please refer to "from(java.sql.Date)" method for reasons
     *
     * The assumption behind java.util.Date is that it was created using a long value, i.e.
     *      new java.util.Date(long);
     * </pre>
     * @throws NullPointerException if date is null
     */
    public TimeConverter from(java.util.Date date) {
        if (date instanceof java.sql.Date)
            return from((java.sql.Date) date);

        Null.check(date).ifAny("java.util.Date cannot be null");
        return new TimeConverter(timezone, date.toInstant(), null, null, null, null, date);
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
     * @throws NullPointerException if date is null
     */
    public TimeConverter from(java.sql.Date date) {
        Null.check(date).ifAny("java.sql.Date cannot be null");
        return from(date.toString());
    }

    /**
     * <pre>
     * This method expects the following date format:
     *      "yyyy-MM-dd"; for example: "2015-08-05"
     *
     * The instance created by this method will assume that the time was 00:00:00.000, using the handler's timezone
     * </pre>
     * @throws NullPointerException if dateString is null
     */
    public TimeConverter from(String dateString) {
        Null.check(dateString).ifAny("Date string cannot be null");
        return from(LocalDate.parse(dateString));
    }

    // CONSTRUCTORS

    public TimeHandler(ZoneId timezone) {
        this.timezone = timezone;
    }

    // PRIVATE

    private final ZoneId timezone;

    // OBJECT OVERRIDES

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeHandler)) return false;
        TimeHandler that = (TimeHandler) o;
        return Objects.equals(timezone, that.timezone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timezone);
    }

}
