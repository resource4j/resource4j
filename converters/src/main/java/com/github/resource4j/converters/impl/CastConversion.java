package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;
import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.util.Set;

public class CastConversion implements Conversion<Object,Object> {

    @Override
    public Set<ConversionPair> acceptedTypes() {
        return null;
    }

    @Override
    public Object convert(Object fromValue, Class<Object> toType, Object format) throws TypeCastException {
        return fromValue;
    }

}
