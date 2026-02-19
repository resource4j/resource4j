package com.github.resource4j.converters;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class TypeConverterEdgeCasesTest {

    enum Color { RED, GREEN, BLUE }

    @Test
    public void testNullToTypeReturnsNull() {
        assertNull(TypeConverter.convert(null, String.class));
    }

    @Test
    public void testNullTargetTypeThrowsNPE() {
        assertThrows(NullPointerException.class,
                () -> TypeConverter.convert("value", null));
    }

    @Test
    public void testEnumFromString() {
        Color result = TypeConverter.convert("RED", Color.class);
        assertEquals(Color.RED, result);
    }

    @Test
    public void testEnumFromOrdinal() {
        Color result = TypeConverter.convert(1, Color.class);
        assertEquals(Color.GREEN, result);
    }

    @Test
    public void testEnumToString() {
        String result = TypeConverter.convert(Color.BLUE, String.class);
        assertEquals("BLUE", result);
    }

    @Test
    public void testEnumToOrdinal() {
        Integer result = TypeConverter.convert(Color.BLUE, Integer.class);
        assertEquals(2, result);
    }

    @Test
    public void testFromFactoryMethod() {
        URI result = TypeConverter.convert("https://example.com", URI.class);
        assertEquals(URI.create("https://example.com"), result);
    }

    @Test
    public void testUnknownConversionThrows() {
        assertThrows(TypeCastException.class,
                () -> TypeConverter.convert(new Object(), Integer.class));
    }

    @Test
    public void testConvertWithDefaultValue() {
        Integer result = TypeConverter.convert("not-a-number", 42);
        assertEquals(42, result);
    }

    @Test
    public void testSameTypeReturnsSameInstance() {
        String value = "hello";
        assertSame(value, TypeConverter.convert(value, String.class));
    }

}
