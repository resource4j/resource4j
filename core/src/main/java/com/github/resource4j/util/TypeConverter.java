package com.github.resource4j.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public final class TypeConverter {

    /**
     * ISO 8601 date format
     */
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * ISO 8601 time format
     */
    private static final String DEFAULT_TIME_FORMAT = "hh:mm:ss";
    /**
     * ISO 8601 date/time format
     */
    private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-ddThh:mm:ss";
    private static final String DEFAULT_NUMBER_FORMAT = "#.#";
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final Set<String> TRUE_VALUES;

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_MAPPING;

    static {
        Map<Class<?>, Class<?>> map = new HashMap<>();
        map.put(Boolean.TYPE, Boolean.class);
        map.put(Character.TYPE, Character.class);
        map.put(Byte.TYPE, Byte.class);
        map.put(Short.TYPE, Short.class);
        map.put(Integer.TYPE, Integer.class);
        map.put(Long.TYPE, Long.class);
        map.put(Float.TYPE, Float.class);
        map.put(Double.TYPE, Double.class);
        PRIMITIVE_TYPE_MAPPING = Collections.unmodifiableMap(map);

        Set<String> trueValues = new HashSet<>();
        trueValues.add("true");
        trueValues.add("on");
        trueValues.add("1");
        trueValues.add("enabled");
        trueValues.add("checked");
        TRUE_VALUES = Collections.unmodifiableSet(trueValues);
    }

    private TypeConverter() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T convert(Object fromValue, T toDefaultValue) {
        try {
            return (T) convert(fromValue, toDefaultValue.getClass());
        } catch (TypeCastException e) {
            return toDefaultValue;
        }
    }

    /**
     * Convert given value to given type
     *
     * @param fromValue
     *            the value to convert
     * @param toType
     *            target type
     * @param <T>
     *            type of the result
     * @return the value converted to given type
     * @throws TypeCastException
     *             if conversion was not possible
     */
    public static <T> T convert(Object fromValue, Class<T> toType) throws TypeCastException {
        return convert(toType, null, null, fromValue);
    }

    public static <T> T convert(Object fromValue, Class<T> toType, String format) throws TypeCastException {
        return convert(toType, format, null, fromValue);
    }

    public static <T> T convert(Object fromValue, Class<T> toType, Format format) throws TypeCastException {
        return convert(toType, null, format, fromValue);
    }

    //
    // Implementation
    //

    /**
     *
     * @param type
     * @param pattern
     * @param format
     * @param value
     * @return
     * @throws TypeCastException
     */
    @SuppressWarnings("unchecked")
    private static <T> T convert(Class<T> type, String pattern, Format format, Object value) throws TypeCastException {
        if (value == null)
            return null;
        if (type.isAssignableFrom(value.getClass()))
            return (T) value;
        if (PRIMITIVE_TYPE_MAPPING.get(type) == value.getClass())
            return (T) value;
        if (type == String.class) {
            return (T) toString(value, format);
    	} else if (value instanceof String) {
            return fromString(type, pattern, format, (String) value);
        } else if (value instanceof Number) {
            return fromNumber(type, (Number) value);
        } else if (value instanceof Calendar) {
        	if (type.isAssignableFrom(java.util.Date.class)) {
				return (T) ((Calendar) value).getTime();
        	}
            return fromNumber(type, ((Calendar) value).getTimeInMillis());
        } else if (value instanceof java.util.Date) {
        	if (type.isAssignableFrom(GregorianCalendar.class)) {
        		GregorianCalendar calendar = new GregorianCalendar();
        		calendar.setTime((Date) value);
				return (T) calendar;
        	}
            return fromNumber(type, ((Date) value).getTime());
        }

        Constructor<?> ctor = null;
        try {
            ctor = type.getConstructor(type);
        } catch (NoSuchMethodException e) {
            // ignore: this is a problem not related to type casting
        }
        if (ctor != null)
            try {
                return (T) ctor.newInstance(value);
            } catch (Exception e) {
                throw new TypeCastException(value, value.getClass(), type, e);
            }
        throw new TypeCastException(value, value.getClass(), type);
    }

    /**
     *
     * @param object
     * @param formatter
     * @return
     */
    private static String toString(Object object, Format formatter) {
        if (object == null)
            return null;
        Class<? extends Object> clazz = object.getClass();
        if (clazz.isArray()) {
            StringBuilder builder = new StringBuilder();
            int length = Array.getLength(object);
            for (int i = 0; i < length; i++) {
                Object value = Array.get(object, i);
                builder.append(toString(value, formatter));
            }
            return builder.toString();
        }
        if (object instanceof Calendar) {
            object = ((Calendar) object).getTime();
        }
        return formatter != null ? formatter.format(object) : String.valueOf(object);
    }

    /**
     *
     * @param type
     * @param format
     * @param value
     * @return
     * @throws TypeCastException
     */
    @SuppressWarnings("unchecked")
    private static <T> T fromString(Class<T> type, String pattern, Format format, String value)
            throws TypeCastException {
        if (type == String.class)
            return (T) value;
        if ((value == null) || (value.length() == 0))
            return null;
        if (type == Character.TYPE)
            return (T) Character.valueOf(value.charAt(0));
        value = value.trim();
        if (value.length() == 0)
            return null;
        if (type.isArray()) {
            Class<?> elementType = type.getComponentType();
            String[] items = value.length() > 0 ? value.split(",") : EMPTY_STRING_ARRAY;
            Object array = Array.newInstance(elementType, items.length);
            for (int i = 0; i < items.length; i++) {
                Array.set(array, i, fromString(elementType, pattern, format, items[i]));
            }
            return (T) array;
        } else if (type.isPrimitive()) {
            if (type == Long.TYPE) {
                return (T) Long.valueOf(value);
            } else if (type == Integer.TYPE) {
                return (T) Integer.valueOf(value);
            } else if (type == Short.TYPE) {
                return (T) Short.valueOf(value);
            } else if (type == Byte.TYPE) {
                return (T) Byte.valueOf(value);
            } else if (type == Double.TYPE) {
                return (T) Double.valueOf(value);
            } else if (type == Float.TYPE) {
                return (T) Float.valueOf(value);
            } else if (type == Boolean.TYPE) {
                return (T) Boolean.valueOf(TRUE_VALUES.contains(value.toLowerCase()));
            }
        }
        if (format == null) {
            format = getFormatter(type, pattern, value);
        }
        if (format != null) {
            try {
                return convert(format.parseObject(value), type);
            } catch (ParseException e) {
                throw new TypeCastException(value, value.getClass(), type, e);
            }
        }
        try {
            Constructor<?> ctor = type.getConstructor(String.class);
            return (T) ctor.newInstance(value);
        } catch (Exception e) {
            // ignore all failures
        }
        try {
            Method method = type.getMethod("fromString", String.class);
            if (Modifier.isStatic(method.getModifiers()) && type.isAssignableFrom(method.getReturnType())) {
                return (T) method.invoke(null, value);
            }
        } catch (Exception e) {
        }
        try {
            Method method = type.getMethod("valueOf", String.class);
            if (Modifier.isStatic(method.getModifiers()) && type.isAssignableFrom(method.getReturnType())) {
                return (T) method.invoke(null, value);
            }
        } catch (Exception e) {
        }
        throw new TypeCastException(value, value.getClass(), type);
    }

    /**
     *
     * @param clazz
     * @param format
     * @param value
     * @return
     */
    private static <T> Format getFormatter(Class<T> clazz, String format, String value) {
        Format formatter = null;
        if (Number.class.isAssignableFrom(clazz)) {
            formatter = new DecimalFormat(format != null ? format : DEFAULT_NUMBER_FORMAT);
        } else if (java.sql.Date.class.isAssignableFrom(clazz)) {
            formatter = new SimpleDateFormat(format != null ? format : DEFAULT_DATE_FORMAT);
        } else if (java.sql.Time.class.isAssignableFrom(clazz)) {
            formatter = new SimpleDateFormat(format != null ? format : DEFAULT_TIME_FORMAT);
        } else if (java.util.Date.class.isAssignableFrom(clazz)) {
            String defaultFormat = DEFAULT_DATE_FORMAT;
            if ((value != null) && (value.indexOf('T') > 0)) {
                defaultFormat = DEFAULT_DATETIME_FORMAT;
            }
            formatter = new SimpleDateFormat(format != null ? format : defaultFormat);
        } else if (java.util.Calendar.class.isAssignableFrom(clazz)) {
            String defaultFormat = DEFAULT_DATE_FORMAT;
            if ((value != null) && (value.indexOf('T') > 0)) {
                defaultFormat = DEFAULT_DATETIME_FORMAT;
            }
            formatter = new SimpleDateFormat(format != null ? format : defaultFormat);
        }
        return formatter;
    }

    /**
     *
     * @param type
     * @param value
     * @return
     * @throws TypeCastException
     */
    @SuppressWarnings("unchecked")
    private static <T> T toDate(Class<T> type, Number value) throws TypeCastException {
        long millis = value.longValue();
        if (java.util.Calendar.class.isAssignableFrom(type)) {
            try {
                Calendar calendar = type == Calendar.class ? new GregorianCalendar() : (Calendar) type.newInstance();
                calendar.setTimeInMillis(millis);
                return (T) calendar;
            } catch (Exception e) {
                throw new TypeCastException(value, value.getClass(), type, e);
            }
        } else if (java.util.Date.class.isAssignableFrom(type)) {
            try {
                Constructor<T> ctor = type.getConstructor(Long.TYPE);
                return ctor.newInstance(millis);
            } catch (Exception e) {
                // ignore
            }
            try {
                java.util.Date date = (java.util.Date) type.newInstance();
                date.setTime(millis);
                return (T) date;
            } catch (Exception e) {
                throw new TypeCastException(value, value.getClass(), type, e);
            }
        }
        throw new TypeCastException(value, value.getClass(), type);
    }

    /**
     *
     * @param type
     * @param value
     * @return
     * @throws TypeCastException
     */
    @SuppressWarnings("unchecked")
    private static <T> T fromNumber(Class<T> type, Number value) throws TypeCastException {
        if (Calendar.class.isAssignableFrom(type) || java.util.Date.class.isAssignableFrom(type)) {
            return toDate(type, value);
        } else if ((type == Long.class) || (type == Long.TYPE)) {
            return (T) Long.valueOf(((Number) value).longValue());
        } else if ((type == Integer.class) || (type == Integer.TYPE)) {
            return (T) Integer.valueOf(((Number) value).intValue());
        } else if ((type == Short.class) || (type == Short.TYPE)) {
            return (T) Short.valueOf(((Number) value).shortValue());
        } else if ((type == Byte.class) || (type == Byte.TYPE)) {
            return (T) Byte.valueOf(((Number) value).byteValue());
        } else if ((type == Double.class) || (type == Double.TYPE)) {
            return (T) Double.valueOf(((Number) value).doubleValue());
        } else if ((type == Float.class) || (type == Float.TYPE)) {
            return (T) Float.valueOf(((Number) value).floatValue());
        } else if ((type == Boolean.class) || (type == Boolean.TYPE)) {
            return (T) Boolean.valueOf(value.longValue() != 0);
        } else if (type == BigInteger.class) {
            return (T) BigInteger.valueOf(value.longValue());
        } else if (type == BigDecimal.class) {
            return (T) BigDecimal.valueOf(value.longValue());
        }
        throw new TypeCastException(value, value.getClass(), type);
    }

}
