package com.github.resource4j.i18n.plural_rules;

import java.util.function.BiFunction;

public class Operator<V> {

    public final V value;

    public final BiFunction<V, V, V> operator;

    public static <V> Operator<V> missing(V value) {
        return new Operator<>(value, (left, right) -> left);
    }

    public Operator(V value, BiFunction<V, V, V> operator) {
        this.value = value;
        this.operator = operator;
    }

    public V apply(V value) {
        return operator.apply(this.value, value);
    }

    public V get() {
        return value;
    }

}
