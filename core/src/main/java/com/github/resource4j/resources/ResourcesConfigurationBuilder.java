package com.github.resource4j.resources;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.resources.cache.Cache;
import com.github.resource4j.resources.cache.CachedBundle;
import com.github.resource4j.resources.cache.CachedValue;
import com.github.resource4j.resources.cache.Caches;
import com.github.resource4j.resources.impl.ResolvedKey;
import com.github.resource4j.resources.impl.ResolvedName;
import com.github.resource4j.resources.processors.ResourceValuePostProcessor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.github.resource4j.objects.parsers.ResourceParsers.propertyMap;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.classpath;
import static com.github.resource4j.resources.BundleFormat.format;
import static java.util.Collections.singletonList;

public class ResourcesConfigurationBuilder implements Supplier<ResourcesConfigurationBuilder.Configuration> {

    private List<ResourceObjectProvider> providers = singletonList(classpath());
    private ResourceKey defaultBundle = ResourceKey.bundle("i18n.resources");
    private List<BundleFormat> formats = singletonList(format(propertyMap()));
    private ResourceValuePostProcessor valuePostProcessor = null;
    private ParametrizedKeyBuilder keyBuilder = null;

    private Supplier<Cache<ResolvedKey, CachedValue>> valueCache = Caches.always();
    private Supplier<ExecutorService> valueExecutor = () -> buildThreadPool("value");

    private Supplier<Cache<ResolvedName, CachedBundle>> bundleCache = Caches.always();
    private Supplier<ExecutorService> bundleExecutor = () -> buildThreadPool("bundle");

    private Supplier<Cache<ResolvedName, ResourceObject>> objectCache = Caches.always();
    private Supplier<ExecutorService> objectExecutor = () -> buildThreadPool("object");

    private int poolSize = 2;

    private int maxDepth = RefreshableResources.DEFAULT_MAX_DEPTH;

    public static ResourcesConfigurationBuilder configure() {
        return new ResourcesConfigurationBuilder();
    }

    public ResourcesConfigurationBuilder sources(ResourceObjectProvider... providers) {
        this.providers = Arrays.asList(providers);
        return this;
    }

    public ResourcesConfigurationBuilder defaultBundle(String name) {
        this.defaultBundle = ResourceKey.bundle(name);
        return this;
    }

    /**
     * Define acceptable formats for resource bundles (only .properties are included by default).
     * When bundle with the same name exists in multiple formats, priority increases from first to last
     * format in this list. E.g. if formats are defined in the order (properties, json), then if both
     * bundles exist, json bundle will be taken.
     * @param formats acceptable formats
     * @return this
     */
    public ResourcesConfigurationBuilder formats(BundleFormat... formats) {
        this.formats = Arrays.asList(formats);
        return this;
    }

    public ResourcesConfigurationBuilder poolSize(int threadsPerPool) {
        this.poolSize = threadsPerPool;
        return this;
    }

    public ResourcesConfigurationBuilder cacheValues(Supplier<Cache<ResolvedKey, CachedValue>> cache) {
        this.valueCache = cache;
        return this;
    }

    public ResourcesConfigurationBuilder cacheBundles(Supplier<Cache<ResolvedName, CachedBundle>> cache) {
        this.bundleCache = cache;
        return this;
    }

    public ResourcesConfigurationBuilder cacheObjects(Supplier<Cache<ResolvedName, ResourceObject>> cache) {
        this.objectCache = cache;
        return this;
    }

    public ResourcesConfigurationBuilder maxCycleDepth(int depth) {
        this.maxDepth = depth;
        return this;
    }

    public ResourcesConfigurationBuilder postProcessingBy(ResourceValuePostProcessor postProcessor) {
        this.valuePostProcessor = postProcessor;
        return this;
    }

    public ResourcesConfigurationBuilder buildParamKeysWith(ParametrizedKeyBuilder kb) {
        this.keyBuilder = kb;
        return this;
    }

    public Configuration get() {
        return new Configuration();
    }

    protected ExecutorService buildThreadPool(String name) {
        return Executors.newFixedThreadPool(poolSize, new NamedThreadFactory(name + "-loader"));
    }

    public class Configuration implements RefreshableResourcesConfigurator {

        @Override
        public void configureSources(Consumer<List<ResourceObjectProvider>> consumer) {
            consumer.accept(providers);
        }

        @Override
        public void configureDefaultBundle(Consumer<ResourceKey> consumer) {
            consumer.accept(defaultBundle);
        }

        @Override
        public void configureFormats(Consumer<List<BundleFormat>> consumer) {
            consumer.accept(formats);
        }

        @Override
        public void configureMaxDepth(Consumer<Integer> consumer) {
            consumer.accept(maxDepth);
        }

        @Override
        public void configurePostProcessing(Consumer<ResourceValuePostProcessor> consumer) {
            consumer.accept(valuePostProcessor);
        }

        @Override
        public void configureKeyBuilder(Consumer<ParametrizedKeyBuilder> consumer) {
            consumer.accept(keyBuilder);
        }

        @Override
        public void configureValuePipeline(Consumer<Cache<ResolvedKey, CachedValue>> cacheConsumer,
                                           Consumer<ExecutorService> poolConsumer) {
            cacheConsumer.accept(valueCache.get());
            poolConsumer.accept(valueExecutor.get());
        }

        @Override
        public void configureBundlePipeline(Consumer<Cache<ResolvedName, CachedBundle>> cacheConsumer,
                                            Consumer<ExecutorService> poolConsumer) {
            cacheConsumer.accept(bundleCache.get());
            poolConsumer.accept(bundleExecutor.get());
        }

        @Override
        public void configureObjectPipeline(Consumer<Cache<ResolvedName, ResourceObject>> cacheConsumer,
                                            Consumer<ExecutorService> poolConsumer) {
            cacheConsumer.accept(objectCache.get());
            poolConsumer.accept(objectExecutor.get());
        }
    }

    static class NamedThreadFactory implements ThreadFactory {
        private final AtomicInteger counter = new AtomicInteger();
        private final String name;
        private static final String THREAD_NAME_PATTERN = "%s-%d";

        public NamedThreadFactory(String name) {
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            final String threadName = String.format(THREAD_NAME_PATTERN, name, counter.incrementAndGet());
            Thread thread = new Thread(r, threadName);
            thread.setDaemon(true);
            return thread;

        }
    }
}
