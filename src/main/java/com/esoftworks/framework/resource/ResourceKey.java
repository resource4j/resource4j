package com.esoftworks.framework.resource;

public class ResourceKey implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	private String bundle;
	
	private String id;

	public static ResourceKey bundle(Class<?> clazz) {
		return new ResourceKey(clazz.getName(), null);
	}
	
	public static ResourceKey bundle(String bundle) {
		return new ResourceKey(bundle, null);
	}
	
	public static ResourceKey key(Class<?> clazz, String id) {
		return new ResourceKey(clazz.getName(), id);
	}
	
	public static ResourceKey key(String bundle, String id) {
		return new ResourceKey(bundle, id);
	}
	
	public static <E extends Enum<E>> ResourceKey key(E enumValue, String key) {
		return new ResourceKey(enumValue.getClass().getName(), enumValue.name()).child(key);
	}
	
	protected ResourceKey(String bundle, String id) {
		this.bundle = bundle;
		this.id = id;
	}

	public ResourceKey child(String childId) {
		return new ResourceKey(bundle, id == null ? childId : id+'.'+childId);
	}
	
	public String getBundle() {
		return bundle;
	}
	
	public String getId() {
		return id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bundle == null) ? 0 : bundle.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ResourceKey other = (ResourceKey) obj;
		if (bundle == null) {
			if (other.bundle != null)
				return false;
		} else if (!bundle.equals(other.bundle))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return bundle+"#"+id;
	}
	
}
