package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;
import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

public class LocalTimeToSQLTimeConversion implements Conversion<LocalTime, Time> {

    @Override
    public Set<ConversionPair> acceptedTypes() {
        return Collections.singleton(new ConversionPair(LocalTime.class, Time.class));
    }

    @Override
    public Time convert(LocalTime fromValue, Class<Time> toType, Object format) throws TypeCastException {
        return java.sql.Time.valueOf(fromValue);
    }
}
