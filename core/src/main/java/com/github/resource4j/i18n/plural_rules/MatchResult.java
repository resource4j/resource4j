package com.github.resource4j.i18n.plural_rules;

import java.util.function.BiFunction;
import java.util.function.Function;

public class MatchResult<T> {

    public final Cursor cursor;

    public final boolean matched;

    public final T value;

    public MatchResult(Cursor cursor,
                       boolean matched,
                       T value) {
        this.cursor = cursor;
        this.matched = matched;
        this.value = value;
    }

    public <V> MatchResult<V> map(Function<T, V> function) {
        return new MatchResult<>(cursor, matched, function.apply(value));
    }

    public MatchResult<Operator<T>> then(Function<Cursor, MatchResult<BiFunction<T, T, T>>> parser) {
        return then(parser, Operator::new, result -> new MatchResult<>(cursor, false, Operator.missing(result.value)));

    }

    public <R,V> MatchResult<V> then(Function<Cursor, MatchResult<R>> parser,
                                     BiFunction<T,R,V> reducer,
                                     Function<MatchResult<T>, MatchResult<V>> skip) {
        if (!this.matched) return skip.apply(this);
        MatchResult<R> result = parser.apply(cursor);
        if (!result.matched) return skip.apply(new MatchResult<T>(cursor, false, value));
        return new MatchResult<V>(cursor, true, reducer.apply(this.value, result.value));
    }

}
