package com.github.resource4j.converters;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class LocalDateToDateConversionsTest {

    @Test
    public void testConvertLocalDateToUtilDateAndBack() {
        LocalDate date = LocalDate.of(2016, Month.FEBRUARY, 25);
        Date utilDate = TypeConverter.convert(date, Date.class);
        LocalDate result = TypeConverter.convert(utilDate, LocalDate.class);
        assertEquals(date, result);
    }

    @Test
    public void testConvertLocalDateToUtilDateAndBackWithCustomTimeZone() {
        LocalDate date = LocalDate.of(2016, Month.FEBRUARY, 25);
        Date utilDate = TypeConverter.convert(date, Date.class, "America/Los_Angeles");
        LocalDate result = TypeConverter.convert(utilDate, LocalDate.class, "America/Los_Angeles");
        assertEquals(date, result);
    }

    @Test
    public void testConvertLocalDateToSQLDateAndBack() {
        LocalDate date = LocalDate.of(2016, Month.FEBRUARY, 25);
        java.sql.Date sqlDate = TypeConverter.convert(date, java.sql.Date.class);
        LocalDate result = TypeConverter.convert(sqlDate, LocalDate.class);
        assertEquals(date, result);
    }


    @Test
    public void testConvertLocalDateToSQLDateAndBackWithCustomTimeZone() {
        LocalDate date = LocalDate.of(2016, Month.FEBRUARY, 25);
        java.sql.Date sqlDate = TypeConverter.convert(date, java.sql.Date.class, "America/Los_Angeles");
        LocalDate result = TypeConverter.convert(sqlDate, LocalDate.class, "America/Los_Angeles");
        assertEquals(date, result);
    }

}
