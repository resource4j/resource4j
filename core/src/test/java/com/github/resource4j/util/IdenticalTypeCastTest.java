package com.github.resource4j.util;

import static com.github.resource4j.util.TypeConverter.convert;
import static java.util.Calendar.JANUARY;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeNotNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class IdenticalTypeCastTest {
	
	@Parameters(name = "\"{3}\" of {1} as {2}")
 	public static Iterable<Object[]> data() {
 		return Arrays.asList(new Object[][] {
 				{ (byte) 1, Byte.class, Byte.TYPE, "1" },
 				{ (short) 1, Short.class, Short.TYPE, "1" }, 
 				{ 1, Integer.class, Integer.TYPE, "1" }, 
 				{ 1L, Long.class, Long.TYPE, "1" },
                { 0.1f, Float.class, Float.TYPE, "0.1" }, 
                { 0.1, Double.class, Double.TYPE, "0.1" }, 
                { false, Boolean.class, Boolean.TYPE, "false" }, 
                { true, Boolean.class, Boolean.TYPE, "true" },
                { 'c', Character.class, Character.TYPE, "c" },
                { "a string", String.class, null, null },
                { new GregorianCalendar(2015, JANUARY, 1), GregorianCalendar.class, Calendar.class, "2015-01-01T00:00:00"},
                { new GregorianCalendar(2015, JANUARY, 1).getTime(), java.util.Date.class, null, "2015-01-01T00:00:00" },
                { new Object(), Object.class, null, null }
		});
     }

	
	private Object value;
	private Class<?> sameType;
	private Class<?> toType;
	private String string;

	public IdenticalTypeCastTest(Object value, Class<?> sameType, Class<?> toType, String string) {
		super();
		this.value = value;
		this.sameType = sameType;
		this.toType = toType;
		this.string = string;
	}

	@Test
	public void testConversionToSameType() {
		assertSame(value, convert(value, sameType));
	}
	
	@Test
	public void testConversionOfNullToType() {
		assertNull(convert(null, sameType));
	}

	@Test
	public void testConversionToPrimitiveOrSupertype() {
		assumeNotNull(toType);
		assertEquals(value, convert(value, toType));
	}

	@Test
	public void testConversionToString() {
		assumeNotNull(string);
		assertEquals(string, convert(value, String.class));
	}

	@Test
	public void testConversionFromString() {
		assumeNotNull(string);
		Object converted = convert(string, sameType);
		assertEquals(value, converted);
	}

}
