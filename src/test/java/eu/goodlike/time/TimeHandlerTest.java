package eu.goodlike.time;

import org.junit.Before;
import org.junit.Test;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeHandlerTest {

    private final ZoneId UTC = ZoneId.of("UTC");
    private final String date = "2015-10-16";
    private final LocalDate localDate = LocalDate.parse(date);
    private final ZonedDateTime zonedDateTime = localDate.atStartOfDay(UTC);
    private final LocalTime localTime = zonedDateTime.toLocalTime();
    private final Instant instant = zonedDateTime.toInstant();
    private final long timestamp = instant.toEpochMilli();


    private TimeHandler timeHandler;

    @Before
    public void setup() {
        timeHandler = new TimeHandler(UTC);
    }

    @Test
    public void tryConverterFromInstant_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(instant))
                .isEqualTo(new TimeConverter(UTC, instant, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromMillis_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(timestamp))
                .isEqualTo(new TimeConverter(UTC, instant, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromZonedDateTime_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(instant.atZone(UTC)))
                .isEqualTo(new TimeConverter(UTC, instant, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromLocalDate_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(localDate))
                .isEqualTo(new TimeConverter(UTC, instant, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromLocalDateAndTime_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(localDate, localTime))
                .isEqualTo(new TimeConverter(UTC, instant, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromLocalDateHoursMinutesSecondsMillis_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(localDate, 0, 0, 0, 0))
                .isEqualTo(new TimeConverter(UTC, instant, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromLocalDateHoursMinutesSeconds_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(localDate, 0, 0, 0))
                .isEqualTo(new TimeConverter(UTC, instant, null, null, null, null, null));
    }
    @Test
    public void tryConverterFromLocalDateHoursMinutes_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(localDate, 0, 0))
                .isEqualTo(new TimeConverter(UTC, instant, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromLocalDateHours_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(localDate, 0))
                .isEqualTo(new TimeConverter(UTC, instant, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromUtilDate_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(new java.util.Date(timestamp)))
                .isEqualTo(new TimeConverter(UTC, instant, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromSqlDate_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(java.sql.Date.valueOf(date)))
                .isEqualTo(new TimeConverter(UTC, instant, null, null, null, null, null));
    }

    @Test
    public void tryConverterFromStringDate_shouldBeEqualToConverterOfSameTimezone() {
        assertThat(timeHandler.from(date))
                .isEqualTo(new TimeConverter(UTC, instant, null, null, null, null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryTooManyHours_shouldThrowIllegalArgument() {
        timeHandler.from(localDate, 24);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryTooManyMinutes_shouldThrowIllegalArgument() {
        timeHandler.from(localDate, 23, 60);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryTooManySeconds_shouldThrowIllegalArgument() {
        timeHandler.from(localDate, 23, 59, 60);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryTooManyMilliseconds_shouldThrowIllegalArgument() {
        timeHandler.from(localDate, 23, 59, 59, 1000);
    }

    @Test
    public void tryJustEnoughManyHours_shouldPass() {
        timeHandler.from(localDate, 23);
    }

    @Test
    public void tryJustEnoughMinutes_shouldPass() {
        timeHandler.from(localDate, 23, 59);
    }

    @Test
    public void tryJustEnoughSeconds_shouldPass() {
        timeHandler.from(localDate, 23, 59, 59);
    }

    @Test
    public void tryJustEnoughMilliseconds_shouldPass() {
        timeHandler.from(localDate, 23, 59, 59, 999);
    }

}
