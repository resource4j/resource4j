package com.github.resource4j.objects.providers.resolvers;

import com.github.resource4j.resources.context.ResourceResolutionContext;

public interface ObjectNameResolver {

    String resolve(String name, ResourceResolutionContext context);

}
