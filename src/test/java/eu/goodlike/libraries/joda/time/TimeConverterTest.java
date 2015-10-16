package eu.goodlike.libraries.joda.time;

import org.joda.time.*;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeConverterTest {

    private final DateTimeZone UTC = DateTimeZone.forID("UTC");
    private final String date = "2015-10-16";
    private final LocalDate localDate = LocalDate.parse(date);
    private final LocalTime localTime = localDate.toDateTimeAtStartOfDay(UTC).toLocalTime();
    private final long timestamp = LocalDate.parse(date).toDateTimeAtStartOfDay(UTC).getMillis();

    private TimeConverter timeConverter;

    @Before
    public void setup() {
        timeConverter = new TimeConverter(UTC, timestamp, null, null, null, null, null, null);
    }

    @Test
    public void tryToEpochMillis_shouldReturnMillis() {
        assertThat(timeConverter.toEpochMillis()).isEqualTo(timestamp);
    }

    @Test
    public void tryJodaInstant_shouldReturnJodaInstant() {
        assertThat(timeConverter.toJodaInstant()).isEqualTo(new Instant(timestamp));
    }

    @Test
    public void tryToJodaDateTime_shouldReturnJodaDateTimeAtUTC() {
        assertThat(timeConverter.toJodaDateTime()).isEqualTo(new DateTime(timestamp, UTC));
    }

    @Test
    public void tryToJodaLocalDate_shouldReturnJodaLocalDate() {
        assertThat(timeConverter.toJodaLocalDate()).isEqualTo(localDate);
    }

    @Test
    public void tryToJodaLocalTime_shouldReturnJodaLocalTime() {
        assertThat(timeConverter.toJodaLocalTime()).isEqualTo(localTime);
    }

    @Test
    public void tryToHours_shouldReturn0() {
        assertThat(timeConverter.toHours()).isEqualTo(0);
    }

    @Test
    public void tryToMinutes_shouldReturn0() {
        assertThat(timeConverter.toMinutes()).isEqualTo(0);
    }

    @Test
    public void tryToSeconds_shouldReturn0() {
        assertThat(timeConverter.toSeconds()).isEqualTo(0);
    }

    @Test
    public void tryToMilliseconds_shouldReturn0() {
        assertThat(timeConverter.toMilliseconds()).isEqualTo(0);
    }

    @Test
    public void tryToJavaInstant_shouldReturnJavaInstant() {
        assertThat(timeConverter.toJavaInstant()).isEqualTo(java.time.Instant.ofEpochMilli(timestamp));
    }

    @Test
    public void tryToJavaDate_shouldReturnJavaDate() {
        assertThat(timeConverter.toJavaDate()).isEqualTo(new java.util.Date(timestamp));
    }

    @Test
    public void tryToSqlDate_shouldReturnSqlDate() {
        assertThat(timeConverter.toSqlDate()).isEqualTo(java.sql.Date.valueOf(date));
    }

}
