package com.github.resource4j.resources.processors.parser;

import com.github.resource4j.converters.TypeConverter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.resource4j.ResourceKey.ID_COMPONENT_DELIMITER;
import static com.github.resource4j.resources.processors.parser.BasicValueParserStateMachine.Action.*;
import static com.github.resource4j.resources.processors.parser.BasicValueParserStateMachine.StateId.*;
import static com.github.resource4j.resources.processors.parser.StateBuilder.aState;
import static com.github.resource4j.resources.processors.parser.StateMachineBuilder.aStateMachine;

/**
 * text := plain_text? (macro[plain_text])*
 * macro := '{' macro_def '}'
 * macro_def := escaped_macro | macro_name (param)*
 * escaped_macro := ':'
 * param := literal [ ':' property ]
 * property := literal
 */
public class BasicValueParserStateMachine implements StateMachineListener<BasicValueParserStateMachine.Action> {

    private final StateMachineValueResolver resolver;
    private final StateMachineInstance<Action, StateId> machine;
    private StringBuilder processed = new StringBuilder();
    private StringBuilder buffer = processed;
    private StringBuilder raw = new StringBuilder();
    private char prev = '\0';
    private char current = '\0';
    private boolean partialResult;
    private String name;
    private Map<String, Object> params = new LinkedHashMap<>();

    public BasicValueParserStateMachine(StateMachineValueResolver resolver) {
        this.resolver = resolver;
        this.machine = MACHINE.start(this);
    }

    public static BasicValueParserStateMachine start(StateMachineValueResolver resolver) {
        return new BasicValueParserStateMachine(resolver);
    }

    public void onReceived(char c) {
        this.prev = this.current;
        this.current = c;
        this.machine.onReceived(c);
        raw.append(c);
    }

    @Override
    public void onEvent(Action event) {
        switch (event) {
            case ACCEPT:
                buffer.append(current);
                break;
            case ACCEPT_PREV:
                buffer.append(prev);
                break;
            case ERROR:
                partialResult = true;
                break;
            case FORK:
                buffer = new StringBuilder();
                raw.setLength(0);
                params.clear();
                name = null;
                break;
            case PUSH:
                if (name == null) {
                    name = buffer.toString();
                }
                buffer.setLength(0);
                break;
            case RESOLVE_SEGMENT: {
                String name = buffer.toString();
                String resolved = resolve(name, Collections.emptyMap());
                if (resolved == null) {
                    throw new IllegalStateException();
                }
                params.put(name, resolved);
                buffer.setLength(0);
                buffer.append(resolved);
                break;
            }
            case RESOLVE: {
                String resolved = resolve(name, params);
                if (resolved == null) {
                    throw new IllegalStateException();
                }
                buffer.append(resolved);
                params.clear();
                break;
            }
            case RESOLVE_LITERAL: {
                String resolved = buffer.toString();
                buffer.setLength(0);
                buffer.append('{').append(resolved).append('}');
                break;
            }
            case JOIN:
                processed.append(buffer);
                buffer = processed;
                break;
            case JOIN_RAW:
                if (buffer != processed) {
                    processed.append(raw);
                    buffer = processed;
                }
                break;
        }
    }

