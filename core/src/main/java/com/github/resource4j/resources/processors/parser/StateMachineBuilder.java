package com.github.resource4j.resources.processors.parser;

import java.util.HashMap;
import java.util.Map;

public class StateMachineBuilder<E extends Enum<E>, T extends Enum<T>> {

    private final State<E, T> initial;
    private Map<T, State<E, T>> states = new HashMap<>();

    public StateMachineBuilder(T id, State<E, T> initial) {
        this.initial = initial;
        this.states.put(id, initial);
    }

    public static <E extends Enum<E>, T extends Enum<T>> StateMachineBuilder<E,T> aStateMachine(T id, StateBuilder<E,T> initialStateBuilder) {
        return new StateMachineBuilder<>(id, initialStateBuilder.build());
    }

    public StateMachineBuilder<E, T> bind(T transition, StateBuilder<E, T> builder) {
        states.put(transition, builder.build());
        return this;
    }

    public StateMachine<E, T> build() {
        return new StateMachine<>(initial, states);
    }

}
