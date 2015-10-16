package eu.goodlike.libraries.joda.time;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeTest {

    private final DateTimeZone UTC = DateTimeZone.forID("UTC");
    private final LocalDate localDate = LocalDate.parse("2015-10-16");

    @Test
    public void tryDefaultZone_shouldBeUTC() {
        assertThat(Time.defaultTimeZone()).isEqualTo(UTC);
    }

    @Test
    public void tryDefaultHandler_shouldBeHandlerForUTC() {
        assertThat(Time.getDefault()).isEqualTo(Time.forZone(UTC));
    }

    @Test
    public void tryForString_shouldBeHandlerForZone() {
        assertThat(Time.forZoneId("UTC")).isEqualTo(Time.forZone(UTC));
    }

    @Test
    public void tryForJavaZone_shouldBeHandlerForZone() {
        TimeZone TZ_UTC = TimeZone.getTimeZone("UTC");
        assertThat(Time.forZone(TZ_UTC)).isEqualTo(Time.forZone(UTC));
    }

    @Test
    public void tryForZone_shouldBeEqualToNewHandler() {
        assertThat(Time.forZone(UTC)).isEqualTo(new TimeHandler(UTC));
    }

    @Test
    public void tryForZoneTwice_shouldBeSame() {
        assertThat(Time.forZone(UTC)).isSameAs(Time.forZone(UTC));
    }

    @Test
    public void tryConverterForLocalDate_shouldBeConverterForDateAtUTC() {
        assertThat(Time.convert(localDate)).isEqualTo(new TimeHandler(UTC).from(localDate));
    }

    @Test
    public void tryConverterForSqlDate_shouldBeConverterForDateAtUTC() {
        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate.toString());
        assertThat(Time.convert(sqlDate)).isEqualTo(new TimeHandler(UTC).from(sqlDate));
    }

    @Test
    public void tryConverterForTimestamp_shouldBeConverterForDateAtUTC() {
        long timestamp = localDate.toDateTimeAtStartOfDay(UTC).getMillis();
        assertThat(Time.convert(timestamp)).isEqualTo(new TimeHandler(UTC).from(timestamp));
    }

}
