package eu.goodlike.time;

import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeTest {

    private final ZoneId UTC = ZoneId.of("UTC");
    private final LocalDate localDate = LocalDate.parse("2015-10-16");

    @Test
    public void tryUTC_shouldBeUTC() {
        assertThat(Time.UTC()).isEqualTo(UTC);
    }

    @Test
    public void tryAtUTC_shouldBeHandlerOfUTC() {
        assertThat(Time.atUTC()).isEqualTo(Time.at(UTC));
    }

    @Test
    public void tryAtZoneString_shouldBeHandlerOfZone() {
        assertThat(Time.at("UTC")).isEqualTo(Time.at(UTC));
    }

    @Test
    public void tryAtZone_shouldBeEqualToNewHandler() {
        assertThat(Time.at(UTC)).isEqualTo(new TimeHandler(UTC));
    }

    @Test
    public void tryTwice_shouldReturnSameHandler() {
        assertThat(Time.at(UTC)).isSameAs(Time.at(UTC));
    }

    @Test
    public void tryConverterForLocalDate_shouldBeConverterAtUTCForDate() {
        assertThat(Time.convert(localDate)).isEqualTo(Time.atUTC().from(localDate));
    }

    @Test
    public void tryConverterForSqlDate_shouldBeConverterAtUTCForDate() {
        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate.toString());
        assertThat(Time.convert(sqlDate)).isEqualTo(Time.atUTC().from(sqlDate));
    }

    @Test
    public void tryConverterForTimestamp_shouldBeConverterAtUTCForTimestamp() {
        long timestamp = localDate.atStartOfDay(UTC).toInstant().toEpochMilli();
        assertThat(Time.convert(timestamp)).isEqualTo(Time.atUTC().from(timestamp));
    }

}
