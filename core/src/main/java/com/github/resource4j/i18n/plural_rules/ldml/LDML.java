package com.github.resource4j.i18n.plural_rules.ldml;

import com.github.resource4j.util.bnf.Cursor;
import com.github.resource4j.util.bnf.Match;
import com.github.resource4j.util.bnf.ValueMatch;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.resource4j.i18n.plural_rules.ldml.LDMLMath.fraction;
import static com.github.resource4j.util.bnf.ValueMatch.valueMatched;
import static com.github.resource4j.util.bnf.ValueMatch.valueNotMatched;
import static java.math.BigInteger.ZERO;

/**
 * condition     = and_condition ('or' and_condition)*
 * relation      = is_relation | in_relation | within_relation
 * is_relation   = expr 'is' ('not')? value
 * in_relation   = expr (('not')? 'in' | '=' | '!=') range_list
 * within_relation = expr ('not')? 'within' range_list
 * expr          = operand (('mod' | '%') value)?
 * operand       = 'n' | 'i' | 'f' | 't' | 'v' | 'w'
 * range_list    = (range | value) (',' range_list)*
 * range         = value'..'value
 * value         = digit+
 * decimalValue  = value ('.' value)?
 * digit         = 0|1|2|3|4|5|6|7|8|9
 */
final class LDML {

    private LDML() { }

    static ValueMatch<BinaryOperator<Predicate<Number>>> or(Cursor cursor) {
        return logic(cursor, "or", Predicate::or);
    }
    
    private static ValueMatch<BinaryOperator<Predicate<Number>>> and(Cursor cursor) {
        return logic(cursor, "and", Predicate::and);
    }

    private static ValueMatch<BinaryOperator<Predicate<Number>>> logic(Cursor cursor,
                                                                       String op, BinaryOperator<Predicate<Number>>  operator) {
        boolean matches = cursor.probe()
                .expectDelimiters()
                .expect(op)
                .expectDelimiters().matches();
        return matches ? valueMatched(cursor, operator) : valueNotMatched(cursor);
    }

    /**
     * and_condition = relation ('and' relation)*
     */
    static ValueMatch<Predicate<Number>> andCondition(Cursor cursor) {
        return cursor.expect(LDML::relation, LDML::and);
    }

    private static ValueMatch<Predicate<Number>> relation(Cursor cursor) {
        return cursor.expect(LDML::expression)
                .thenEither(
                    LDML::equal,
                    LDML::notEqual)
                .then(LDML::rangeList);
    }

    private static ValueMatch<BiFunction<Function<Number, Number>, RangeList, Predicate<Number>>> equal(Cursor cursor) {
        if (cursor.probe()
                .expectDelimiters().expect("=").expectDelimiters()
                .matches()) {
            return valueMatched(cursor, (function, range) -> (value) -> range.contains(function.apply(value)));
        } else {
            return valueNotMatched(cursor);
        }
    }

    private static ValueMatch<BiFunction<Function<Number, Number>, RangeList, Predicate<Number>>> notEqual(Cursor cursor) {
        if (cursor.probe()
                .expectDelimiters().expect("!=").expectDelimiters()
                .matches()) {
            return valueMatched(cursor, (function, range) -> (value) -> !range.contains(function.apply(value)));
        } else {
            return valueNotMatched(cursor);
        }
    }

    private static ValueMatch<RangeList> rangeList(Cursor cursor) {
        return cursor.expect(LDML::range, LDML::rangeListSeparator);
    }

    private static ValueMatch<RangeList> range(Cursor cursor) {
        ValueMatch<Number> minResult = value(cursor);
        if (minResult.isPresent()) {
            Number min = minResult.get();
            if (cursor.probe().expect("\\.\\.").matches()) {
                return value(cursor)
                        .map(max -> valueMatched(cursor, new RangeList(min, max)))
                        .orElse(valueNotMatched(cursor));
            } else {
                return valueMatched(cursor, new RangeList(min));
            }
        }
        return valueNotMatched(cursor);
    }

    private static ValueMatch<? extends BiFunction<RangeList, RangeList, RangeList>> rangeListSeparator(Cursor cursor) {
        return cursor.probe().maybeDelimiters().expect(",").maybeDelimiters().matches()
                ? valueMatched(cursor, RangeList::add)
                : valueNotMatched(cursor);
    }


    private static ValueMatch<Function<Number, Number>> expression(Cursor cursor) {
        return cursor.expect(LDML::operand)
                .thenMaybe(LDML::modulo);
    }

    private static ValueMatch<Function<Number, Number>> operand(Cursor cursor) {
        Optional<String> match = cursor.probe().expectOneOf("n", "i", "f", "t", "v", "w").match();
        if (match.isPresent()) {
            Function<Number, Number> function = Function.identity();
            switch (match.get()) {
                case "n": function = LDMLMath::abs; break;
                case "i": function = LDMLMath::trunc; break;
                case "f": function = number -> fraction(number, true).orElse(ZERO); break;
                case "t": function = number -> fraction(number, false).orElse(ZERO); break;
                case "v": function = number -> fraction(number, true).map(LDMLMath::size).orElse(ZERO); break;
                case "w": function = number -> fraction(number, false).map(LDMLMath::size).orElse(ZERO); break;
                default: /* unknown, but we don't expect it anyway */ break;
            }
            return valueMatched(cursor, function);
        }
        return valueNotMatched(cursor);
    }

    private static ValueMatch<Function<Function<Number, Number>, Function<Number, Number>>> modulo(Cursor cursor) {
        boolean matches = cursor.probe().expectDelimiters()
                .expectOneOf("mod", "%")
                .expectDelimiters()
                .matches();
        if (matches) {
            Match<Number> divider = LDML.value(cursor);
            if (divider.isPresent()) {
                Function<Number, Number> modulo = number -> number.longValue() % divider.get().longValue();
                return valueMatched(cursor, function -> function.andThen(modulo));
            }
        }
        return valueNotMatched(cursor);
    }

    static ValueMatch<Number> value(Cursor cursor) {
        return cursor.probe().expect("[0-9]+")
                .match()
                .map(Integer::parseInt)
                .map(number -> valueMatched(cursor, (Number) number))
                .orElseGet(() -> valueNotMatched(cursor));
    }

}
