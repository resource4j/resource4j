package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class NumberToStringConversion implements PrimitiveTypeConversion<Number,String> {


    @Override
    public Set<ConversionPair> acceptedTypes() {
        Set<ConversionPair> conversionPairs = new HashSet<>();
        for (Map.Entry<Class<?>,Class<?>> entry : PRIMITIVE_TYPE_MAPPING.entrySet()) {
            if (Number.class.isAssignableFrom(entry.getKey())) {
                conversionPairs.add(new ConversionPair(entry.getKey(), String.class));
                conversionPairs.add(new ConversionPair(entry.getValue(), String.class));
            }
        }
        return conversionPairs;
    }

    @Override
    public String convert(Number fromValue, Class<String> toType, Optional<Object> format) throws TypeCastException {
        NumberFormat formatter = null;
        if (format.isPresent()) {
            Object formatObject = format.get();
            if (formatObject instanceof String) {
                formatter = new DecimalFormat((String) formatObject);
            } else if (formatObject instanceof NumberFormat) {
                formatter = (NumberFormat) formatObject;
            }
        }
        if (formatter == null) {
            formatter = NumberFormat.getInstance();
        }
        return formatter.format(fromValue);
    }

    private String formatterClassName(Optional<Object> format) {
        return format.isPresent() ? format.get().getClass().getSimpleName() : "null";
    }

}
