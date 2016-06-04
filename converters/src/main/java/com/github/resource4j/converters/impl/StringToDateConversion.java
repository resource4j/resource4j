package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;
import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StringToDateConversion implements Conversion<String,Date> {
    /**
     * ISO 8601 date/time format
     */
    protected static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";


    @Override
    public Set<ConversionPair> acceptedTypes() {
        Set<ConversionPair> conversionPairs = new HashSet<>();
        conversionPairs.add(new ConversionPair(String.class, java.util.Date.class));
        return conversionPairs;

    }

    @Override
    public Date convert(String fromValue, Class<Date> toType, Optional<Object> pattern) throws TypeCastException {
        DateFormat format = null;
        if (pattern.isPresent()) {
            Object formatObject = pattern.get();
            if (formatObject instanceof String) {
                format = new SimpleDateFormat((String) formatObject);
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
            } else if (formatObject instanceof DateFormat) {
                format = (DateFormat) formatObject;
            }
        }
        if (format == null) {
            format = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        try {
            return format.parse(fromValue);
        } catch (ParseException e) {
            throw new TypeCastException(fromValue, fromValue.getClass(), toType, e);
        }
    }
}
