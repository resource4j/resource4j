package com.github.resource4j;

import com.github.resource4j.resources.Resources;

/**
 * Resource key is a reference to a resource value that consists of bundle name and identifier of value within bundle.
 * @see Resources
 * @see ResourceValue
 * 
 * @author Ivan Gammel
 * @since 1.0
 */
public class ResourceKey implements java.io.Serializable {

    public static final char BUNDLE_ID_SEPARATOR = '.';

	public static final char ID_COMPONENT_DELIMITER = '_';

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
     * This operation is inversion of {@link #toString()} method for identifiers 
     * not containing {@link #ID_COMPONENT_DELIMITER} character:<ul>
     * <li>string.equals(plain(string).toString())</li>
     * <li>key.equals(plain(key.toString()))</li>
     * </ul>
     * @param key string representation of a key
     * @return the key corresponding to given string
     */
	public static final ResourceKey plain(String key) {
		int idx = key.lastIndexOf(BUNDLE_ID_SEPARATOR);
		if (idx < 0) {
			return new ResourceKey(null, key);
		}
		String bundle = idx > 0 ? key.substring(0, idx) : null;
		String id = idx < key.length() - 1 ? key.substring(idx + 1) : null;
		return key(bundle, id);
	}


    public ResourceKey relative(String id) {
        ResourceKey full = plain(id);
        if (full.getBundle() == null) return key(bundle, id);
        return full;
    }


    /**
     * Creates a bundle key corresponding to given class.
     * @param clazz a class owning the bundle
     * @return the bundle key for given class
     */
    public static ResourceKey bundle(Class<?> clazz) {
        return new ResourceKey(clazz.getName(), null);
    }

    /**
     * Creates a bundle key by given name.
     * @see ResourceKey class description for details.
     * @param bundle name of the bundle
     * @return the bundle key with given bundle name
     */
    public static ResourceKey bundle(String bundle) {
        return new ResourceKey(bundle, null);
    }

    /**
     * Creates a resource key for value stored in default bundle.
     * @param key identifier of value in default value.
     * @return the key for value in default bundle
     */
    public static ResourceKey key(String key) {
        return new ResourceKey(null, key);
    }

    /**
     * Creates a resource key for given enumeration value. By convention, 
     * resource bundle for enumerations has the name of enumeration class 
     * and value identifier is the same as enumeration value name.
     * @param value the enumeration value 
     * @return the resource key
     */
    public static ResourceKey key(Enum<?> value) {
        return new ResourceKey(value.getClass().getName(), value.name());
    }

    /**
     * Creates a resource key with given id for bundle specified by given class.
     * @param clazz the class owning the bundle.
     * @param id value identifier
     * @return the resource key
     */
    public static ResourceKey key(Class<?> clazz, String id) {
        return new ResourceKey(clazz.getName(), id);
    }

    /**
     * Creates a resource key with id defined as enumeration value name and bundle specified by given class.
     * @param clazz the class owning the bundle
     * @param value enumeration value used to define key id
     * @return the resource key
     */
    public static ResourceKey key(Class<?> clazz, Enum<?> value) {
        return new ResourceKey(clazz.getName(), value.name());
    }
    /**
     * Creates a resource key with given bundle name and identifier
     * @param bundle the bundle name
     * @param id the key identifier within bundle
     * @return the resource key
     */
    public static ResourceKey key(String bundle, String id) {
        return new ResourceKey(bundle, id);
    }

    /**
     * Creates a resource key defined as a child of key defined by enumeration value.
     * @see #key(Enum)
     * @see #child(String)
     * @param enumValue the enumeration value defining the parent key
     * @param key the child id
     * @return the resource key
     */
    public static ResourceKey key(Enum<?> enumValue, String key) {
        return new ResourceKey(enumValue.getClass().getName(), enumValue.name()).child(key);
    }

    //
    // Public API
    //

    /**
     * Derives new resource key from this key with identifier as defined below:
     * <pre>
     * &lt;delimiter&gt; ::= '_'
     * &lt;id&gt; ::= &lt;this id&gt; &lt;delimiter&gt; &lt;child id&gt;
     * </pre>  
     * Example:
     * <pre>
     * ResourceKey k1 = key("mybundle","value").child("name");
     * ResourceKey k2 = key("mybundle", "value.name");
     * 
     * k1.equals(k2) == true
     * </pre>
     * @param childId child identifier
     * @return new key with child identifier
     */
    public ResourceKey child(String childId) {
        return new ResourceKey(bundle, id == null ? childId : join(id, childId));
    }

    /**
     * Returns resource key representing the bundle in which this key is defined.
     * @return the key representing the bundle
     */
    public ResourceKey bundle() {
    	return new ResourceKey(bundle, null);
    }

    /**
     * Returns unresolved name of resource object associated with this key
     * @return unresolved name of resource object
     */
    public String objectName() {
        if (bundle == null) {
            return null;
        }
        return bundle.replace('.', '/');
    }

    /**
     * Returns name of the bundle for this key
     * @return the bundle name
     */
    public String getBundle() {
        return bundle;
    }

    /**
     * Returns identifier of this key
     * @return the identifier
     */
    public String getId() {
        return id;
    }

    /**
     * The key hash code is calculated based on bundle name and identifier.
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
        return (bundle != null ? bundle : "") + BUNDLE_ID_SEPARATOR + (id != null ?  id : "");
    }

	public static String join(String id, String subkey) {
		return id + ID_COMPONENT_DELIMITER + subkey;
	}

}
