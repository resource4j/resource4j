package com.github.resource4j.resources.processors.strategies;

import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.resources.processors.ResourceResolver;

public interface PropertyResolver {

    Object resolve(Object value, String property, ResourceResolutionContext context, ResourceResolver resolver);

}
