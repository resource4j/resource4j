package com.github.resource4j.objects.providers.mutable;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.ByteArrayResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.providers.resolvers.DefaultObjectNameResolver;
import com.github.resource4j.objects.providers.resolvers.ObjectNameResolver;
import com.github.resource4j.objects.providers.events.ResourceObjectRepositoryEventDispatcher;
import com.github.resource4j.objects.providers.events.ResourceObjectRepositoryListener;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Resource object repository which stores data in heap.
 * Currently storage is implemented as ConcurrentHashMap, but it may change in future.
 */
public class HeapResourceObjectRepository implements ResourceObjectRepository {

    public static final Predicate<ResourceObjectDetails> UNUSED = details -> details.readCount() == 0;

    public static Predicate<ResourceObjectDetails> olderThan(long timestamp) {
        return details -> details.timestamp() < timestamp;
    }

    public static Predicate<ResourceObjectDetails> largerThan(int size) {
        return details -> details.size() > size;
    }

    private Map<String, ObjectHolder> content = new ConcurrentHashMap<>();

    private Clock clock;

    private AtomicLong footprint = new AtomicLong();

    /**
     * used as generator of unique object keys
     */
    private ObjectNameResolver resolver = new DefaultObjectNameResolver();

    private ResourceObjectRepositoryEventDispatcher dispatcher = new ResourceObjectRepositoryEventDispatcher(this);

    public HeapResourceObjectRepository(Clock clock) {
        this.clock = clock;
    }

    @Override
    public String toString() {
        return "heap:" + this.hashCode();
    }

    /**
     * Returns approximate memory footprint of this repository (may differ from actual, since footprint value is cached
     * and calculation of it is not atomic with repository update.
     * @return approximate memory footprint in bytes
     */
    public long footprint() {
        return footprint.get();
    }

    public List<ResourceObject> find(Predicate<ResourceObjectDetails> searchCriteria) {
        return content.entrySet().stream()
                .filter(entry -> searchCriteria.test(entry.getValue()))
                .map(entry -> asResourceObject(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean contains(String name, ResourceResolutionContext context) {
        String resolvedName = resolver.resolve(name, context);
        return content.containsKey(resolvedName);
    }

    @Override
    public void put(String name, ResourceResolutionContext context, byte[] data) {
        String resolvedName = resolver.resolve(name, context);
        ObjectHolder newHolder = new ObjectHolder(name, data, clock.millis());
        ObjectHolder existingHolder = content.putIfAbsent(resolvedName, newHolder);
        if (existingHolder != null) {
            int delta = existingHolder.update(data);
            footprint.addAndGet(delta);
            dispatcher.objectCreated(name, context);
        } else {
            footprint.addAndGet(newHolder.footprint());
            dispatcher.objectRemoved(name, context);
        }
    }

    @Override
    public void remove(String name, ResourceResolutionContext context) {
        String resolvedName = resolver.resolve(name, context);
        ObjectHolder removed = content.remove(resolvedName);
        footprint.addAndGet(-removed.footprint());
        dispatcher.objectRemoved(name, context);
    }

    @Override
    public ResourceObject get(String name, ResourceResolutionContext context) throws MissingResourceObjectException {
        String resolvedName = resolver.resolve(name, context);
        ObjectHolder holder = content.get(resolvedName);
        if (holder == null) {
            throw new MissingResourceObjectException(name, resolvedName);
        }
        holder.readCounter.incrementAndGet();
        return asResourceObject(resolvedName, holder);
    }

    @Override
    public void addListener(ResourceObjectRepositoryListener listener) {
        dispatcher.addListener(listener);
    }

    @Override
    public void removeListener(ResourceObjectRepositoryListener listener) {
        dispatcher.removeListener(listener);
    }

    private ByteArrayResourceObject asResourceObject(String resolvedName, ObjectHolder holder) {
        return new ByteArrayResourceObject(holder.name, resolvedName, holder.data, holder.timestamp);
    }

    public interface ResourceObjectDetails {
        long timestamp();
        int readCount();
        int size();
    }

    /**
     * Structure for storing object data with creation timestamp
     */
    private static class ObjectHolder implements ResourceObjectDetails {

        public static final int SIZE_OF = 48;

        private AtomicInteger readCounter = new AtomicInteger();

        private final String name;

        private byte[] data;

        private long timestamp;

        public ObjectHolder(String name, byte[] content, long timestamp) {
            this.name = name != null ? name : "";
            this.data = content;
            this.timestamp = timestamp;
        }

        public int update(byte[] newData) {
            int delta = newData.length - data.length;
            data = newData;
            return delta;
        }

        public long timestamp() {
            return timestamp;
        }

        public int size() {
            return data.length;
        }

        public int readCount() {
            return readCounter.get();
        }

        public int footprint() {
            return data.length + name.length() * 2 + SIZE_OF;
        }
    }

}
