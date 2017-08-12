package com.github.resource4j.i18n.plural_rules;

import com.github.resource4j.i18n.plural_rules.bnf.Cursor;
import com.github.resource4j.i18n.plural_rules.bnf.MatchResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LDMLTest {

    @Test
    public void shouldMatchValidValue() {
        Cursor cursor = new Cursor("12345");
        MatchResult<Number> result = cursor.expect(LDML::value);
        assertTrue(result.value.isPresent());
        assertEquals(12345, result.value.get().intValue());
    }

}
