package com.github.resource4j.refreshable;

import java.util.Map;
import java.util.function.Consumer;

import com.github.resource4j.OptionalString;
import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.resources.AbstractResources;
import com.github.resource4j.resources.cache.CachedValue;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class RefreshableResources extends AbstractResources {

	private ValueCache valueCache;
	
	private ContentCache contentCache;
	
//	private Parser parser;
	
	@Override
	public OptionalString get(ResourceKey key, ResourceResolutionContext context) {
		CachedValue<OptionalString> value = valueCache.get(key, context);
		if (!value.isLoaded()) {
			String bundle = key.getBundle();
			fireLoadResourceValueRequest(key, 
					context, 
					val -> valueCache.put(key, val, context));
		}
		value = valueCache.get(key, context);
		return value.get();
	}

	@Override
	public ResourceFile contentOf(String name, ResourceResolutionContext context) {
		CachedValue<ResourceFile> value = contentCache.get(name, context);
		if (!value.isLoaded()) {
			fireLoadResourceFileRequest(name, 
					context, 
					resource -> contentCache.put(name, resource, context));
		}
		value = contentCache.get(name, context);
		return value.get();
	}
	
	private void parseToCache(String bundle, ResourceFile file, ResourceResolutionContext context) {
//		Map<String,String> values = parser.parse(file);
		
	}
	
	private void fireLoadResourceValueRequest(ResourceKey key, 
			ResourceResolutionContext context, 
			Consumer<OptionalValue<?>> consumer) {
		
	}
	
	private void fireLoadResourceFileRequest(String bundle, 
			ResourceResolutionContext context, Consumer<ResourceFile> consumer) {	
		
	}

}
