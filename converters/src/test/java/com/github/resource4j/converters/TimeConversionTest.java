package com.github.resource4j.converters;

import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TimeConversionTest {

    // --- LocalTime <-> SQL Time roundtrip ---

    @Test
    public void testLocalTimeToSQLTimeRoundtrip() {
        LocalTime original = LocalTime.of(14, 30, 45);
        Time sqlTime = TypeConverter.convert(original, Time.class);
        LocalTime result = TypeConverter.convert(sqlTime, LocalTime.class);
        assertEquals(original, result);
    }

    @Test
    public void testMidnightRoundtrip() {
        LocalTime midnight = LocalTime.MIDNIGHT;
        Time sqlTime = TypeConverter.convert(midnight, Time.class);
        LocalTime result = TypeConverter.convert(sqlTime, LocalTime.class);
        assertEquals(midnight, result);
    }

    @Test
    public void testNoonRoundtrip() {
        LocalTime noon = LocalTime.NOON;
        Time sqlTime = TypeConverter.convert(noon, Time.class);
        LocalTime result = TypeConverter.convert(sqlTime, LocalTime.class);
        assertEquals(noon, result);
    }

    // --- DateToCalendar timezone handling ---

    @Test
    public void testDateToCalendarWithTimezone() {
        Date date = new Date(1420070400000L); // 2015-01-01T00:00:00 UTC
        Calendar result = TypeConverter.convert(date, Calendar.class, "America/New_York");
        assertEquals(TimeZone.getTimeZone("America/New_York"), result.getTimeZone());
        assertEquals(date.getTime(), result.getTimeInMillis());
    }

    @Test
    public void testDateToCalendarWithNullPatternDefaultsToUTC() {
        Date date = new Date(1420070400000L);
        Calendar result = TypeConverter.convert(date, Calendar.class, (String) null);
        assertEquals(TimeZone.getTimeZone("UTC"), result.getTimeZone());
    }

    // --- DateToString format handling ---

    @Test
    public void testDateToStringWithCustomPattern() {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        cal.set(2015, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        String result = TypeConverter.convert(date, String.class, "yyyy-MM-dd");
        assertEquals("2015-01-01", result);
    }

    @Test
    public void testDateToStringWithDateFormat() {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        cal.set(2015, Calendar.JULY, 26, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        DateFormat format = new SimpleDateFormat("MMM d yyyy", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String result = TypeConverter.convert(date, String.class, format);
        assertEquals("Jul 26 2015", result);
    }

    @Test
    public void testDateToStringWithNullFormatUsesDefault() {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        cal.set(2015, Calendar.JANUARY, 1, 11, 59, 23);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        String result = TypeConverter.convert(date, String.class, (String) null);
        assertEquals("2015-01-01T11:59:23", result);
    }

}
