package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;
import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DateToLocalDateConversion implements Conversion<Date, LocalDate> {

    @Override
    public Set<ConversionPair> acceptedTypes() {
        Set<ConversionPair> conversionPairs = new HashSet<>();
        conversionPairs.add(new ConversionPair(Date.class, LocalDate.class));
        conversionPairs.add(new ConversionPair(java.sql.Date.class, LocalDate.class));
        return conversionPairs;
    }

    @Override
    public LocalDate convert(Date fromValue, Class<LocalDate> toType, Object format) throws TypeCastException {
        if (fromValue instanceof java.sql.Date) {
            return ((java.sql.Date) fromValue).toLocalDate();
        } else try {
            ZoneId zone = TimeConversions.toZoneId(format);
            return fromValue.toInstant().atZone(zone).toLocalDate();
        } catch (DateTimeException e) {
            throw new TypeCastException(fromValue, Date.class, toType, e);
        }
    }

}
