package com.github.resource4j.i18n.plural_rules;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.function.Function.identity;

public class PredicateParser {

    public static Predicate<Number> parse(String text) {
        Cursor cursor = new Cursor(text);
        MatchResult<Predicate<Number>> result = new MatchResult<>(cursor, true, (number) -> true);
        result = result.then(PredicateParser::andCondition, (left, right) -> right, identity());
        do {
            result = result
                        .then(PredicateParser::orOperator)
                        .then(PredicateParser::andCondition, Operator::apply, op -> op.map(Operator::get));
        } while (result.matched);
        return result.value;
    }

    private static MatchResult<BiFunction<Predicate<Number>, Predicate<Number>, Predicate<Number>>> orOperator(Cursor cursor) {
        return defineOperator(cursor, "or", Predicate::or);
    }

    private static MatchResult<BiFunction<Predicate<Number>, Predicate<Number>, Predicate<Number>>> andOperator(Cursor cursor) {
        return defineOperator(cursor, "and", Predicate::and);
    }

    private static MatchResult<BiFunction<Predicate<Number>, Predicate<Number>, Predicate<Number>>> defineOperator(Cursor cursor, String op, BiFunction<Predicate<Number>, Predicate<Number>, Predicate<Number>> operator) {
        Optional<String> match = cursor.probe()
                .consumeDelimeters()
                .expect(op)
                .consumeDelimeters().match();
        return new MatchResult<>(cursor, match.isPresent(), operator);
    }

    private static MatchResult<Predicate<Number>> andCondition(Cursor cursor) {
        MatchResult<Predicate<Number>> result = new MatchResult<>(cursor, true, (number) -> true);
        result = result.then(PredicateParser::relation, (left, right) -> right, identity());
        do {
            result = result
                    .then(PredicateParser::andOperator)
                    .then(PredicateParser::relation, Operator::apply, op -> op.map(Operator::get));
        } while (result.matched);
        return result;
    }

    private static MatchResult<Predicate<Number>> relation(Cursor cursor) {
        MatchResult<Predicate<Number>> result = isRelation(cursor);
        if (!result.matched) {
            result = inRelation(cursor);
        }
        if (!result.matched) {
            result = withinRelation(cursor);
        }
        return result;
    }

    private static MatchResult<Predicate<Number>> withinRelation(Cursor cursor) {
        return null;
    }

    private static MatchResult<Predicate<Number>> inRelation(Cursor cursor) {
        return null;
    }

    private static MatchResult<Predicate<Number>> isRelation(Cursor cursor) {
        MatchResult<Function<Number, Number>> expr = isExpression(cursor);
        if (!expr.matched) {
            return new MatchResult<>(cursor, false, (number) -> false);
        }
        return null;
    }

    private static MatchResult<Function<Number, Number>> isExpression(Cursor cursor) {
        return null;
    }

}
