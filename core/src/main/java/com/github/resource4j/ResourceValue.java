package com.github.resource4j;

/**
 * A wrapper of single value from the resource file. Values are identified by keys and can be of any type.
 * @author Ivan Gammel
 * @since 1.0
 *
 * @param <V> type of the managed value.

 * @see ResourceKey
 */
public interface ResourceValue<V> {
	
    /**
     * @return the key identifying this value.
     * @since 1.0
     */
    ResourceKey key();

    /**
     * @return the wrapper value "as is", that is, without any type conversions.
     * @since 1.0
     */
    V asIs();

}
