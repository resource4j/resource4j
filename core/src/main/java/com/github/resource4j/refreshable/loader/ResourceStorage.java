package com.github.resource4j.refreshable.loader;

import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public interface ResourceStorage<K,V> {
	
	ResourceResponse<K,V> get(long timestamp, K key, ResourceResolutionContext context);

}