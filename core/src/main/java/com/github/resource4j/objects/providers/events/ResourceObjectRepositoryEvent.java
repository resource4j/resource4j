package com.github.resource4j.objects.providers.events;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.io.Serializable;

public class ResourceObjectRepositoryEvent implements Serializable {

    private static final long serialVersionUID = 1;

    private final String source;

    private final ResourceObjectEventType type;

    private final String name;

    private final String key;

    private final ResourceResolutionContext context;

    public static ResourceObjectRepositoryEvent created(String source, String name, ResourceResolutionContext context) {
        return new ResourceObjectRepositoryEvent(source, ResourceObjectEventType.CREATED, name, context);
    }


    public static ResourceObjectRepositoryEvent modified(String source, String name, ResourceResolutionContext context) {
        return new ResourceObjectRepositoryEvent(source, ResourceObjectEventType.MODIFIED, name, context);
    }


    public static ResourceObjectRepositoryEvent deleted(String source, String name, ResourceResolutionContext context) {
        return new ResourceObjectRepositoryEvent(source, ResourceObjectEventType.DELETED, name, context);
    }

    public static ResourceObjectRepositoryEvent valueSet(String source, ResourceKey key, ResourceResolutionContext context) {
        return new ResourceObjectRepositoryEvent(source, ResourceObjectEventType.VALUE_SET, key, context);
    }

    public static ResourceObjectRepositoryEvent valueRemoved(String source, ResourceKey key, ResourceResolutionContext context) {
        return new ResourceObjectRepositoryEvent(source, ResourceObjectEventType.VALUE_REMOVED, key, context);
    }

    public ResourceObjectRepositoryEvent(String source,
                                         ResourceObjectEventType type,
                                         String name,
                                         ResourceResolutionContext context) {
        this.source = source;
        this.type = type;
        this.name = name;
        this.key = null;
        this.context = context;
    }

    public ResourceObjectRepositoryEvent(String source,
                                         ResourceObjectEventType type,
                                         ResourceKey key,
                                         ResourceResolutionContext context) {
        this.source = source;
        this.type = type;
        this.name = key.getBundle();
        this.key = key.getId();
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

    public ResourceKey key() {
        return ResourceKey.key(name, key);
    }

    public ResourceResolutionContext context() {
        return context;
    }

    @Override
    public String toString() {
        return type + ":" + name + "@" + context;
    }

}
