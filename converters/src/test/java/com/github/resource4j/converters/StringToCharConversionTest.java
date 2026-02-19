package com.github.resource4j.converters;

import com.github.resource4j.converters.impl.StringToCharConversion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringToCharConversionTest {

    @Test
    public void testSingleChar() {
        Character result = TypeConverter.convert("a", Character.class);
        assertEquals('a', result);
    }

    @Test
    public void testEmptyStringReturnsNullDirectly() {
        // The conversion itself returns null for empty string, but TypeConverter
        // then falls through to fromFactoryMethod which throws.
        // Test the conversion directly to verify the null return.
        StringToCharConversion conversion = new StringToCharConversion();
        @SuppressWarnings("unchecked")
        Character result = conversion.convert("", (Class) Character.class, null);
        assertNull(result);
    }

    @Test
    public void testEmptyStringThroughTypeConverterThrows() {
        // TypeConverter gets null from conversion, then tries factory method, which fails
        assertThrows(TypeCastException.class, () -> TypeConverter.convert("", Character.class));
    }

    @Test
    public void testMultiCharThrows() {
        assertThrows(TypeCastException.class, () -> TypeConverter.convert("ab", Character.class));
    }

    @Test
    public void testSingleDigitChar() {
        Character result = TypeConverter.convert("7", Character.class);
        assertEquals('7', result);
    }

    @Test
    public void testSingleSpaceChar() {
        Character result = TypeConverter.convert(" ", Character.class);
        assertEquals(' ', result);
    }

    @Test
    public void testPrimitiveCharType() {
        char result = TypeConverter.convert("x", Character.TYPE);
        assertEquals('x', result);
    }

}
