package com.github.resource4j.resources.processors.strategies;

import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.resources.processors.ResourceResolver;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.util.Collections;

public class DateFormatStrategy implements PropertyResolver {
    @Override
    public Object resolve(Object value, String property, ResourceResolutionContext context, ResourceResolver resolver) {
        if (value instanceof TemporalAccessor) {
            TemporalAccessor temporal = (TemporalAccessor) value;
            Object format = resolver.get(property, Collections.emptyMap());
            if (format instanceof java.time.format.DateTimeFormatter) {
                return ((DateTimeFormatter) format).format(temporal);
            } else if (format instanceof String) {
                return new DateTimeFormatterBuilder()
                            .appendPattern((String) format)
                            .toFormatter()
                            .format(temporal);
            }
        }
        return null;
    }
}
