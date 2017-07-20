package com.github.resource4j.i18n;

import java.util.function.Predicate;

public class PredicateParser {

    // value -> (.) decimal value
    // decimal value -> ('..')range, (',') list
    // range -> (',') list
    // operand -> ('mod', '%') expr
    // expr -> ('in', 'is', 'not', 'within') relation

    public static Predicate<Number> parse(String text) {
        return null;
    }

}
