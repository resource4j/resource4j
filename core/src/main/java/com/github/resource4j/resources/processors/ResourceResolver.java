package com.github.resource4j.resources.processors;


import java.util.Map;

public interface ResourceResolver {

    Object get(String name, Map<String, Object> params);

}
