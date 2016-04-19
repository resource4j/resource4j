package com.github.resource4j.objects.providers.mutable;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.ByteArrayResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.providers.events.ResourceObjectRepositoryEventDispatcher;
import com.github.resource4j.objects.providers.events.ResourceObjectRepositoryListener;
import com.github.resource4j.objects.providers.resolvers.DefaultObjectNameResolver;
import com.github.resource4j.objects.providers.resolvers.ObjectNameResolver;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.values.GenericOptionalString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.Clock;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.resource4j.objects.providers.events.ResourceObjectRepositoryEvent.*;

/**
 * Resource object repository which stores data in heap.
 * Currently storage is implemented as ConcurrentHashMap, but it may change in future.
 */
public class HeapResourceObjectRepository implements ResourceValueRepository {

    public static final Predicate<ResourceObjectDetails> UNUSED = details -> details.readCount() == 0;

    public static Predicate<ResourceObjectDetails> olderThan(long timestamp) {
        return details -> details.timestamp() < timestamp;
    }

    private static final Logger LOG = LoggerFactory.getLogger(HeapResourceObjectRepository.class);

    private ConcurrentMap<String, ObjectHolder> content = new ConcurrentHashMap<>();

    private Clock clock;

    /**
     * used as generator of unique object keys
     */
    private ObjectNameResolver resolver = new DefaultObjectNameResolver();

    private ResourceObjectRepositoryEventDispatcher dispatcher = new ResourceObjectRepositoryEventDispatcher(this);

    private Marker marker;

    public HeapResourceObjectRepository(Clock clock) {
        this.clock = clock;
        this.marker = MarkerFactory.getMarker(toString());
    }

    @Override
    public String toString() {
        return "heap:" + this.hashCode();
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
            existingHolder.data = data;
            LOG.trace(marker, "Object modified: {} @ {}", name, context);
            dispatcher.repositoryUpdated(modified(toString(), name, context));
        } else {
            LOG.trace(marker, "Object added: {} @ {}", name, context);
            dispatcher.repositoryUpdated(created(toString(), name, context));
        }
    }

    @Override
    public void remove(String name, ResourceResolutionContext context) {
        String resolvedName = resolver.resolve(name, context);
        content.remove(resolvedName);
        LOG.trace(marker, "Object removed: {} @ {}", name, context);
        dispatcher.repositoryUpdated(deleted(toString(), name, context));
    }

    @Override
    public ResourceObject get(String name, ResourceResolutionContext context) throws MissingResourceObjectException {
        GetResult result = doGet(name, context);
        return asResourceObject(result.resolvedName, result.holder);
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
        byte[] bytes = holder.data instanceof byte[]
                ? (byte[]) holder.data
                : serialize(holder.data);
        return new ByteArrayResourceObject(holder.name, resolvedName, bytes, holder.timestamp);
    }

    @Override
    public void put(ResourceKey key, ResourceResolutionContext context, String value) {
        String name = key.objectName();
        String resolvedName = resolver.resolve(name, context);
        if (value != null) {
            ObjectHolder existingHolder = content.computeIfAbsent(resolvedName, (string) -> {
                Map<String, String> map = new ConcurrentHashMap<>();
                LOG.trace(marker, "Object added: {}@{}", resolvedName, context);
                return new ObjectHolder(name, map, clock.millis());
            });
            if (existingHolder != null) {
                Map<String, String> map = existingHolder.map();
                String old = map.put(key.getId(), value);
                existingHolder.data = map;
                if (old == null) {
                    LOG.trace(marker, "Value added: {}@{}", key, context);
                } else {
                    LOG.trace(marker, "Value modified: {}@{}", key, context);
                }
                dispatcher.repositoryUpdated(valueSet(toString(), key, context));
            }
        } else {
            ObjectHolder holder = content.get(resolvedName);
            if (holder != null) {
                Map<String, String> map = holder.map();
                map.remove(key.getId());
                LOG.trace(marker, "Value removed: {}@{}", key, context);
                dispatcher.repositoryUpdated(valueRemoved(toString(), key, context));
            }
        }
    }

    @Override
    public void remove(ResourceKey key, ResourceResolutionContext ctx) {
        put(key, ctx, null);
    }

    protected static class GetResult {
        public String resolvedName;
        public ObjectHolder holder;
        public GetResult(String resolvedName, ObjectHolder holder) {
            this.resolvedName = resolvedName;
            this.holder = holder;
        }
    }


    @Override
    public OptionalString get(ResourceKey key, ResourceResolutionContext context) {
        try {
            GetResult result = doGet(key.objectName(), context);
            Map<String,String> map = result.holder.data instanceof Map
                    ? (Map<String,String>) result.holder.data
                    : Collections.emptyMap();
            String value = map.get(key.getId());
            return new GenericOptionalString(result.resolvedName, key, value);
        } catch (MissingResourceObjectException e) {
            return new GenericOptionalString(e.getActualName(), key, null, e);
        }
    }

    private GetResult doGet(String name, ResourceResolutionContext context) throws MissingResourceObjectException {
        String resolvedName = resolver.resolve(name, context);
        ObjectHolder holder = content.get(resolvedName);
        if (holder == null) {
            throw new MissingResourceObjectException(name, resolvedName);
        }
        holder.readCounter.incrementAndGet();
        return new GetResult(resolvedName, holder);
    }

    protected static byte[] serialize(Object map) {
        try {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bs);
            os.writeObject(map);
            os.close();
            bs.close();
            return bs.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public interface ResourceObjectDetails {
        long timestamp();
        int readCount();
    }

    /**
     * Structure for storing object data with creation timestamp
     */
    private static class ObjectHolder implements ResourceObjectDetails {

        private AtomicInteger readCounter = new AtomicInteger();

        private final String name;

        private Object data;

        private long timestamp;

        public ObjectHolder(String name, Object content, long timestamp) {
            this.name = name != null ? name : "";
            this.data = content;
            this.timestamp = timestamp;
        }

        public long timestamp() {
            return timestamp;
        }

        public int readCount() {
            return readCounter.get();
        }

        @SuppressWarnings("unchecked")
        public Map<String,String> map() {
            if (data instanceof Map) {
                return (Map<String,String>) data;
            } else {
                throw new ResourceObjectIsNotBundleException(name);
            }
        }
    }

}
