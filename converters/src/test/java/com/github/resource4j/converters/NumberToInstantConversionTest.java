package com.github.resource4j.converters;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class NumberToInstantConversionTest {

    @Test
    public void testEpochZero() {
        Instant result = TypeConverter.convert(0L, Instant.class);
        assertEquals(Instant.EPOCH, result);
    }

    @Test
    public void testKnownTimestamp() {
        // 2015-01-01T00:00:00Z = 1420070400000L
        Instant result = TypeConverter.convert(1420070400000L, Instant.class);
        assertEquals(Instant.parse("2015-01-01T00:00:00Z"), result);
    }

    @Test
    public void testNegativeTimestamp() {
        // Before epoch: -1000L = 1969-12-31T23:59:59Z
        Instant result = TypeConverter.convert(-1000L, Instant.class);
        assertEquals(Instant.ofEpochMilli(-1000L), result);
    }

    @Test
    public void testNullReturnsNull() {
        Instant result = TypeConverter.convert(null, Instant.class);
        assertNull(result);
    }

}
