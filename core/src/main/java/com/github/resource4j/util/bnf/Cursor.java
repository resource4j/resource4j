package com.github.resource4j.util.bnf;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.github.resource4j.util.bnf.ValueMatch.valueNotMatched;

public class Cursor {

    private final String text;
    private int position;

    public Cursor(String text) {
        this.text = text;
        this.position = 0;
    }

    public CursorProbe probe() {
        return new CursorProbe(text, position, newPos -> this.position = newPos);
    }

    public <T> ValueMatch<T> expect(Function<Cursor, ValueMatch<T>> matcher) {
        return matcher.apply(this);
    }

    public <T> ValueMatch<T> expect(
            Function<Cursor, ValueMatch<T>> value,
            Function<Cursor, ValueMatch<? extends BiFunction<T,T,T>>> combinator) {
        ValueMatch<T> match = expect(value);
        if (match.isPresent() && !eof()) {
            for (FunctionMatch<T, T, T> operator = match.then(combinator);
                 operator.isPresent();
                 operator = match.then(combinator)) {
                ValueMatch<T> res = operator.then(value);
                if (res.isPresent()) {
                    match = res;
                } else {
                    return valueNotMatched(this);
                }
            }
        }
        return match;
    }

    int position() {
        return this.position;
    }

    private boolean eof() {
        return position == text.length();
    }

}
