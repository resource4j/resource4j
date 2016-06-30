package com.github.resource4j.values;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceValue;

/**
 * Generic implementation of {@link ResourceValue}.
 * @author Ivan Gammel
 * @param <V> type of managed value
 */
public abstract class GenericResourceValue<V> implements ResourceValue<V> {

	protected final ResourceKey key;
	
	protected final V value;

	protected final String resolvedSource;
	
	protected GenericResourceValue(String resolvedSource, ResourceKey key, V value) {
		super();
		this.resolvedSource = resolvedSource;
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String resolvedSource() {
		return resolvedSource;
	}
	
	@Override
	public ResourceKey key() {
		return key;
	}

	@Override
	public V asIs() {
		return value;
	}
	
	@Override
	public String toString() {
		return value == null ? "<missing>" : String.valueOf(value);
	}
	
}
