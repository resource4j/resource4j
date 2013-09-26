package com.github.resource4j.generic;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.MandatoryValue;
import com.github.resource4j.MissingValueException;
import com.github.resource4j.ResourceKey;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public class GenericMandatoryString extends GenericResourceString implements MandatoryString {

    protected GenericMandatoryString(ResourceKey key, String value) {
        this(key, value, null);
    }

    protected GenericMandatoryString(ResourceKey key, String value, Throwable cause) {
        super(key, value);
        if (value == null) {
            throw new MissingValueException(key, cause);
        }
    }

    @Override
    public <T> MandatoryValue<T> ofType(Class<T> type) {
        return new GenericMandatoryValue<T>(key, as(type));
    }

}
