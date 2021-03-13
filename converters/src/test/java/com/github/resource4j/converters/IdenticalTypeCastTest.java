package com.github.resource4j.converters;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Array;
import java.time.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static com.github.resource4j.converters.TypeConverter.convert;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class IdenticalTypeCastTest {

 	public static Stream<Arguments> parameters() {
 		return Stream.of(new Object[][] {
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
		}).map(Arguments::of);
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

	@ParameterizedTest
	@MethodSource("parameters")
	public void testConversionToSameType(
			Object value,
			Class<?> sameType,
			Class<?> toType,
			String string,
			BiFunction<Object, Object, Boolean> comparator) {
		assertSame(value, convert(value, sameType));
	}

	@ParameterizedTest
	@MethodSource("parameters")
	public void testConversionOfNullToType(
			Object value,
			Class<?> sameType,
			Class<?> toType,
			String string,
			BiFunction<Object, Object, Boolean> comparator) {
		assertNull(convert(null, sameType));
	}

	@ParameterizedTest
	@MethodSource("parameters")
	public void testConversionToPrimitiveOrSupertype(
			Object value,
			Class<?> sameType,
			Class<?> toType,
			String string,
			BiFunction<Object, Object, Boolean> comparator) {
		assumeTrue(toType != null);
		assertEquals(value, convert(value, toType));
	}

	@ParameterizedTest
	@MethodSource("parameters")
	public void testConversionToString(
			Object value,
			Class<?> sameType,
			Class<?> toType,
			String string,
			BiFunction<Object, Object, Boolean> comparator) {
		assumeTrue(string != null);
		assertEquals(string, convert(value, String.class));
	}

	@ParameterizedTest
	@MethodSource("parameters")
	public void testConversionFromString(
			Object value,
			Class<?> sameType,
			Class<?> toType,
			String string,
			BiFunction<Object, Object, Boolean> comparator) {
		assumeTrue(string != null);
		Object converted = convert(string, sameType);
        assertEqualsByCompare(value, converted, comparator);
	}

    private void assertEqualsByCompare(Object expected, Object actual, BiFunction<Object, Object, Boolean> comparator) {
        if (comparator == null) {
            assertEquals(expected, actual);
        } else {
            assertTrue(comparator.apply(expected, actual));
        }
    }

	@ParameterizedTest
	@MethodSource("parameters")
	public void testConversionFromStringArray(
			Object value,
			Class<?> sameType,
			Class<?> toType,
			String string,
			BiFunction<Object, Object, Boolean> comparator) {
		assumeTrue(string != null);
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
            assertEqualsByCompare(Array.get(array, i), Array.get(converted, i), comparator);
        }
	}

}
