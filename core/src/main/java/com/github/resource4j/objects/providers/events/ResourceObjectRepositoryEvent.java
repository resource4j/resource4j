package com.github.resource4j.objects.providers.events;

import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.io.Serializable;

public class ResourceObjectRepositoryEvent implements Serializable {

    private String source;

    private ResourceObjectEventType type;

    private String name;

    private ResourceResolutionContext context;

    public static ResourceObjectRepositoryEvent created(String source, String name, ResourceResolutionContext context) {
        return new ResourceObjectRepositoryEvent(source, ResourceObjectEventType.CREATED, name, context);
    }


    public static ResourceObjectRepositoryEvent modified(String source, String name, ResourceResolutionContext context) {
        return new ResourceObjectRepositoryEvent(source, ResourceObjectEventType.MODIFIED, name, context);
    }


    public static ResourceObjectRepositoryEvent deleted(String source, String name, ResourceResolutionContext context) {
        return new ResourceObjectRepositoryEvent(source, ResourceObjectEventType.DELETED, name, context);
    }

    public ResourceObjectRepositoryEvent(String source,
                                         ResourceObjectEventType type,
                                         String name,
                                         ResourceResolutionContext context) {
        this.source = source;
        this.type = type;
        this.name = name;
        this.context = context;
    }

    public String source() {
        return source;
    }

    public ResourceObjectEventType type() {
        return type;
    }


    public String objectName() {
        return name;
    }

    public ResourceResolutionContext context() {
        return context;
    }

    @Override
    public String toString() {
        return type + ":" + name + "@" + context;
    }

}
