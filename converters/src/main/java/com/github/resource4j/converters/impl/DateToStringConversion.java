package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;
import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

public class DateToStringConversion implements Conversion<java.util.Date,String> {

    @Override
    public Set<ConversionPair> acceptedTypes() {
        Set<ConversionPair> conversionPairs = new HashSet<>();
        conversionPairs.add(new ConversionPair(java.util.Date.class, String.class));
        conversionPairs.add(new ConversionPair(java.sql.Date.class, String.class));
        return conversionPairs;
    }

    @Override
    public String convert(java.util.Date fromValue, Class<String> toType, Object format) throws TypeCastException {
        DateFormat formatter = null;
        if (format != null) {
            if (format instanceof String) {
                formatter = new SimpleDateFormat((String) format);
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            } else if (format instanceof DateFormat) {
                formatter = (DateFormat) format;
            }
        }
        if (formatter == null) {
            formatter = new SimpleDateFormat(StringToDateConversion.DEFAULT_DATETIME_FORMAT);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return formatter.format(fromValue);
    }
}
