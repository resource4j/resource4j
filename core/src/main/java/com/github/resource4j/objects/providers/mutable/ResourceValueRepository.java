package com.github.resource4j.objects.providers.mutable;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.objects.providers.ResourceValueProvider;
import com.github.resource4j.resources.context.ResourceResolutionContext;

public interface ResourceValueRepository extends ResourceObjectRepository, ResourceValueProvider {

    void put(ResourceKey key, ResourceResolutionContext ctx, String value);

    void remove(ResourceKey key, ResourceResolutionContext ctx);

}
