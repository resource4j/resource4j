package com.github.resource4j.converters;

import org.junit.jupiter.api.Test;

import java.text.NumberFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class NumberToStringConversionTest {

    @Test
    public void testIntegerWithDecimalFormatPattern() {
        String result = TypeConverter.convert(42, String.class, "000");
        assertEquals("042", result);
    }

    @Test
    public void testDoubleWithPattern() {
        String result = TypeConverter.convert(3.14159, String.class, "#.##");
        assertEquals("3.14", result);
    }

    @Test
    public void testWithNumberFormatObject() {
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        String result = TypeConverter.convert(1234567, String.class, format);
        assertEquals("1,234,567", result);
    }

    @Test
    public void testLongWithPattern() {
        String result = TypeConverter.convert(7L, String.class, "0000");
        assertEquals("0007", result);
    }

    @Test
    public void testWithoutFormatUsesStringValueOf() {
        assertEquals("42", TypeConverter.convert(42, String.class));
        assertEquals("0.1", TypeConverter.convert(0.1, String.class));
        assertEquals("0.1", TypeConverter.convert(0.1f, String.class));
    }

}
