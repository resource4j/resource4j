package com.github.resource4j.resources.processors.parser;

public interface StateMachineListener<E extends Enum<E>> {

    void onEvent(E event);

}
