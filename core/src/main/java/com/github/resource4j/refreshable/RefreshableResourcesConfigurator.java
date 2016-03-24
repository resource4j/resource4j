package com.github.resource4j.refreshable;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.refreshable.cache.Cache;
import com.github.resource4j.refreshable.cache.CachedBundle;
import com.github.resource4j.refreshable.cache.CachedObject;
import com.github.resource4j.refreshable.cache.CachedValue;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public interface RefreshableResourcesConfigurator {

    void configureFormats(Consumer<List<BundleFormat>> consumer);

    void configureSources(Consumer<List<ResourceObjectProvider>> consumer);

    void configureDefaultBundle(Consumer<ResourceKey> consumer);

    void configureValuePipeline(Consumer<Cache<ResolvedKey, CachedValue>> cacheConsumer,
                                Consumer<ExecutorService> poolConsumer);

    void configureBundlePipeline(Consumer<Cache<ResolvedName, CachedBundle>> cacheConsumer,
                                 Consumer<ExecutorService> poolConsumer);

    void configureObjectPipeline(Consumer<Cache<ResolvedName, CachedObject>> cacheConsumer,
                                 Consumer<ExecutorService> poolConsumer);
}
