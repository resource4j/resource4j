package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;
import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.sql.Date;
import java.util.Set;

import static com.github.resource4j.converters.ConversionPair.of;

public class UtilDateToSQLDateConversion implements Conversion<java.util.Date,java.sql.Date> {

    @Override
    public Set<ConversionPair> acceptedTypes() {
        return ConversionPair.pairs(of(java.util.Date.class, java.sql.Date.class));
    }

    @Override
    public Date convert(java.util.Date fromValue, Class<Date> toType, Object format) throws TypeCastException {
        return new Date(fromValue.getTime());
    }

}
