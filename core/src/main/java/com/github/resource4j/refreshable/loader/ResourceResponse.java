package com.github.resource4j.refreshable.loader;

import java.util.Map;

import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class ResourceResponse<K,V> implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Request timestamp
	 */
	private long requested;
	
	/**
	 * Response timestamp
	 */
	private long received;

	private Map<K,V> data;
	
	private ResourceResolutionContext context;
	
	private String sourceId;

	public ResourceResponse(long requested, long received, 
			Map<K,V> value, ResourceResolutionContext context,
			String sourceId) {
		super();
		this.requested = requested;
		this.received = received;
		this.key = key;
		this.value = value;
		this.context = context;
		this.sourceId = sourceId;
	}

	public long getRequested() {
		return requested;
	}

	public long getReceived() {
		return received;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}
	
	public boolean isPresent() {
		return value != null;
	}

	public ResourceResolutionContext getContext() {
		return context;
	}

	public String getSourceId() {
		return sourceId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		ResourceResponse<K,V> other = (ResourceResponse<K,V>) obj;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return sourceId + ":" + key + " (" + (value != null ? "loaded" : "missing") + ")";
	}
	
}
