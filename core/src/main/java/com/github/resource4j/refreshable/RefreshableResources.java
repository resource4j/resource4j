package com.github.resource4j.refreshable;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.refreshable.cache.CachedValue;
import com.github.resource4j.refreshable.cache.ObjectCache;
import com.github.resource4j.refreshable.cache.ValueCache;
import com.github.resource4j.resources.AbstractResources;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.util.concurrent.Future;

public class RefreshableResources extends AbstractResources {

    private ValueCache values;

    private ObjectCache objects;

	@Override
	public OptionalString get(ResourceKey key, ResourceResolutionContext context) {
        return null;
	}

	@Override
	public ResourceObject contentOf(String name, ResourceResolutionContext context) {
        return null;
	}

}

