package com.esoftworks.framework.resource.generic;

import com.esoftworks.framework.resource.ResourceKey;
import com.esoftworks.framework.resource.ResourceValue;

public abstract class GenericResourceValue<V> implements ResourceValue<V> {

	protected final ResourceKey key;
	
	protected final V value;
	
	protected GenericResourceValue(ResourceKey key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	@Override
	public ResourceKey key() {
		return key;
	}

	@Override
	public V asIs() {
		return value;
	}
}
