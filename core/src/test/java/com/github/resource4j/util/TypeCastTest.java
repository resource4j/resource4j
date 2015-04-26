package com.github.resource4j.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.Test;

import com.github.resource4j.util.TypeConverter;

public class TypeCastTest {

	@Test
	public void convertNullToAnything() throws ParseException {
		assertNull(TypeConverter.convert(null, Object.class));
	}
	
	@Test
	public void testParseIntInLocale() throws ParseException {
		Long value = TypeConverter.convert("3,000,000", Long.class, DecimalFormat.getNumberInstance(Locale.US));
		assertNotNull(value);
		assertEquals(3000000L, value.longValue());
	}
	
    @Test
    public void testParseLong() throws ParseException {
        Long value = TypeConverter.convert("12345", Long.TYPE);
        assertNotNull(value);
        assertEquals(12345L, value.longValue());
    }
    
    @Test
    public void testParseInt() throws ParseException {
        Integer value = TypeConverter.convert("12345", Integer.TYPE);
        assertNotNull(value);
        assertEquals(12345, value.intValue());
    }

    @Test
    public void testParseShort() throws ParseException {
        Short value = TypeConverter.convert("12345", Short.TYPE);
        assertNotNull(value);
        assertEquals((short) 12345, value.shortValue()); // NOPMD by igammel on 30.03.12 15:39
    }

    @Test
    public void testParseByte() throws ParseException {
        Byte value = TypeConverter.convert("123", Byte.TYPE);
        assertNotNull(value);
        assertEquals((byte) 123, value.byteValue());
    }

    @Test
    public void testParseDouble() throws ParseException {
        Double value = TypeConverter.convert("12345.67", Double.TYPE);
        assertNotNull(value);
        assertEquals(12345.67, value.doubleValue(), 0.00000001);
    }

    @Test
    public void testParseFloat() throws ParseException {
        Float value = TypeConverter.convert("12345.67", Float.TYPE);
        assertNotNull(value);
        assertEquals(12345.67f, value.floatValue(), 0.00000001);
    }
    
    @Test
    public void testParseBoolean() throws ParseException {
        Boolean value = TypeConverter.convert("true", Boolean.TYPE);
        assertNotNull(value);
        assertTrue(value.booleanValue());
    }
    
    @Test
    public void testParseChar() throws ParseException {
        Character value = TypeConverter.convert("y", Character.TYPE);
        assertNotNull(value);
        assertEquals('y', value.charValue());
    }

    @Test
    public void testParseDateAsCalendar() throws ParseException {
        Calendar expected = new GregorianCalendar(1997, Calendar.JULY, 16);
        Calendar date = TypeConverter.convert("1997-07-16", Calendar.class);
        assertEquals(expected, date);
    }

    @Test
    public void testParseDate() throws ParseException {
        Calendar expected = new GregorianCalendar(1997, Calendar.JULY, 16);
        Date date = TypeConverter.convert("1997-07-16", Date.class);
        assertEquals(expected.getTime(), date);
    }
    
    @Test
    public void testCastBooleanToPrimitive() throws ParseException {
        Boolean value = Boolean.TRUE;
        Boolean result = TypeConverter.convert(value, Boolean.TYPE);
        assertSame(value, result);
    }

    @Test
    public void testCastIntegerToPrimitive() throws ParseException {
        Integer value = Integer.valueOf(0);
        Integer result = TypeConverter.convert(value, Integer.TYPE);
        assertSame(value, result);
    }

    @Test
    public void testCastUtilDateToSQLDate() throws ParseException {
        GregorianCalendar calendar = new GregorianCalendar(2012, Calendar.JANUARY, 10);
        long millis = calendar.getTimeInMillis();
        java.util.Date value = new java.util.Date(millis);
        java.sql.Date result = TypeConverter.convert(value, java.sql.Date.class);
        assertEquals(new java.sql.Date(millis), result);
    }

    @Test
    public void testCastSQLDateToUtilDate() throws ParseException {
        GregorianCalendar calendar = new GregorianCalendar(2012, Calendar.JANUARY, 10);
        long millis = calendar.getTimeInMillis();
        java.sql.Date value = new java.sql.Date(millis);
        java.util.Date result = TypeConverter.convert(value, java.util.Date.class);
        assertEquals(new java.util.Date(millis), result);
    }

    @Test
    public void testCastSQLDateToCalendar() throws ParseException {
        GregorianCalendar calendar = new GregorianCalendar(2012, Calendar.JANUARY, 10);
        long millis = calendar.getTimeInMillis();
        java.sql.Date value = new java.sql.Date(millis);
        Calendar result = TypeConverter.convert(value, Calendar.class);
        assertEquals(calendar, result);
    }

    @Test
    public void testCastCalendarToSQLDate() throws ParseException {
        GregorianCalendar calendar = new GregorianCalendar(2012, Calendar.JANUARY, 10);
        long millis = calendar.getTimeInMillis();
        java.sql.Date value = new java.sql.Date(millis);
        Calendar result = TypeConverter.convert(value, Calendar.class);
        assertEquals(calendar, result);
    }
    
    @Test
    public void testConversionWithDateFormat() {
    	Calendar calendar = new GregorianCalendar(2014, Calendar.JULY, 26);
    	DateFormat format = new SimpleDateFormat("MMM d yyyy", Locale.US);
    	
    	String toString = TypeConverter.convert(calendar, String.class, format);
    	Calendar fromString = TypeConverter.convert(toString, Calendar.class, format);
    	assertEquals(calendar.get(Calendar.DATE), fromString.get(Calendar.DATE));
    	assertEquals(calendar.get(Calendar.MONTH), fromString.get(Calendar.MONTH));
    	assertEquals(calendar.get(Calendar.YEAR), fromString.get(Calendar.YEAR));
    }
    

}
