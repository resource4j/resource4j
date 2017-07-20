package com.github.resource4j.resources.processors.parser;

import java.util.Map;

public interface StateMachineValueResolver {

    Object resolve(String key, String property, Map<String, Object> params);

}
