package com.github.resource4j.i18n;

import org.junit.Test;

import java.util.function.Predicate;

import static com.github.resource4j.i18n.PredicateParser.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PredicateParserTest {

    @Test
    public void shouldAcceptAllWhenEmpty() {
        final Predicate<Number> predicate = parse("");
        assertTrue(predicate.test(0));
        assertTrue(predicate.test(0.1));
        assertTrue(predicate.test(1));
        assertTrue(predicate.test(1.5));
        assertTrue(predicate.test(-1));
        assertTrue(predicate.test(2));
        assertTrue(predicate.test(Integer.MAX_VALUE));
    }


    @Test
    public void shouldAcceptEnumeratedIntegerPart() {
        final Predicate<Number> predicate = parse("i = 0,1");
        assertTrue(predicate.test(0));
        assertTrue(predicate.test(0.1));
        assertTrue(predicate.test(1));
        assertTrue(predicate.test(1.5));
        assertFalse(predicate.test(-1));
        assertFalse(predicate.test(2));
        assertFalse(predicate.test(Integer.MAX_VALUE));
    }

    @Test
    public void shouldAcceptLongEnumeratedIntegerPart() {
        final Predicate<Number> predicate = parse("i = 0,1,3,5");
        assertTrue(predicate.test(0));
        assertTrue(predicate.test(0.1));
        assertTrue(predicate.test(1.5));
        assertFalse(predicate.test(2));
        assertTrue(predicate.test(3));
        assertTrue(predicate.test(3.93334));
        assertTrue(predicate.test(5.0));
        assertFalse(predicate.test(Integer.MAX_VALUE));
    }
    @Test
    public void shouldAcceptRange() {
        final Predicate<Number> predicate = parse("i = 0..1");
        assertTrue(predicate.test(0));
        assertTrue(predicate.test(0.1));
        assertTrue(predicate.test(1));
        assertTrue(predicate.test(1.5));
        assertFalse(predicate.test(-1));
        assertFalse(predicate.test(2));
        assertFalse(predicate.test(Integer.MAX_VALUE));
    }

    @Test
    public void shouldAcceptLongRange() {
        final Predicate<Number> predicate = parse("i = 0..5");
        assertTrue(predicate.test(0));
        assertTrue(predicate.test(0.1));
        assertTrue(predicate.test(1));
        assertTrue(predicate.test(4.5));
        assertFalse(predicate.test(Integer.MAX_VALUE));
    }

}
