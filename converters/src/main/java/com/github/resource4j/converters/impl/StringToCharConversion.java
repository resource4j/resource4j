package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.util.Optional;
import java.util.Set;

import static com.github.resource4j.converters.ConversionPair.of;
import static com.github.resource4j.converters.ConversionPair.pairs;

public class StringToCharConversion implements PrimitiveTypeConversion<String,Character> {

    @Override
    public Set<ConversionPair> acceptedTypes() {
        return pairs(of(String.class, Character.class), of(String.class, Character.TYPE));
    }

    @Override
    public Character convert(String fromValue, Class<Character> toType, Optional<Object> format) throws TypeCastException {
        if (fromValue.length() == 0) {
            return null;
        }
        if (fromValue.length() > 1) {
            throw new TypeCastException(fromValue, fromValue.getClass(), toType, "string is too long");
        }
        return fromValue.charAt(0);
    }
}
