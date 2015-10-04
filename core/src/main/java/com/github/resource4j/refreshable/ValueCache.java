package com.github.resource4j.refreshable;

import java.util.Map;

import com.github.resource4j.OptionalString;
import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.resources.cache.CachedValue;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class ValueCache {

	public CachedValue<OptionalString> get(ResourceKey key, ResourceResolutionContext context) {
		return null;
	}
	
	public void update(String bundle, 
			Map<String,String> values, 
			ResourceResolutionContext context,
			ResourceFile source) {
		
	}

	public void put(ResourceKey key, OptionalValue<?> val, ResourceResolutionContext context) {
	}

}
