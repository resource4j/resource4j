package com.github.resource4j.objects.providers.events;

import com.github.resource4j.objects.providers.mutable.ResourceObjectRepository;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

public class ResourceObjectRepositoryEventDispatcher implements ResourceObjectRepositoryListener {

    private Set<ResourceObjectRepositoryListener> listeners = new CopyOnWriteArraySet<>();

    private Logger log;

    public ResourceObjectRepositoryEventDispatcher(ResourceObjectRepository repository) {
        log = LoggerFactory.getLogger(repository.getClass());
    }

    public void addListener(ResourceObjectRepositoryListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(ResourceObjectRepositoryListener listener) {
        this.listeners.remove(listener);
    }

    private void notifySafe(String name, ResourceResolutionContext context, Consumer<ResourceObjectRepositoryListener> consumer) {
        listeners.forEach(listener -> {
            try {
                consumer.accept(listener);
            } catch (RuntimeException e) {
                log.error("failed to notify listener {} about object ({}-{})", listener, name, context, e);
            }
        });
    }

    @Override
    public void objectRemoved(String name, ResourceResolutionContext context) {
        notifySafe(name, context, listener -> listener.objectRemoved(name, context));
    }

    @Override
    public void objectCreated(String name, ResourceResolutionContext context) {
        notifySafe(name, context, listener -> listener.objectCreated(name, context));
    }

    @Override
    public void objectUpdated(String name, ResourceResolutionContext context) {
        notifySafe(name, context, listener -> listener.objectUpdated(name, context));
    }
}
