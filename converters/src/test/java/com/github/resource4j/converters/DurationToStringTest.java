package com.github.resource4j.converters;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class DurationToStringTest {

    public static final Duration SOME_LONG_DURATION = Duration.ofDays(3).plusHours(5).plusMinutes(30);
    public static final String SOME_LONG_DURATION_STRING = "PT77H30M";
    public static final String SOME_LONG_NEGATIVE_DURATION_STRING = "PT-77H-30M";

    @Test
    public void testDurationToStringConversion() {
        Duration duration = SOME_LONG_DURATION;
        String actual = TypeConverter.convert(duration, String.class);
        assertEquals(SOME_LONG_DURATION_STRING, actual);
    }

    @Test
    public void testStringToDurationConversion() {
        Duration expected = SOME_LONG_DURATION;
        Duration actual = TypeConverter.convert(SOME_LONG_DURATION_STRING, Duration.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testNegativeDurationToStringConversion() {
        Duration duration = SOME_LONG_DURATION.negated();
        String actual = TypeConverter.convert(duration, String.class);
        assertEquals(SOME_LONG_NEGATIVE_DURATION_STRING, actual);
    }

    @Test
    public void testStringToNegativeDurationConversion() {
        Duration expected = SOME_LONG_DURATION.negated();
        Duration actual = TypeConverter.convert(SOME_LONG_NEGATIVE_DURATION_STRING, Duration.class);
        assertEquals(expected, actual);
    }

}
