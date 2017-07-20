package com.github.resource4j.resources.processors.parser;

/**
 * Created by Ivan on 22.06.2017.
 */
class Case<E extends Enum<E>, T extends Enum<T>> extends Action<E, T> {
    final char c;
    public Case(T transition, E[] events, char c) {
        super(transition, events);
        this.c = c;
    }
}
