package com.github.resource4j;

import com.github.resource4j.objects.providers.mutable.HeapResourceObjectRepository;
import com.github.resource4j.objects.providers.mutable.ResourceValueRepository;
import com.github.resource4j.resources.RefreshableResources;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.ResourcesConfigurationBuilder;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import org.junit.jupiter.api.BeforeEach;

import java.time.Clock;

import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;

public abstract class AbstractResource4JTest {

    private Resources resources;

    private ResourceValueRepository repository;

    @BeforeEach
    public void init() {
        ResourcesConfigurationBuilder builder = configure();
        repository = doInit(builder);
        resources = new RefreshableResources(builder.get());
    }

    public ResourceValueRepository doInit(ResourcesConfigurationBuilder builder) {
        ResourceValueRepository repository = new HeapResourceObjectRepository(Clock.systemUTC());
        builder.sources(repository);
        return repository;
    }

    protected Resources resources() {
        return resources;
    }

    protected void givenExists(String name, ResourceResolutionContext ctx, byte[] data) {
        repository.put(name, ctx, data);
    }

    protected void givenExists(ResourceKey key, ResourceResolutionContext ctx, String value) {
        repository.put(key, ctx, value);
    }

}
