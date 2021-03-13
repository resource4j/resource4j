package com.github.resource4j.converters;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TypeCastTest {

	@Test
	public void convertNullToAnything() {
		assertNull(TypeConverter.convert(null, Object.class));
	}
	
	@Test
	public void testParseIntInLocale() {
		Long value = TypeConverter.convert("3,000,000", Long.class, DecimalFormat.getNumberInstance(Locale.US));
		assertNotNull(value);
		assertEquals(3000000L, value.longValue());
	}
	
    @Test
    public void testParseLong() {
        Long value = TypeConverter.convert("12345", Long.TYPE);
        assertNotNull(value);
        assertEquals(12345L, value.longValue());
    }
    
    @Test
    public void testParseInt() {
        Integer value = TypeConverter.convert("12345", Integer.TYPE);
        assertNotNull(value);
        assertEquals(12345, value.intValue());
    }

    @Test
    public void testParseShort() {
        Short value = TypeConverter.convert("12345", Short.TYPE);
        assertNotNull(value);
        assertEquals((short) 12345, value.shortValue()); // NOPMD by igammel whenRequested 30.03.12 15:39
    }

    @Test
    public void testParseByte() {
        Byte value = TypeConverter.convert("123", Byte.TYPE);
        assertNotNull(value);
        assertEquals((byte) 123, value.byteValue());
    }

    @Test
    public void testParseDouble() {
        Double value = TypeConverter.convert("12345.67", Double.TYPE);
        assertNotNull(value);
        assertEquals(12345.67, value, 0.00000001);
    }

    @Test
    public void testParseFloat() {
        Float value = TypeConverter.convert("12345.67", Float.TYPE);
        assertNotNull(value);
        assertEquals(12345.67f, value, 0.00000001);
    }
    
    @Test
    public void testParseBoolean() {
        Boolean value = TypeConverter.convert("true", Boolean.TYPE);
        assertNotNull(value);
        assertTrue(value);
    }
    
    @Test
    public void testParseChar() {
        Character value = TypeConverter.convert("y", Character.TYPE);
        assertNotNull(value);
        assertEquals('y', value.charValue());
    }

    @Test
    public void testParseDate() {
        Calendar expected = new GregorianCalendar(1997, Calendar.JULY, 16);
        Date date = TypeConverter.convert("1997-07-16", Date.class, new SimpleDateFormat("yyyy-MM-dd"));
        assertEquals(expected.getTime(), date);
    }
    
    @Test
    public void testCastBooleanToPrimitive() {
        Boolean result = TypeConverter.convert(Boolean.TRUE, Boolean.TYPE);
        assertSame(Boolean.TRUE, result);
    }

    @Test
    public void testCastIntegerToPrimitive() {
        Integer value = 0;
        Integer result = TypeConverter.convert(value, Integer.TYPE);
        assertSame(value, result);
    }

    @Test
    public void testCastUtilDateToSQLDate() {
        GregorianCalendar calendar = new GregorianCalendar(2012, Calendar.JANUARY, 10);
        long millis = calendar.getTimeInMillis();
        java.util.Date value = new java.util.Date(millis);
        java.sql.Date result = TypeConverter.convert(value, java.sql.Date.class);
        assertEquals(new java.sql.Date(millis), result);
    }

    @Test
    public void testCastSQLDateToUtilDate() {
        GregorianCalendar calendar = new GregorianCalendar(2012, Calendar.JANUARY, 10);
        long millis = calendar.getTimeInMillis();
        java.sql.Date value = new java.sql.Date(millis);
        java.util.Date result = TypeConverter.convert(value, java.util.Date.class);
        assertEquals(new java.util.Date(millis), result);
    }

    @Test
    public void testCastSQLDateToCalendar() {
        GregorianCalendar calendar = new GregorianCalendar(2012, Calendar.JANUARY, 10);
        long millis = calendar.getTimeInMillis();
        java.sql.Date value = new java.sql.Date(millis);
        Calendar result = TypeConverter.convert(value, Calendar.class);
        assertEquals(calendar.getTimeInMillis(), result.getTimeInMillis());
    }

    @Test
    public void testCastCalendarToUtilDate() {
        GregorianCalendar calendar = new GregorianCalendar(2012, Calendar.JANUARY, 10);
        long millis = calendar.getTimeInMillis();
        java.util.Date value = new java.util.Date(millis);
        java.util.Date result = TypeConverter.convert(calendar, Date.class);
        assertEquals(value, result);
    }

    @Test
    public void testCastCalendarToSQLDate() {
        GregorianCalendar calendar = new GregorianCalendar(2012, Calendar.JANUARY, 10);
        long millis = calendar.getTimeInMillis();
        java.sql.Date value = new java.sql.Date(millis);
        Calendar result = TypeConverter.convert(value, Calendar.class);
        assertEquals(calendar.getTimeInMillis(), result.getTimeInMillis());
    }
    
    @Test
    public void testConversionWithDateFormat() {
    	Calendar calendar = new GregorianCalendar(2014, Calendar.JULY, 26);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

    	DateFormat format = new SimpleDateFormat("MMM d yyyy", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
    	
    	String toString = TypeConverter.convert(calendar, String.class, format);
    	Calendar fromString = TypeConverter.convert(toString, Calendar.class, format);

    	assertEquals(calendar.get(Calendar.DATE), fromString.get(Calendar.DATE));
    	assertEquals(calendar.get(Calendar.MONTH), fromString.get(Calendar.MONTH));
    	assertEquals(calendar.get(Calendar.YEAR), fromString.get(Calendar.YEAR));
    }
    

}
