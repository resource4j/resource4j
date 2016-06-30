package com.github.resource4j.objects.providers.events;

import com.github.resource4j.resources.context.ResourceResolutionContext;

public abstract class ResourceObjectRepositoryListenerAdapter implements ResourceObjectRepositoryListener {

    private String name;

    protected ResourceObjectRepositoryListenerAdapter(String name) {
        this.name = name;
    }

    public void objectDeleted(String name, ResourceResolutionContext context) {
    }

    public void objectCreated(String name, ResourceResolutionContext context) {
    }

    public void objectModified(String name, ResourceResolutionContext context) {
    }

    @Override
    public void repositoryUpdated(ResourceObjectRepositoryEvent event) {
        switch (event.type()) {
            case CREATED:
                objectCreated(event.objectName(), event.context());
                break;
            case MODIFIED:
                objectModified(event.objectName(), event.context());
                break;
            case DELETED:
                objectDeleted(event.objectName(), event.context());
                break;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }

}
