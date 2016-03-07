package com.github.resource4j.objects.providers.events;

import com.github.resource4j.resources.context.ResourceResolutionContext;

public interface ResourceObjectRepositoryListener {

    void objectRemoved(String name, ResourceResolutionContext context);

    void objectCreated(String name, ResourceResolutionContext context);

    void objectUpdated(String name, ResourceResolutionContext context);

}
