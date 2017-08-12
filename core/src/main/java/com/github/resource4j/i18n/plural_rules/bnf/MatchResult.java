package com.github.resource4j.i18n.plural_rules.bnf;

import com.github.resource4j.i18n.plural_rules.bnf.Cursor;

import java.util.Optional;

public abstract class MatchResult<T> {

    public final Cursor cursor;

    public final Optional<T> value;

    public MatchResult(Cursor cursor) {
        this.cursor = cursor;
        this.value = Optional.empty();
    }

    public MatchResult(Cursor cursor, T value) {
        this.cursor = cursor;
        this.value = Optional.of(value);
    }

    protected MatchResult(Cursor cursor, Optional<T> value) {
        this.cursor = cursor;
        this.value = value;
    }

}
