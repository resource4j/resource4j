package com.github.resource4j.resources.processors;

import com.github.resource4j.ResourceException;

public interface ResourceValuePostProcessor {

    String process(ResourceResolver resolver, String value) throws ResourceException;

}
