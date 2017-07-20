package com.github.resource4j.resources.processors.parser;

public class State<E extends Enum<E>, T extends Enum<T>> {
    private final Case<E, T>[] cases;
    private final Action<E, T> anyMatch;
    private final Action<E, T> finalMatch;
    private final Action<E, T> onError;

    public State(Case<E, T>[] cases,
                 Action<E, T> anyMatch,
                 Action<E, T> finalMatch,
                 Action<E, T> onError) {
        this.cases = cases;
        this.anyMatch = anyMatch;
        this.finalMatch = finalMatch;
        this.onError = onError;
    }

    public T onComplete(StateMachineListener<E> listener) {
        try {
            return finalMatch.trigger(listener);
        } catch (IllegalStateException e) {
            return onError.trigger(listener);
        }
    }

    public T onReceived(char c, StateMachineListener<E> listener) {
        try {
            for (Case<E, T> cs : cases) {
                if (cs.c == c) {
                    return cs.trigger(listener);
                }
            }
            return anyMatch.trigger(listener);
        } catch (IllegalStateException e) {
            return onError.trigger(listener);
        }
    }
}
