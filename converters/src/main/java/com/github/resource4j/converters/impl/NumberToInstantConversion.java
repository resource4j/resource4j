package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;
import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NumberToInstantConversion implements Conversion<Number,Instant> {
    @Override
    public Set<ConversionPair> acceptedTypes() {
        return Stream.of(
                    new ConversionPair(Long.class, Instant.class),
                    new ConversionPair(Long.TYPE, Instant.class)
                )
                .collect(Collectors.toSet());
    }

    @Override
    public Instant convert(Number fromValue, Class<Instant> toType, Object pattern) throws TypeCastException {
        return Instant.ofEpochMilli(fromValue.longValue());
    }

}
