package com.github.resource4j.i18n.plural_rules;

import com.github.resource4j.i18n.plural_rules.bnf.Cursor;
import com.github.resource4j.i18n.plural_rules.bnf.MatchResult;
import com.github.resource4j.i18n.plural_rules.bnf.ValueMatchResult;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.resource4j.i18n.plural_rules.LDMLMath.fraction;
import static com.github.resource4j.i18n.plural_rules.bnf.ValueMatchResult.valueMatched;
import static com.github.resource4j.i18n.plural_rules.bnf.ValueMatchResult.valueNotMatched;
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
public class LDML {

    static ValueMatchResult<BinaryOperator<Predicate<Number>>> or(Cursor cursor) {
        return logic(cursor, "or", Predicate::or);
    }
    
    private static ValueMatchResult<BinaryOperator<Predicate<Number>>> and(Cursor cursor) {
        return logic(cursor, "and", Predicate::and);
    }

    private static ValueMatchResult<BinaryOperator<Predicate<Number>>> logic(Cursor cursor,
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
    static ValueMatchResult<Predicate<Number>> andCondition(Cursor cursor) {
        ValueMatchResult<Predicate<Number>> list = LDML.relation(cursor);
        ValueMatchResult<Predicate<Number>> result;
        do {
            result = list;
            list = list
                    .then(LDML::and)
                    .then(LDML::relation);
        } while (list.value.isPresent());
        return result;
    }

    private static ValueMatchResult<Predicate<Number>> relation(Cursor cursor) {
        return cursor.expect(LDML::expression)
                .thenEither(
                    LDML::equal,
                    LDML::notEqual)
                .then(LDML::rangeList);
    }

    private static ValueMatchResult<BiFunction<Function<Number, Number>, RangeList, Predicate<Number>>> equal(Cursor cursor) {
        if (cursor.probe()
                .expectDelimiters().expect("=").expectDelimiters()
                .matches()) {
            return valueMatched(cursor, (function, range) -> (value) -> range.contains(function.apply(value)));
        } else {
            return valueNotMatched(cursor);
        }
    }

    private static ValueMatchResult<BiFunction<Function<Number, Number>, RangeList, Predicate<Number>>> notEqual(Cursor cursor) {
        if (cursor.probe()
                .expectDelimiters().expect("!=").expectDelimiters()
                .matches()) {
            return valueMatched(cursor, (function, range) -> (value) -> !range.contains(function.apply(value)));
        } else {
            return valueNotMatched(cursor);
        }
    }

    private static ValueMatchResult<RangeList> rangeList(Cursor cursor) {
        ValueMatchResult<RangeList> result = LDML.range(cursor);
        ValueMatchResult<RangeList> list = result;
        do {
            result = list;
            list = list.then(LDML::rangeListSeparator)
                .then(LDML::range);
        } while (list.value.isPresent());
        return result;
    }

    private static ValueMatchResult<RangeList> range(Cursor cursor) {
        ValueMatchResult<Number> minResult = value(cursor);
        if (minResult.value.isPresent()) {
            Number min = minResult.value.get();
            if (cursor.probe().expect("\\.\\.").matches()) {
                return value(cursor).value
                        .map(max -> valueMatched(cursor, new RangeList(min, max)))
                        .orElse(valueNotMatched(cursor));
            } else {
                return valueMatched(cursor, new RangeList(min));
            }
        }
        return valueNotMatched(cursor);
    }

    private static ValueMatchResult<? extends BiFunction<RangeList, RangeList, RangeList>> rangeListSeparator(Cursor cursor) {
        return cursor.probe().maybeDelimiters().expect(",").maybeDelimiters().matches()
                ? valueMatched(cursor, RangeList::add)
                : valueNotMatched(cursor);
    }


    private static ValueMatchResult<Function<Number, Number>> expression(Cursor cursor) {
        return cursor.expect(LDML::operand)
                .thenMaybe(LDML::modulo);
    }

    private static ValueMatchResult<Function<Number, Number>> operand(Cursor cursor) {
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

    private static ValueMatchResult<Function<Function<Number, Number>, Function<Number, Number>>> modulo(Cursor cursor) {
        boolean matches = cursor.probe().expectDelimiters()
                .expectOneOf("mod", "%")
                .expectDelimiters()
                .matches();
        if (matches) {
            MatchResult<Number> divider = LDML.value(cursor);
            if (divider.value.isPresent()) {
                Function<Number, Number> modulo = number -> number.longValue() % divider.value.get().longValue();
                return valueMatched(cursor, function -> function.andThen(modulo));
            }
        }
        return valueNotMatched(cursor);
    }

    static ValueMatchResult<Number> value(Cursor cursor) {
        return cursor.probe().expect("[0-9]+")
                .match()
                .map(Integer::parseInt)
                .map(number -> valueMatched(cursor, (Number) number))
                .orElseGet(() -> valueNotMatched(cursor));
    }

}
