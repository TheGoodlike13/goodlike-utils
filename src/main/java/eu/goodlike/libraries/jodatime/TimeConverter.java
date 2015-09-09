package eu.goodlike.libraries.jodatime;

import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;

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
 * Uses epoch milliseconds as the base for converting; uses ISOChronology for given timezone;
 *
 * When converting to and from LocalDate/LocalTime/SqlDate assumes time 00:00:00.000
 *
 * This class is not thread safe and should not be cached; it only calculates the appropriate values
 * when needed and stores them for further computations
 * </pre>
 */
public final class TimeConverter implements DateConverter {

    public long toEpochMillis() {
        return epochMillis;
    }

    public Instant toJodaInstant() {
        if (instant == null)
            instant = new Instant(epochMillis);

        return instant;
    }

    public DateTime toJodaDateTime() {
        if (dateTime == null)
            dateTime = new DateTime(epochMillis, chronology);

        return dateTime;
    }

    @Override
    public LocalDate toJodaLocalDate() {
        if (localDate == null)
            localDate = new LocalDate(epochMillis, chronology);

        return localDate;
    }

    public LocalTime toJodaLocalTime() {
        if (localTime == null)
            localTime = new LocalTime(epochMillis, chronology);

        return localTime;
    }

    public int toHours() {
        return toJodaLocalTime().getHourOfDay();
    }

    public int toMinutes() {
        return toJodaLocalTime().getMinuteOfHour();
    }

    public int toSeconds() {
        return toJodaLocalTime().getSecondOfMinute();
    }

    public int toMilliseconds() {
        return toJodaLocalTime().getMillisOfSecond();
    }

    public java.time.Instant toJavaInstant() {
        if (javaInstant == null)
            javaInstant = java.time.Instant.ofEpochMilli(epochMillis);

        return javaInstant;
    }

    /**
     * <pre>
     * The assumption behind java.util.Date is that it will be consumed using a long value, i.e.
     *      date.getTime();
     * </pre>
     */
    public java.util.Date toJavaDate() {
        if (javaDate == null)
            javaDate = new java.util.Date(epochMillis);

        return javaDate;
    }

    /**
     * <pre>
     * The assumption behind java.sql.Date is that it will be consumed using a String value, i.e.
     *      date.toString():
     * This uses the JAVA timezone to create a localized instance; therefore, the epoch milliseconds value
     * refers to the start of day at JAVA timezone, and the String value of the Date can get altered during
     * conversion; using toString() method, however, once again uses JAVA timezone, essentially allowing the value
     * to remain correct
     * </pre>
     */
    @Override
    public java.sql.Date toSqlDate() {
        if (sqlDate == null)
            sqlDate = java.sql.Date.valueOf(toJodaLocalDate().toString());

        return sqlDate;
    }

    // CONSTRUCTORS

    public TimeConverter(DateTimeZone timeZone, long epochMillis, Instant instant, DateTime dateTime, LocalDate localDate,
                              LocalTime localTime, java.time.Instant javaInstant, java.util.Date javaDate) {
        this.chronology = ISOChronology.getInstance(timeZone);
        this.epochMillis = epochMillis;
        this.instant = instant;
        this.dateTime = dateTime;
        this.localDate = localDate;
        this.localTime = localTime;
        this.javaInstant = javaInstant;
        this.javaDate = javaDate;
    }

    // PRIVATE

    private final Chronology chronology;
    private final long epochMillis;
    private Instant instant;
    private DateTime dateTime;
    private LocalDate localDate;
    private LocalTime localTime;
    private java.time.Instant javaInstant;
    private java.util.Date javaDate;
    private java.sql.Date sqlDate;

}
