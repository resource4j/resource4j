package com.github.resource4j.resources;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.resources.cache.Cache;
import com.github.resource4j.resources.cache.CachedBundle;
import com.github.resource4j.resources.cache.CachedValue;
import com.github.resource4j.resources.cache.impl.BasicValueCache;
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

public class ResourcesConfiguratorBuilder implements Supplier<ResourcesConfiguratorBuilder.Configurator> {

    private List<ResourceObjectProvider> providers = singletonList(classpath());
    private ResourceKey defaultBundle = ResourceKey.bundle("i18n.resources");
    private List<BundleFormat> formats = singletonList(format(propertyMap()));
    private ResourceValuePostProcessor valuePostProcessor = null;

    private Supplier<BasicValueCache<ResolvedKey, CachedValue>> valueCache = BasicValueCache::new;
    private Supplier<ExecutorService> valueExecutor = () -> buildThreadPool("value");

    private Supplier<BasicValueCache<ResolvedName, CachedBundle>> bundleCache = BasicValueCache::new;
    private Supplier<ExecutorService> bundleExecutor = () -> buildThreadPool("bundle");

    private Supplier<BasicValueCache<ResolvedName, ResourceObject>> objectCache = BasicValueCache::new;
    private Supplier<ExecutorService> objectExecutor = () -> buildThreadPool("object");

    private int poolSize = 2;

    public static ResourcesConfiguratorBuilder configure() {
        return new ResourcesConfiguratorBuilder();
    }

    public ResourcesConfiguratorBuilder sources(ResourceObjectProvider... providers) {
        this.providers = Arrays.asList(providers);
        return this;
    }

    public ResourcesConfiguratorBuilder defaultBundle(String name) {
        this.defaultBundle = ResourceKey.bundle(name);
        return this;
    }

    public ResourcesConfiguratorBuilder formats(BundleFormat... formats) {
        this.formats = Arrays.asList(formats);
        return this;
    }

    public ResourcesConfiguratorBuilder poolSize(int threadsPerPool) {
        this.poolSize = threadsPerPool;
        return this;
    }

    public ResourcesConfiguratorBuilder postProcessingBy(ResourceValuePostProcessor postProcessor) {
        this.valuePostProcessor = postProcessor;
        return this;
    }

    public Configurator get() {
        return new Configurator();
    }

    protected ExecutorService buildThreadPool(String name) {
        return Executors.newFixedThreadPool(poolSize, new NamedThreadFactory(name + "-loader"));
    }

    public class Configurator implements RefreshableResourcesConfigurator {

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
        public void configurePostProcessing(Consumer<ResourceValuePostProcessor> consumer) {
            consumer.accept(valuePostProcessor);
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
            return new Thread(r, threadName);

        }
    }
}