    private String resolve(String name, Map<String, Object> params) {
        StringBuilder key = new StringBuilder();
        String property = null;
        if (name.indexOf(':') > 0) {
            String[] parts = name.split(":");
            if (parts.length != 2 || parts[0].length() < 1 || parts[1].length() < 1) {
                throw new IllegalArgumentException(name);
            }
            key.append(parts[0]);
            property = parts[1];
        }else {
            key.append(name);
        }
        // NB: linked hashmap preserves order of parameters
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            key.append(ID_COMPONENT_DELIMITER).append(entry.getValue());
        }
        Object value = resolver.resolve(key.toString(), property, params);
//        Object value = resourceResolver.get(key.toString(), params);
//        if (property != null) {
//            value = propertyResolver.resolve(value, property, resourceResolver);
//        }
        return TypeConverter.convert(value, String.class);
    }

    public boolean onComplete() {
        this.prev = this.current;
        this.current = '\0';
        this.machine.onComplete();
        return !partialResult;
    }

    public String toString() {
        return processed.toString();
    }

    protected static final StateBuilder<Action, StateId> INITIAL = aState(TEXT, ACCEPT)
            .on('\\', ESCAPE_TEXT)
            .on('{', START_MACRO, FORK)
            .on('}', TEXT, ERROR, ACCEPT)
            .whenDone(TEXT);

    protected static StateBuilder<Action, StateId> escape(StateId state) {
        return aState(state, ACCEPT).whenDone(TEXT, ERROR, ACCEPT_PREV, JOIN_RAW);
    }

    protected static StateBuilder<Action, StateId> aMacroState(StateId state) {
        return aState(state, ACCEPT)
                .on('{', START_MACRO, ERROR, JOIN_RAW, FORK)
                .onError(TEXT, ERROR, JOIN_RAW, ACCEPT)
                .whenDone(TEXT, ERROR, JOIN_RAW);
    }

    protected static StateBuilder<Action, StateId> aStartMacroState(StateId state, StateId escapeState) {
        return aMacroState(state)
                .on('\\', escapeState)
                .on(';', TEXT, ERROR, JOIN_RAW, ACCEPT)
                .on('}', TEXT, ERROR, JOIN_RAW, ACCEPT);
    }

    protected static StateBuilder<Action, StateId> aContinueMacroState(StateId anyState, StateId escapeState) {
        return aMacroState(anyState)
                .on('\\', escapeState);
    }

    protected static final StateMachine<Action, StateId> MACHINE = aStateMachine(TEXT, INITIAL)
            .bind(ESCAPE_TEXT, escape(TEXT))
            .bind(START_MACRO,
                    aStartMacroState(NAME, ESCAPE_NAME)
                            .on(':', START_LITERAL)
            ).bind(START_LITERAL,
                    aStartMacroState(LITERAL, ESCAPE_LITERAL)
            ).bind(NAME,
                    aContinueMacroState(NAME, ESCAPE_NAME)
                            .on(';', START_PARAM, PUSH)
                            .on('}', TEXT, PUSH, RESOLVE, JOIN)
            ).bind(START_PARAM,
                    aStartMacroState(PARAM, ESCAPE_PARAM)
                    .on(':', TEXT, ERROR, JOIN_RAW, ACCEPT)
            ).bind(PARAM,
                    aContinueMacroState(PARAM, ESCAPE_PARAM)
                    .on(';', START_PARAM, RESOLVE_SEGMENT, PUSH)
                    .on('}', TEXT, RESOLVE_SEGMENT, PUSH, RESOLVE, JOIN)
            ).bind(LITERAL,
                    aContinueMacroState(LITERAL, ESCAPE_LITERAL)
                    .on('}', TEXT, RESOLVE_LITERAL, JOIN)
            ).bind(ESCAPE_NAME,
                    escape(NAME)
            ).bind(ESCAPE_PARAM,
                    escape(PARAM)
            ).bind(ESCAPE_LITERAL,
                    escape(LITERAL)
            ).build();

    protected enum Action {
        ACCEPT,
        ACCEPT_PREV,
        FORK,
        PUSH,
        RESOLVE_SEGMENT,
        RESOLVE,
        RESOLVE_LITERAL,
        JOIN_RAW,
        JOIN,
        ERROR,
    }

    protected enum StateId {
        TEXT,
        ESCAPE_TEXT,
        START_MACRO,
        START_LITERAL,
        LITERAL,
        ESCAPE_LITERAL,
        NAME,
        ESCAPE_NAME,
        START_PARAM,
        PARAM,
        ESCAPE_PARAM
    }

}
