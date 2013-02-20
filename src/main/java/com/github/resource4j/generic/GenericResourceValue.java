package com.github.resource4j.generic;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceValue;

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
