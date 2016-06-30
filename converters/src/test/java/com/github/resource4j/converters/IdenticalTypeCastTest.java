package com.github.resource4j.converters;

import static com.github.resource4j.converters.TypeConverter.convert;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeNotNull;

import java.lang.reflect.Array;
import java.time.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.function.BiFunction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class IdenticalTypeCastTest {

    @Parameters(name = "\"{3}\" of {1} as {2}")
 	public static Iterable<Object[]> data() {
 		return Arrays.asList(new Object[][] {
 				{ (byte) 1, Byte.class, Byte.TYPE, "1", null },
 				{ (short) 1, Short.class, Short.TYPE, "1", null },
 				{ 1, Integer.class, Integer.TYPE, "1", null },
 				{ 1L, Long.class, Long.TYPE, "1", null },
                { 0.1f, Float.class, Float.TYPE, "0.1", null },
                { 0.1, Double.class, Double.TYPE, "0.1", null },
                { false, Boolean.class, Boolean.TYPE, "false", null },
                { true, Boolean.class, Boolean.TYPE, "true", null },
                { 'c', Character.class, Character.TYPE, "c", null },
                { "a string", String.class, null, null, null },
                { calendar(), GregorianCalendar.class, Calendar.class, "2015-01-01T11:59:23", compare(
                        (Calendar c1, Calendar c2) -> c1.getTimeInMillis() == c2.getTimeInMillis()
                ) },
                { calendar().getTime(), java.util.Date.class, null, "2015-01-01T11:59:23", null },
                { Instant.from(time()), Instant.class, null, "2015-01-01T11:59:23Z", null },
                { time(), ZonedDateTime.class, null, "2015-01-01T11:59:23Z", null },
                { time().toLocalDateTime(), LocalDateTime.class, null, "2015-01-01T11:59:23", null },
                { time().toLocalDate(), LocalDate.class, null, "2015-01-01", null },
                { new Object(), Object.class, null, null, null },
		});
     }

    private static <T> BiFunction<T, T, Boolean> compare(BiFunction<T,T,Boolean> fn) {
        return fn;
    }

	private static Calendar calendar() {
        GregorianCalendar gc = GregorianCalendar.from(time());
        return gc;
	}

    private static ZonedDateTime time() {
        return ZonedDateTime.of(2015, 1, 1, 11, 59, 23, 0, ZoneOffset.UTC);
    }

	private Object value;
	private Class<?> sameType;
	private Class<?> toType;
	private String string;
    private final BiFunction<Object, Object, Boolean> comparator;

    public IdenticalTypeCastTest(Object value, Class<?> sameType, Class<?> toType, String string, BiFunction<Object,Object,Boolean> comparator) {
		super();
		this.value = value;
		this.sameType = sameType;
		this.toType = toType;
		this.string = string;
        this.comparator = comparator;
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
        assertEqualsByCompare(value, converted);
	}

    private void assertEqualsByCompare(Object expected, Object actual) {
        if (comparator == null) {
            assertEquals(expected, actual);
        } else {
            assertTrue(comparator.apply(expected, actual));
        }
    }

	@Test
	public void testConversionFromStringArray() {
		assumeNotNull(string);
		StringBuilder origin = new StringBuilder();
        Object array = Array.newInstance(sameType, 10);
        for (int i = 0; i < 10; i++) {
			if (i > 0) origin.append(',');
			origin.append(string);
            Array.set(array, i, value);
		}
		Object converted = convert(origin.toString(), array.getClass());
        assertEquals(array.getClass(), converted.getClass());
        assertEquals(10, Array.getLength(converted));
        for (int i = 0; i < 10; i++) {
            assertEqualsByCompare(Array.get(array, i), Array.get(converted, i));
        }
	}

}
