package com.github.resource4j.objects.providers;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.context.ResourceResolutionContext;

/**
 * Provider that allows to retrieve single value instead of a bundle
 */
public interface ResourceValueProvider extends ResourceObjectProvider {

    OptionalString get(ResourceKey key, ResourceResolutionContext ctx);

}
