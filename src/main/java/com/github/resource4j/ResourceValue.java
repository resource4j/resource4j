package com.github.resource4j;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 *
 * @param <V>
 */
public interface ResourceValue<V> {

    ResourceKey key();

    V asIs();

}
