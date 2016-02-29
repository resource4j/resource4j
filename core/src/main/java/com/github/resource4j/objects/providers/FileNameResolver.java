package com.github.resource4j.objects.providers;

import com.github.resource4j.resources.context.ResourceResolutionContext;

public interface FileNameResolver {

    String resolve(String name, ResourceResolutionContext context);

}
