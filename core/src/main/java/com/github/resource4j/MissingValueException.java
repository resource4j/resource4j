package com.github.resource4j;

import com.github.resource4j.resources.Resources;

/**
 * Exception thrown when unresolved {@link OptionalValue} is converted to {@link MandatoryValue}.
 * @author Ivan Gammel
 * @since 1.0
 * @see Resources
 */
public class MissingValueException extends ResourceException {

    private static final long serialVersionUID = 1L;

    private ResourceKey key;

    public MissingValueException(ResourceKey key, Throwable cause) {
        super(String.valueOf(key), cause);
        this.key = key;
    }

    /**
     * Returns the key of unresolved value
     * @return the key of unresolved value
     */
    public ResourceKey getKey() {
        return key;
    }

}
