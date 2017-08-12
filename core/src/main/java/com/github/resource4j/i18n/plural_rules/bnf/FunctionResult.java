package com.github.resource4j.i18n.plural_rules.bnf;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.github.resource4j.i18n.plural_rules.bnf.ValueMatchResult.valueMatched;
import static com.github.resource4j.i18n.plural_rules.bnf.ValueMatchResult.valueNotMatched;

public class FunctionResult<L,R,V> extends MatchResult<L> {

    private final BiFunction<L, R, V> producer;

    public static <L,R,V> FunctionResult<L,R,V> functionMatched(Cursor cursor,
                                                                ValueMatchResult<L> left,
                                                                BiFunction<L, R, V> producer) {
        return new FunctionResult<>(cursor, left, producer);
    }

    public static <L,R,V> FunctionResult<L,R,V> functionNotMatched(Cursor cursor) {
        return new FunctionResult<>(cursor);
    }

    private FunctionResult(Cursor cursor) {
        super(cursor);
        this.producer = null;
    }

    private FunctionResult(Cursor cursor,
                          ValueMatchResult<L> left,
                          BiFunction<L, R, V> producer) {
        super(cursor, left.value);
        this.producer = producer;
    }

    public ValueMatchResult<V> then(Function<Cursor, ValueMatchResult<R>> function) {
        if (!this.value.isPresent() || producer == null)
            return valueNotMatched(cursor);
        L lval = this.value.get();

        ValueMatchResult<R> right = function.apply(cursor);
        return right.value
                .map(rval -> valueMatched(cursor, producer.apply(lval, rval)))
                .orElse(valueNotMatched(cursor));
    }

}
