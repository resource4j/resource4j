package com.github.resource4j.util.bnf;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.github.resource4j.util.bnf.ValueMatch.valueMatched;
import static com.github.resource4j.util.bnf.ValueMatch.valueNotMatched;

public class FunctionMatch<L,R,V> extends Match<L> {

    private final BiFunction<L, R, V> producer;

    static <L,R,V> FunctionMatch<L,R,V> functionMatched(Cursor cursor,
                                                        ValueMatch<L> left,
                                                        BiFunction<L, R, V> producer) {
        return new FunctionMatch<>(cursor, left, producer);
    }

    static <L,R,V> FunctionMatch<L,R,V> functionNotMatched(Cursor cursor) {
        return new FunctionMatch<>(cursor);
    }

    private FunctionMatch(Cursor cursor) {
        super(cursor);
        this.producer = null;
    }

    private FunctionMatch(Cursor cursor,
                          ValueMatch<L> left,
                          BiFunction<L, R, V> producer) {
        super(cursor, left.value);
        this.producer = producer;
    }

    public ValueMatch<V> then(Function<Cursor, ValueMatch<R>> function) {
        if (this.value == null || producer == null)
            return valueNotMatched(cursor());

        ValueMatch<R> right = function.apply(cursor);
        return right
                .map(rval -> valueMatched(cursor, producer.apply(this.value, rval)))
                .orElse(valueNotMatched(cursor));
    }

}
