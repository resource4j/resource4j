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

    @Override
    public void repositoryUpdated(ResourceObjectRepositoryEvent event) {
        listeners.forEach(listener -> {
            try {
                listener.repositoryUpdated(event);
            } catch (RuntimeException e) {
                log.error("failed to notify listener {}: {} object ({}-{})",
                        listener,
                        event.type().toString().toLowerCase(),
                        event.objectName(),
                        event.context(),
                        e);
            }
        });
    }
}
