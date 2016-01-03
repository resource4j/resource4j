package com.github.resource4j;

import java.text.Format;

import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.util.TypeCastException;

/**
 * A wrapper of single value from the resource file. Values are identified by keys and can be of any type.
 * @author Ivan Gammel
 * @since 1.0
 *
 * @param <V> type of the managed value.
 * @see Resources
 * @see ResourceKey
 * @see ResourceResolutionContext
 */
public interface ResourceValue<V> {
	
    /**
     * Returns the key identifying this value in resolution context.
     * @return the key identifying this value.
     * @since 1.0
     */
    ResourceKey key();
    
    /**
     * Returns the wrapped value. May return <code>null</code>.
     * @return the wrapped value "as is", that is, without any type conversions.
     * @since 1.0
     */
    V asIs();

	/**
	 * Returns name of resource bundle within resolution context in which this value found:
	 * e.g. if bundle name specified in key was "example", the resolution context was 
	 * defined by Germany locale and the value was found 
	 * in <code>classpath:example-de_DE.properties</code> file, then the name of this file will be 
	 * the resolved source.
	 * @return name of resource bundle
	 * @since 3.0
	 */
	String resolvedSource();
 
	/**
	 * Converts this value to resource value of given type.
     * @param type class object for the expected type of value
     * @param <T> the expected type of value
	 * @return resource value of given type
	 * @throws TypeCastException if conversion failed
	 */
    <T> ResourceValue<T> ofType(Class<T> type) throws TypeCastException;
    
    /**
	 * Converts this value to resource string.
	 * @return resource string
	 * @throws TypeCastException if conversion failed
	 * @since 3.0
	 */
    ResourceString asString() throws TypeCastException;
    
    /**
	 * Converts this value to resource string using given conversion pattern.
	 * @param format conversion pattern
	 * @return resource string
	 * @throws TypeCastException if conversion failed
	 * @since 3.0
	 */
    ResourceString asString(String format) throws TypeCastException;
    
    /**
	 * Converts this value to resource string using given conversion pattern.
	 * @param format conversion pattern
	 * @return resource string
	 * @throws TypeCastException if conversion failed
	 * @since 3.0
	 */
    ResourceString asString(Format format) throws TypeCastException;
    
}
