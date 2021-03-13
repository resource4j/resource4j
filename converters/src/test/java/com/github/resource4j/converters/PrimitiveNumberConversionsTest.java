package com.github.resource4j.converters;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrimitiveNumberConversionsTest {

    @Test
    public void testIntegerClassToLongPrimitiveConversion() {
        long result = TypeConverter.convert(Integer.valueOf(10), Long.TYPE);
        assertEquals(10, result);
    }

    @Test
    public void testConversionWithOverflowIgnoresHigherBytes() {
        byte result = TypeConverter.convert(257, Byte.TYPE);
        assertEquals((byte) 1, result);
    }

}
