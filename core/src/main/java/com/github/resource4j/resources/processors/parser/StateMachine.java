package com.github.resource4j.resources.processors.parser;

import java.util.Map;

public class StateMachine<E extends Enum<E>, T extends Enum<T>> {
    private State<E, T> initial;
    private Map<T, State<E, T>> states;

    public StateMachine(State<E, T> initial, Map<T, State<E, T>> states) {
        this.initial = initial;
        this.states = states;
    }

    public State<E, T> initialState() {
        return initial;
    }

    public State<E, T> state(T stateId) {
        return states.get(stateId);
    }

    public StateMachineInstance<E, T> start(StateMachineListener<E>  listener) {
        return new StateMachineInstance<>(this, listener);
    }

}

