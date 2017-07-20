package com.github.resource4j.resources.processors.parser;

/**
 * Created by Ivan on 22.06.2017.
 */
class Action<E extends Enum<E>, T extends Enum<T>> {
    public final T transition;
    public final E[] events;
    Action(T transition, E[] events) {
        this.transition = transition;
        this.events = events;
    }
    public T trigger(StateMachineListener<E> listener) {
        for (E event: events) {
            listener.onEvent(event);
        }
        return transition;
    }

}
