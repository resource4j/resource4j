package com.github.resource4j.refreshable;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.refreshable.cache.Cache;
import com.github.resource4j.refreshable.cache.CachedBundle;
import com.github.resource4j.refreshable.cache.CachedObject;
import com.github.resource4j.refreshable.cache.CachedValue;
import com.github.resource4j.refreshable.cache.impl.BasicValueCache;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.github.resource4j.objects.parsers.ResourceParsers.propertyMap;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.classpath;
import static com.github.resource4j.refreshable.BundleFormat.format;
import static java.util.Collections.singletonList;

public class ResourcesConfiguratorBuilder implements Supplier<ResourcesConfiguratorBuilder.Configurator> {

    private List<ResourceObjectProvider> providers = singletonList(classpath());
    private ResourceKey defaultBundle = ResourceKey.bundle("i18n.resources");
    private List<BundleFormat> formats = singletonList(format(propertyMap()));

    private Supplier<BasicValueCache<ResolvedKey, CachedValue>> valueCache = BasicValueCache::new;
    private Supplier<ExecutorService> valueExecutor = () -> buildThreadPool("value");

    private Supplier<BasicValueCache<ResolvedName, CachedBundle>> bundleCache = BasicValueCache::new;
    private Supplier<ExecutorService> bundleExecutor = () -> buildThreadPool("bundle");

    private Supplier<BasicValueCache<ResolvedName, CachedObject>> objectCache = BasicValueCache::new;
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
        public void configureObjectPipeline(Consumer<Cache<ResolvedName, CachedObject>> cacheConsumer,
                                            Consumer<ExecutorService> poolConsumer) {
            cacheConsumer.accept(objectCache.get());
            poolConsumer.accept(objectExecutor.get());
        }
    }

}
