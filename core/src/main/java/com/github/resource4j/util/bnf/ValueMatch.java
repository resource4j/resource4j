package com.github.resource4j.util.bnf;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.github.resource4j.util.bnf.FunctionMatch.functionMatched;
import static com.github.resource4j.util.bnf.FunctionMatch.functionNotMatched;

public class ValueMatch<T> extends Match<T> {

    public static <T> ValueMatch<T> valueNotMatched(Cursor cursor) {
        return new ValueMatch<>(cursor);
    }

    public static <T> ValueMatch<T> valueMatched(Cursor cursor, T value) {
        return new ValueMatch<>(cursor, value);
    }

    private ValueMatch(Cursor cursor) {
        super(cursor);
    }

    private ValueMatch(Cursor cursor, T value) {
        super(cursor, value);
    }

    public <R,V> FunctionMatch<T, R, V> then(Function<Cursor, ValueMatch<? extends BiFunction<T, R, V>>> operator) {
        if (this.value == null) return functionNotMatched(cursor);
        ValueMatch<? extends BiFunction<T, R, V>> result = operator.apply(cursor);
        return result
                .map(function -> functionMatched(cursor, this, function))
                .orElseGet(() -> functionNotMatched(cursor));
    }

    @SafeVarargs
    public final <R,V> FunctionMatch<T,R,V> thenEither(Function<Cursor, ValueMatch<BiFunction<T, R, V>>>... cases) {
        if (this.value == null) return functionNotMatched(cursor);

        for (Function<Cursor, ValueMatch<BiFunction<T, R, V>>> function : cases) {
            ValueMatch<BiFunction<T, R, V>> operatorResult = function.apply(cursor);
            if (operatorResult.value != null)
                return functionMatched(cursor, this, operatorResult.value);
        }
        return functionNotMatched(cursor);
    }

    public ValueMatch<T> thenMaybe(Function<Cursor, ValueMatch<Function<T, T>>> producer) {
        if (this.value == null) return this;
        ValueMatch<Function<T, T>> match = producer.apply(cursor);
        return match
                .map(function -> valueMatched(cursor, function.apply(this.value)))
                .orElse(this);
    }

}
