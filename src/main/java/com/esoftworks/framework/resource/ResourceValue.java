package com.esoftworks.framework.resource;


public interface ResourceValue<V> {

	ResourceKey key();

	V asIs();
	
}
