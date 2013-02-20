package com.github.resource4j;


public interface ResourceValue<V> {

	ResourceKey key();

	V asIs();
	
}
