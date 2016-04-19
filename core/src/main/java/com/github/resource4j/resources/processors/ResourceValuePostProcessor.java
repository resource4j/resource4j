package com.github.resource4j.resources.processors;

import com.github.resource4j.ResourceException;

public interface ResourceValuePostProcessor {

    String process(String value) throws ResourceException;

}
