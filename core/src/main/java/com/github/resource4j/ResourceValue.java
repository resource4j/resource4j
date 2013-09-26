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
     *
     * @return
     */
    ResourceKey key();

    /**
     *
     * @return
     */
    V asIs();

}
