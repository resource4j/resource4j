package com.github.resource4j.converters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringToBooleanConversionTest {

    @Test
    public void testTrueValue() {
        assertTrue(TypeConverter.convert("true", Boolean.class));
    }

    @Test
    public void testOnValue() {
        assertTrue(TypeConverter.convert("on", Boolean.class));
    }

    @Test
    public void testOneValue() {
        assertTrue(TypeConverter.convert("1", Boolean.class));
    }

    @Test
    public void testEnabledValue() {
        assertTrue(TypeConverter.convert("enabled", Boolean.class));
    }

    @Test
    public void testCheckedValue() {
        assertTrue(TypeConverter.convert("checked", Boolean.class));
    }

    @Test
    public void testCaseInsensitiveTrue() {
        assertTrue(TypeConverter.convert("TRUE", Boolean.class));
    }

    @Test
    public void testCaseInsensitiveMixed() {
        assertTrue(TypeConverter.convert("True", Boolean.class));
    }

    @Test
    public void testCaseInsensitiveOn() {
        assertTrue(TypeConverter.convert("ON", Boolean.class));
    }

    @Test
    public void testCaseInsensitiveEnabled() {
        assertTrue(TypeConverter.convert("Enabled", Boolean.class));
    }

    @Test
    public void testFalseValue() {
        assertFalse(TypeConverter.convert("false", Boolean.class));
    }

    @Test
    public void testOffValue() {
        assertFalse(TypeConverter.convert("off", Boolean.class));
    }

    @Test
    public void testZeroValue() {
        assertFalse(TypeConverter.convert("0", Boolean.class));
    }

    @Test
    public void testDisabledValue() {
        assertFalse(TypeConverter.convert("disabled", Boolean.class));
    }

    @Test
    public void testNoValue() {
        assertFalse(TypeConverter.convert("no", Boolean.class));
    }

    @Test
    public void testRandomStringIsFalse() {
        assertFalse(TypeConverter.convert("random", Boolean.class));
    }

    @Test
    public void testEmptyStringIsFalse() {
        assertFalse(TypeConverter.convert("", Boolean.class));
    }

    @Test
    public void testPrimitiveBoolean() {
        assertTrue(TypeConverter.convert("true", Boolean.TYPE));
    }

}
