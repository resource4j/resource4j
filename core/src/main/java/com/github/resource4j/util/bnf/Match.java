package com.github.resource4j.util.bnf;

import java.util.Optional;
import java.util.function.Function;

public abstract class Match<T> {

    final Cursor cursor;

    final T value;

    Match(Cursor cursor) {
        this.cursor = cursor;
        this.value = null;
    }

    Match(Cursor cursor, T value) {
        this.cursor = cursor;
        this.value = value;
    }

    final Cursor cursor() {
        return cursor;
    }

    public final boolean isPresent() {
        return value != null;
    }

    public final T get() {
        return value;
    }

    public <U> Optional<U> map(Function<? super T, ? extends U> function) {
        return Optional.ofNullable(value).map(function);
    }

    public T orElse(T other) {
        return Optional.ofNullable(value).orElse(other);
    }

}
