package eu.goodlike.time;

import org.junit.Before;
import org.junit.Test;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeConverterTest {

    private final ZoneId UTC = ZoneId.of("UTC");
    private final String date = "2015-10-16";
    private final LocalDate localDate = LocalDate.parse(date);
    private final ZonedDateTime zonedDateTime = localDate.atStartOfDay(UTC);
    private final LocalTime localTime = zonedDateTime.toLocalTime();
    private final Instant instant = zonedDateTime.toInstant();
    private final long timestamp = instant.toEpochMilli();

    private TimeConverter timeConverter;

    @Before
    public void setup() {
        timeConverter = new TimeConverter(UTC, instant, null, null, null, null, null);
    }

    @Test
    public void tryToInstant_shouldReturnInstant() {
        assertThat(timeConverter.toInstant()).isEqualTo(instant);
    }

    @Test
    public void tryToMillis_shouldReturnMillis() {
        assertThat(timeConverter.toEpochMilli()).isEqualTo(timestamp);
    }

    @Test
    public void tryToZonedDateTime_shouldReturnZonedDateTime() {
        assertThat(timeConverter.toDateTime()).isEqualTo(zonedDateTime);
    }

    @Test
    public void tryToLocalDate_shouldReturnLocalDate() {
        assertThat(timeConverter.toLocalDate()).isEqualTo(localDate);
    }

    @Test
    public void tryToLocalTime_shouldReturnLocalTime() {
        assertThat(timeConverter.toLocalTime()).isEqualTo(localTime);
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
    public void tryToUtilDate_shouldReturnUtilDate() {
        assertThat(timeConverter.toJavaDate()).isEqualTo(new java.util.Date(timestamp));
    }

    @Test
    public void tryToSqlDate_shouldReturnSqlDate() {
        assertThat(timeConverter.toSqlDate()).isEqualTo(java.sql.Date.valueOf(date));
    }

}
