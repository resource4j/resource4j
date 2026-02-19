package com.github.resource4j.converters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CharToNumberConversionTest {

    @Test
    public void testHexDigitZero() {
        Integer result = TypeConverter.convert('0', Integer.class);
        assertEquals(0, result);
    }

    @Test
    public void testHexDigitNine() {
        Integer result = TypeConverter.convert('9', Integer.class);
        assertEquals(9, result);
    }

    @Test
    public void testHexDigitA() {
        Integer result = TypeConverter.convert('A', Integer.class);
        assertEquals(10, result);
    }

    @Test
    public void testHexDigitF() {
        Integer result = TypeConverter.convert('F', Integer.class);
        assertEquals(15, result);
    }

    @Test
    public void testLowercaseHexDigitThrows() {
        assertThrows(TypeCastException.class, () -> TypeConverter.convert('a', Integer.class));
    }

    @Test
    public void testNonHexCharThrows() {
        assertThrows(TypeCastException.class, () -> TypeConverter.convert('G', Integer.class));
    }

    @Test
    public void testSpaceThrows() {
        assertThrows(TypeCastException.class, () -> TypeConverter.convert(' ', Integer.class));
    }

    @Test
    public void testCharToPrimitiveInt() {
        int result = TypeConverter.convert('5', Integer.TYPE);
        assertEquals(5, result);
    }

    @Test
    public void testCharToLong() {
        Long result = TypeConverter.convert('5', Long.class);
        assertEquals(5L, result);
    }

    @Test
    public void testCharToByte() {
        Byte result = TypeConverter.convert('3', Byte.class);
        assertEquals((byte) 3, result);
    }

}
