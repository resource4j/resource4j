package com.github.resource4j.resources.processors.parser;

public class StateMachineInstance<E extends Enum<E>, T extends Enum<T>> {

    private StateMachine<E, T> machine;

    private State<E, T> currentState;

    private StateMachineListener<E> listener;

    public StateMachineInstance(StateMachine<E, T> machine,
                                StateMachineListener<E> listener) {
        this.machine = machine;
        this.listener = listener;
        this.currentState = machine.initialState();
    }

    public void onReceived(char c) {
        T stateId = currentState.onReceived(c, listener);
        this.currentState = machine.state(stateId);
    }

    public void onComplete() {
        this.currentState.onComplete(listener);
    }

}
