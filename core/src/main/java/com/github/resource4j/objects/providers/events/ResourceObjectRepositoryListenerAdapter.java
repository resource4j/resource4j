package com.github.resource4j.objects.providers.events;

import com.github.resource4j.resources.context.ResourceResolutionContext;

public class ResourceObjectRepositoryListenerAdapter implements ResourceObjectRepositoryListener {

    @Override
    public void objectRemoved(String name, ResourceResolutionContext context) {
    }

    @Override
    public void objectCreated(String name, ResourceResolutionContext context) {
    }

    @Override
    public void objectUpdated(String name, ResourceResolutionContext context) {
    }

}
