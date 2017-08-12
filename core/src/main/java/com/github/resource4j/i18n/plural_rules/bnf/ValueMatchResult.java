package com.github.resource4j.i18n.plural_rules.bnf;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.github.resource4j.i18n.plural_rules.bnf.FunctionResult.functionMatched;
import static com.github.resource4j.i18n.plural_rules.bnf.FunctionResult.functionNotMatched;

public class ValueMatchResult<T> extends MatchResult<T> {

    public static <T> ValueMatchResult<T> valueNotMatched(Cursor cursor) {
        return new ValueMatchResult<>(cursor);
    }

    public static <T> ValueMatchResult<T> valueMatched(Cursor cursor, T value) {
        return new ValueMatchResult<T>(cursor, value);
    }

    private ValueMatchResult(Cursor cursor) {
        super(cursor);
    }

    private ValueMatchResult(Cursor cursor, T value) {
        super(cursor, value);
    }

    public <R,V> FunctionResult<T, R, V> then(Function<Cursor, ValueMatchResult<? extends BiFunction<T, R, V>>> operator) {
        if (!this.value.isPresent()) return functionNotMatched(cursor);
        ValueMatchResult<? extends BiFunction<T, R, V>> result = operator.apply(cursor);
        return result.value
                .map(function -> functionMatched(cursor, this, function))
                .orElseGet(() -> functionNotMatched(cursor));
    }

    @SafeVarargs
    public final <R,V> FunctionResult<T,R,V> thenEither(Function<Cursor, ValueMatchResult<BiFunction<T, R, V>>>... cases) {
        if (!this.value.isPresent()) return functionNotMatched(cursor);
        FunctionResult<T,R,V> result = null;
        for (Function<Cursor, ValueMatchResult<BiFunction<T, R, V>>> function : cases) {
            ValueMatchResult<BiFunction<T, R, V>> operatorResult = function.apply(cursor);
            if (operatorResult.value.isPresent())
                return functionMatched(cursor, this, operatorResult.value.get());
        }
        return functionNotMatched(cursor);
    }

    public ValueMatchResult<T> thenMaybe(Function<Cursor, ValueMatchResult<Function<T, T>>> producer) {
        if (!this.value.isPresent()) return this;
        ValueMatchResult<Function<T, T>> match = producer.apply(cursor);
        return match.value
                .map(function -> valueMatched(cursor, function.apply(this.value.get())))
                .orElse(this);
    }

}
