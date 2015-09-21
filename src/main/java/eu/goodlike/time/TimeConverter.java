package eu.goodlike.time;

import java.time.*;
import java.util.Date;

import static java.time.temporal.ChronoField.MILLI_OF_SECOND;

public final class TimeConverter implements DateConverter {

    public Instant toInstant() {
        return instant;
    }

    public long toEpochMilli() {
        if (epochMillis == null)
            epochMillis = instant.toEpochMilli();

        return epochMillis;
    }

    public ZonedDateTime toDateTime() {
        if (zonedDateTime == null)
            zonedDateTime = instant.atZone(timezone);

        return zonedDateTime;
    }

    @Override
    public LocalDate toLocalDate() {
        if (localDate == null)
            localDate = toDateTime().toLocalDate();

        return localDate;
    }

    public LocalTime toLocalTime() {
        if (localTime == null)
            localTime = toDateTime().toLocalTime();

        return localTime;
    }

    public int toHours() {
        return toLocalTime().getHour();
    }

    public int toMinutes() {
        return toLocalTime().getMinute();
    }

    public int toSeconds() {
        return toLocalTime().getSecond();
    }

    public int toMilliseconds() {
        return (int)toLocalTime().getLong(MILLI_OF_SECOND);
    }

    /**
     * <pre>
     * The assumption behind java.util.Date is that it will be consumed using a long value, i.e.
     *      date.getTime();
     * </pre>
     */
    public java.util.Date toJavaDate() {
        if (utilDate == null)
            utilDate = Date.from(instant);

        return utilDate;
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
            sqlDate = java.sql.Date.valueOf(toLocalDate().toString());

        return sqlDate;
    }

    // CONSTRUCTORS

    public TimeConverter(ZoneId timezone, Instant instant, Long epochMillis, ZonedDateTime zonedDateTime,
                         LocalDate localDate, LocalTime localTime, java.util.Date utilDate) {
        this.timezone = timezone;
        this.instant = instant;
        this.epochMillis = epochMillis;
        this.zonedDateTime = zonedDateTime;
        this.localDate = localDate;
        this.localTime = localTime;
        this.utilDate = utilDate;
    }

    // PRIVATE

    private final ZoneId timezone;
    private final Instant instant;
    private Long epochMillis;
    private ZonedDateTime zonedDateTime;
    private LocalDate localDate;
    private LocalTime localTime;
    private java.util.Date utilDate;
    private java.sql.Date sqlDate;

}
