package com.github.resource4j.refreshable.loader;

public interface ResourceLoadingListener<K, V> {

	void responseReceived(ResourceResponse<K,V> response);

}
