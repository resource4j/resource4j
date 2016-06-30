package com.github.resource4j.resources;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.resources.cache.Cache;
import com.github.resource4j.resources.cache.CachedBundle;
import com.github.resource4j.resources.cache.CachedObject;
import com.github.resource4j.resources.cache.CachedValue;
import com.github.resource4j.resources.impl.ResolvedKey;
import com.github.resource4j.resources.impl.ResolvedName;
import com.github.resource4j.resources.processors.ResourceValuePostProcessor;

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

    void configureObjectPipeline(Consumer<Cache<ResolvedName, ResourceObject>> cacheConsumer,
                                 Consumer<ExecutorService> poolConsumer);

    void configurePostProcessing(Consumer<ResourceValuePostProcessor> consumer);
}
