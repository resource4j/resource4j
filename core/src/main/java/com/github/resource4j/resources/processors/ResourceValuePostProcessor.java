package com.github.resource4j.resources.processors;

import com.github.resource4j.ResourceException;
import com.github.resource4j.resources.context.ResourceResolutionContext;

public interface ResourceValuePostProcessor {

    String process(String value, ResourceResolutionContext context, ResourceResolver resolver) throws ResourceException;

}
