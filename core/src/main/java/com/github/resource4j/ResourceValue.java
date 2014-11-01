package com.github.resource4j;

import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

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

}
