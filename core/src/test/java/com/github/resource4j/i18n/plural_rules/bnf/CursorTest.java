package com.github.resource4j.i18n.plural_rules.bnf;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CursorTest {

    @Test
    public void shouldMatchNumberAndAdvanceToEndOfString() {
        String expression = "12345";
        Cursor cursor = new Cursor(expression);
        assertTrue(cursor.probe().expect("[0-9]+").matches());
        assertEquals(5, cursor.position());
    }

    @Test
    public void shouldMatchNumberAndAdvanceToNextCharacter() {
        String expression = "12345 ";
        Cursor cursor = new Cursor(expression);
        assertTrue(cursor.probe().expect("[0-9]+").matches());
        assertEquals(5, cursor.position());
    }

    @Test
    public void shouldMatchNumberInsideWhitespaceAndAdvanceToNextCharacter() {
        String expression = "   12345   ttt";
        Cursor cursor = new Cursor(expression);
        assertTrue(cursor.probe()
                .expectDelimiters()
                .expect("[0-9]+")
                .expectDelimiters()
                .matches());
        assertEquals(11, cursor.position());
    }

    @Test
    public void shouldNotMatchNonNumberWithWhitespace() {
        String expression = "   ttt   ";
        Cursor cursor = new Cursor(expression);
        assertFalse(cursor.probe()
                .expectDelimiters()
                .expect("[0-9]+")
                .expectDelimiters()
                .matches());
        assertEquals(0, cursor.position());
    }


    @Test
    public void shouldNotMatchEmptyStringAndStayAtBeginning() {
        String expression = "";
        Cursor cursor = new Cursor(expression);
        assertFalse(cursor.probe().expect("[0-9]+").matches());
        assertEquals(0, cursor.position());
    }


    @Test
    public void shouldNotMatchNonDigitAndStayAtBeginning() {
        String expression = "a12345";
        Cursor cursor = new Cursor(expression);
        assertFalse(cursor.probe().expect("[0-9]+").matches());
        assertEquals(0, cursor.position());
    }

    @Test
    public void shouldMatchOneOfWords() {
        String expression = " != 2";
        Cursor cursor = new Cursor(expression);
        CursorProbe probe = cursor.probe()
                .expectDelimiters()
                .expectOneOf("not equal", "!=")
                .expectDelimiters();
        assertTrue(probe.match().isPresent());
        assertEquals("!=", probe.match().get());
        assertEquals(4, cursor.position());

    }

}
