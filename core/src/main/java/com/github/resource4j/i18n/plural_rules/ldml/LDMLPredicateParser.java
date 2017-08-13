package com.github.resource4j.i18n.plural_rules.ldml;

import com.github.resource4j.util.bnf.Cursor;
import com.github.resource4j.util.bnf.ValueMatch;

import java.util.function.Predicate;

class LDMLPredicateParser {

    static Predicate<Number> parse(String text) {
        Cursor cursor = new Cursor(text);
        ValueMatch<Predicate<Number>> result = cursor.expect(LDML::andCondition, LDML::or);
        if (!result.isPresent()) {
            throw new IllegalArgumentException(text);
        }
        return result.get();
    }

}
