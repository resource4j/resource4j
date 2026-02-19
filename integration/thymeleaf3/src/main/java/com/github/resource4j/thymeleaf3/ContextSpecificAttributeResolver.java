package com.github.resource4j.thymeleaf3;

import org.thymeleaf.context.IContext;

public interface ContextSpecificAttributeResolver {

    String name();

    Object resolve(IContext context);

}
