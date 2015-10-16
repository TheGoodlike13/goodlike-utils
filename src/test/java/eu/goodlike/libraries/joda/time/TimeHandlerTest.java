package eu.goodlike.libraries.joda.time;

import org.joda.time.*;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeHandlerTest {

    private final DateTimeZone UTC = DateTimeZone.forID("UTC");
    private final String date = "2015-10-16";
    private final LocalDate localDate = LocalDate.parse(date);
    private final LocalTime localTime = localDate.toDateTimeAtStartOfDay(UTC).toLocalTime();
    private final long timestamp = LocalDate.parse(date).toDateTimeAtStartOfDay(UTC).getMillis();

    private TimeHandler timeHandler;

    @Before
    public void setup() {
        timeHandler = new TimeHandler(UTC);
    }

    @Test
    public void tryConverterFromMillis_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(timestamp))
                .isEqualTo(new TimeConverter(UTC, timestamp, null, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromInstant_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(new Instant(timestamp)))
                .isEqualTo(new TimeConverter(UTC, timestamp, null, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromDateTime_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(new DateTime(timestamp)))
                .isEqualTo(new TimeConverter(UTC, timestamp, null, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromLocalDate_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(LocalDate.parse(date)))
                .isEqualTo(new TimeConverter(UTC, timestamp, null, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromLocalDateAndTime_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(localDate, localTime))
                .isEqualTo(new TimeConverter(UTC, timestamp, null, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromLocalDateHoursMinutesSecondsMillis_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(localDate, 0, 0, 0, 0))
                .isEqualTo(new TimeConverter(UTC, timestamp, null, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromLocalDateHoursMinutesSeconds_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(localDate, 0, 0, 0))
                .isEqualTo(new TimeConverter(UTC, timestamp, null, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromLocalDateHoursMinutes_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(localDate, 0, 0))
                .isEqualTo(new TimeConverter(UTC, timestamp, null, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromLocalDateHours_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(localDate, 0))
                .isEqualTo(new TimeConverter(UTC, timestamp, null, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromJavaInstant_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(java.time.Instant.ofEpochMilli(timestamp)))
                .isEqualTo(new TimeConverter(UTC, timestamp, null, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromJavaUtilDate_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(new java.util.Date(timestamp)))
                .isEqualTo(new TimeConverter(UTC, timestamp, null, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromJavaSqlDate_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(java.sql.Date.valueOf(date)))
                .isEqualTo(new TimeConverter(UTC, timestamp, null, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromString_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(date))
                .isEqualTo(new TimeConverter(UTC, timestamp, null, null, null, null, null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryTooManyHours_shouldThrowIllegalArgument() {
        timeHandler.from(localDate, 25);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryTooManyMinutes_shouldThrowIllegalArgument() {
        timeHandler.from(localDate, 23, 65);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryTooManySeconds_shouldThrowIllegalArgument() {
        timeHandler.from(localDate, 23, 59, 65);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryTooManyMilliseconds_shouldThrowIllegalArgument() {
        timeHandler.from(localDate, 23, 59, 59, 1500);
    }

}
