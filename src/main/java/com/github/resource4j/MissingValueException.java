package com.github.resource4j;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public class MissingValueException extends ResourceException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ResourceKey key;

    public MissingValueException(ResourceKey key, Throwable cause) {
        super(String.valueOf(key), cause);
        this.key = key;
    }

    public ResourceKey getKey() {
        return key;
    }

}
