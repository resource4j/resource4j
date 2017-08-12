package com.github.resource4j.i18n.plural_rules.bnf;

import java.util.function.Function;

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

    public <T> ValueMatchResult<T> expect(Function<Cursor, ValueMatchResult<T>> matcher) {
        return matcher.apply(this);
    }

    public int position() {
        return this.position;
    }

}
