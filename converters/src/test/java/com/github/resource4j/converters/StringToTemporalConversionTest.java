package com.github.resource4j.converters;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

public class StringToTemporalConversionTest {

    @Test
    public void testStringToLocalTime() {
        LocalTime result = TypeConverter.convert("10:15:30", LocalTime.class, "HH:mm:ss");
        assertEquals(LocalTime.of(10, 15, 30), result);
    }

    @Test
    public void testStringToYear() {
        Year result = TypeConverter.convert("2025", Year.class, "yyyy");
        assertEquals(Year.of(2025), result);
    }

    @Test
    public void testStringToYearMonth() {
        YearMonth result = TypeConverter.convert("2025-03", YearMonth.class, "yyyy-MM");
        assertEquals(YearMonth.of(2025, 3), result);
    }

    @Test
    public void testStringToLocalDateWithCustomPattern() {
        LocalDate result = TypeConverter.convert("01/15/2025", LocalDate.class, "MM/dd/yyyy");
        assertEquals(LocalDate.of(2025, 1, 15), result);
    }

    @Test
    public void testStringToLocalDateWithDefaultFormat() {
        LocalDate result = TypeConverter.convert("2025-03-15", LocalDate.class);
        assertEquals(LocalDate.of(2025, 3, 15), result);
    }

    @Test
    public void testStringToLocalDateTime() {
        LocalDateTime result = TypeConverter.convert("2025-01-01T11:59:23", LocalDateTime.class);
        assertEquals(LocalDateTime.of(2025, 1, 1, 11, 59, 23), result);
    }

    @Test
    public void testInvalidFormatThrows() {
        // StringToTemporalConversion wraps parse errors in TypeCastException,
        // but the exception propagates from the catch block
        assertThrows(Exception.class,
                () -> TypeConverter.convert("not-a-date", LocalDate.class));
    }

}
