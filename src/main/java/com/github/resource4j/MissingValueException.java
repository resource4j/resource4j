package com.github.resource4j;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public class MissingValueException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ResourceKey key;

    public MissingValueException(ResourceKey key) {
        super(String.valueOf(key));
        this.key = key;
    }

    public ResourceKey getKey() {
        return key;
    }

}
