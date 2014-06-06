package com.github.resource4j;

/**
 * @author Ivan Gammel
 * @since 1.0
 * @see ResourceValue
 */
public class ResourceKey implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private final String bundle;

    private final String id;

    protected ResourceKey(String bundle, String id) {
        this.bundle = bundle;
        this.id = id;
    }

    //
    // Factory methods
    //
    /**
     * Constructs new resource key from plain string according to following rules:<ul>
     * <li>Last dot in the string separates key bundle from key id.</li> 
     * <li>Strings starting from the dot represent a key with <code>null</code> bundle (default bundle).</li>
     * <li>Strings ending with a dot represent keys with <code>null</code> id (bundles).</li> 
     * </ul>
     * This operation is reverse to {@link #toString()} method, that is following statements are true:<ul>
     * <li>string.equals(plain(string).toString())</li>
     * <li>key.equals(plain(key.toString()))</li>
     * </ul>
     * @param key string representation of a key
     * @return a key corresponding to given string
     */
	public static final ResourceKey plain(String key) {
		int idx = key.lastIndexOf('.');
		if (idx < 0) {
			return new ResourceKey(null, key);
		}
		String bundle = idx > 0 ? key.substring(0, idx) : null;
		String id = idx < key.length() - 1 ? key.substring(idx + 1) : null;
		return key(bundle, id);
	}
	
    /**
     *
     * @param clazz
     * @return
     */
    public static ResourceKey bundle(Class<?> clazz) {
        return new ResourceKey(clazz.getName(), null);
    }

    /**
     *
     * @param bundle
     * @return
     */
    public static ResourceKey bundle(String bundle) {
        return new ResourceKey(bundle, null);
    }

    /**
     *
     * @param key
     * @return
     */
    public static ResourceKey key(String key) {
        return new ResourceKey(null, key);
    }

    /**
     * @param value
     * @return
     */
    public static ResourceKey key(Enum<?> value) {
        return new ResourceKey(value.getClass().getName(), value.name());
    }

    /**
     *
     * @param clazz
     * @param id
     * @return
     */
    public static ResourceKey key(Class<?> clazz, String id) {
        return new ResourceKey(clazz.getName(), id);
    }

    /**
     *
     * @param clazz
     * @param value
     * @return
     */
    public static ResourceKey key(Class<?> clazz, Enum<?> value) {
        return new ResourceKey(clazz.getName(), value.name());
    }
    /**
     *
     * @param bundle
     * @param id
     * @return
     */
    public static ResourceKey key(String bundle, String id) {
        return new ResourceKey(bundle, id);
    }

    /**
     *
     * @param enumValue
     * @param key
     * @return
     */
    public static <E extends Enum<E>> ResourceKey key(E enumValue, String key) {
        return new ResourceKey(enumValue.getClass().getName(), enumValue.name()).child(key);
    }

    //
    // Public API
    //

    /**
     *
     * @param childId
     * @return
     */
    public ResourceKey child(String childId) {
        return new ResourceKey(bundle, id == null ? childId : id+'.'+childId);
    }

    /**
     *
     * @return
     */
    public String getBundle() {
        return bundle;
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bundle == null) ? 0 : bundle.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * Two keys are equal if they have same bundle and same id.
     */
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

    /**
     * This method is reverse to {@link #plain(String)}.
     * @return string representation of this key. 
     */
    @Override
    public String toString() {
        return (bundle != null ? bundle : "") + '.' + (id != null ? id : "");
    }

}
