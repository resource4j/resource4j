package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AutoBoxingConversion implements PrimitiveTypeConversion<Object,Object> {

    @Override
    public Set<ConversionPair> acceptedTypes() {
        Set<ConversionPair> conversionPairs = new HashSet<>();
        for (Map.Entry<Class<?>,Class<?>> entry : PRIMITIVE_TYPE_MAPPING.entrySet()) {
            conversionPairs.add(new ConversionPair(entry.getKey(), entry.getValue()));
            conversionPairs.add(new ConversionPair(entry.getValue(), entry.getKey()));
        }
        return conversionPairs;
    }

    @Override
    public Object convert(Object fromValue, Class<Object> toType, Object pattern) throws TypeCastException {
        return fromValue;
    }


}
