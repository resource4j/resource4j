package com.github.resource4j.i18n.plural_rules;

import com.github.resource4j.i18n.plural_rules.bnf.Cursor;
import com.github.resource4j.i18n.plural_rules.bnf.ValueMatchResult;

import java.util.function.Predicate;

public class PredicateParser {

    public static Predicate<Number> parse(String text) {
        Cursor cursor = new Cursor(text);
        ValueMatchResult<Predicate<Number>> result = cursor.expect(LDML::andCondition);
        Predicate<Number> predicate = result.value.orElse(number -> false);
        do {
            result = result
                        .then(LDML::or)
                        .then(LDML::andCondition);
            predicate = result.value.orElse(predicate);
        } while (result.value.isPresent());
        return predicate;
    }


}
