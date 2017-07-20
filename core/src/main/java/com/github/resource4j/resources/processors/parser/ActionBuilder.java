package com.github.resource4j.resources.processors.parser;

public class ActionBuilder<E extends Enum<E>, T extends Enum<T>> {

    private E[] events;
    private T transition;

    public ActionBuilder(T transition, E[] events) {
        this.events = events;
        this.transition = transition;
    }

    public Action<E, T> build() {
        return new Action<>(transition, events);
    }

    public Case<E, T> buildCase(char c) {
        return new Case<>(transition, events, c);
    }
}
