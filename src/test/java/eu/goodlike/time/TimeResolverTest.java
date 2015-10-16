package eu.goodlike.time;

import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeResolverTest {

    private final ZoneId UTC = ZoneId.of("UTC");
    private final String date = "2015-10-16";
    private final LocalDate localDate = LocalDate.parse(date);

    @Test
    public void tryFromSameDay_shouldResolveIntoWholeDay() {
        assertThat(TimeResolver.from(Time.atUTC(), localDate, localDate))
                .isEqualTo(TimeResolver.from(Time.atUTC().from(localDate).toEpochMilli(),
                        Time.atUTC().from(localDate.plusDays(1)).toEpochMilli()));
    }

    @Test
    public void tryFromSameDayWithZoneId_shouldResolveIntoSameDayWithHandler() {
        assertThat(TimeResolver.from(UTC, localDate, localDate))
                .isEqualTo(TimeResolver.from(Time.atUTC(), localDate, localDate));
    }

    @Test
    public void tryWithNullTimezone_shouldUseUTC() {
        assertThat(TimeResolver.from(null, date, date)).isEqualTo(TimeResolver.from("UTC", date, date));
    }

    @Test
    public void tryWithNullStartDate_shouldUseToday() {
        String date = LocalDate.now(UTC).toString();
        assertThat(TimeResolver.from("UTC", null, date)).isEqualTo(TimeResolver.from("UTC", date, date));
    }

    @Test
    public void tryWithNullEndDate_shouldUseStartDate() {
        assertThat(TimeResolver.from("UTC", date, null)).isEqualTo(TimeResolver.from("UTC", date, date));
    }

    @Test
    public void tryWithNullStartAndEndTimes_shouldResolveToDates() {
        assertThat(TimeResolver.from("UTC", date, date, null, null)).isEqualTo(TimeResolver.from("UTC", date, date));
    }

    @Test
    public void tryWithNonNullStartAndEndTimes_shouldIgnoreDates() {
        long start = 0;
        long end = 1;
        assertThat(TimeResolver.from("UTC", date, date, start, end)).isEqualTo(TimeResolver.from(start, end));
    }

}
