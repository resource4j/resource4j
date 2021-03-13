package com.github.resource4j.i18n.plural_rules.ldml;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static com.github.resource4j.i18n.plural_rules.ldml.LDMLPredicateParser.parse;
import static org.junit.jupiter.api.Assertions.*;

public class LDMLPredicateParserTest {

    @Test
    public void shouldFailIfIncorrectFormat() {
        assertThrows(IllegalArgumentException.class, () -> parse("wrong"));
    }

    @Test
    public void shouldFailIfErrorInExpression() {
        assertThrows(IllegalArgumentException.class, () -> parse("i == 1"));
    }

    @Test
    public void shouldFailIfIncompleteOrExpression() {
        assertThrows(IllegalArgumentException.class, () -> parse("i = 1 or"));
    }

    @Test
    public void shouldFailIfIncompleteAndExpression() {
        assertThrows(IllegalArgumentException.class, () -> parse("i = 1 and"));
    }

    @Test
    public void shouldFailWhenEmpty() {
        assertThrows(IllegalArgumentException.class, () -> parse(""));
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
    public void shouldAcceptOrCondition() {
        final Predicate<Number> predicate = parse("i = 0 or i = 1");
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
        assertFalse(predicate.test(Integer.MIN_VALUE));
        assertFalse(predicate.test(Double.NEGATIVE_INFINITY));
        assertFalse(predicate.test(-1));
        assertTrue(predicate.test(-0.1));
        assertTrue(predicate.test(0));
        assertTrue(predicate.test(0.1));
        assertTrue(predicate.test(1));
        assertTrue(predicate.test(4.5));
        assertTrue(predicate.test(5.1));
        assertTrue(predicate.test(5.9));
        assertFalse(predicate.test(6));
        assertFalse(predicate.test(Integer.MAX_VALUE));
        assertFalse(predicate.test(Double.POSITIVE_INFINITY));
    }

    @Test
    public void shouldAcceptMultipleRanges() {
        final Predicate<Number> predicate = parse("i = 0..2,4,6..9");
        assertFalse(predicate.test(Integer.MIN_VALUE));
        assertFalse(predicate.test(Double.NEGATIVE_INFINITY));
        assertFalse(predicate.test(-1));
        assertTrue(predicate.test(-0.1));
        assertTrue(predicate.test(0));
        assertTrue(predicate.test(0.1));
        assertTrue(predicate.test(1));
        assertTrue(predicate.test(4.5));
        assertFalse(predicate.test(5));
        assertTrue(predicate.test(6.1));
        assertTrue(predicate.test(9.9));
        assertFalse(predicate.test(Integer.MAX_VALUE));
        assertFalse(predicate.test(Double.POSITIVE_INFINITY));
    }

}
