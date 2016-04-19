package com.github.resource4j.objects.providers;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.context.ResourceResolutionContext;

public interface ResourceValueProvider extends ResourceObjectProvider {

    OptionalString get(ResourceKey key, ResourceResolutionContext ctx);

}
