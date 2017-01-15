package com.github.resource4j.converters;

import com.github.resource4j.converters.impl.*;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.Format;
import java.util.*;
import java.util.stream.Collectors;

public class TypeConverter {

    @SuppressWarnings("WeakerAccess")
    public static TypeConverter converter() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    private static final Class<? extends Conversion>[] CONVERSIONS = new Class[] {
            AutoBoxingConversion.class,
            StringToBooleanConversion.class,
            StringToNumberConversion.class,
            StringToTemporalConversion.class,
            StringToDateConversion.class,
            DateToStringConversion.class,
            NumberToStringConversion.class,
            NumberToInstantConversion.class,
            DateToCalendarConversion.class,
            CalendarToDateConversion.class,
            CharToNumberConversion.class,
            UtilDateToSQLDateConversion.class,
            StringToCharConversion.class,
            DateToLocalDateConversion.class,
            LocalDateToDateConversion.class,
            SQLTimeToLocalTimeConversion.class,
            LocalTimeToSQLTimeConversion.class,
            IntegerNumberConversion.class
    };

    private static final Class<?>[][] PATHS = new Class[][] {
            new Class[] { String.class, Date.class, Calendar.class },
            new Class[] { String.class, Date.class, GregorianCalendar.class },
            new Class[] { Calendar.class, Date.class, String.class },
            new Class[] { GregorianCalendar.class, Date.class, String.class },
            new Class[] { Calendar.class, Date.class, java.sql.Date.class },
            new Class[] { GregorianCalendar.class, Date.class, java.sql.Date.class },
            new Class[] { java.sql.Date.class, Date.class, Calendar.class },
            new Class[] { java.sql.Date.class, Date.class, GregorianCalendar.class },
    };

    private static final TypeConverter INSTANCE = new TypeConverter(
            CONVERSIONS,
            PATHS
    );

    private Map<ConversionPair,Conversion> conversions = new LinkedHashMap<>();

    @SuppressWarnings("WeakerAccess")
    public TypeConverter(Class<? extends Conversion>[] conversions, Class<?>[][] paths) {
        this(Arrays.stream(conversions)
                .map(c -> {
                    try {
                        return (Conversion) c.newInstance();
                    } catch (Exception e){
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList()).toArray(new Conversion[0]));

        CastConversion cast = new CastConversion();

        for (Class<?>[] path : paths) {
            Class<?> from = path[0];
            Class<?> through = path[1];
            Class<?> to = path[2];
            ConversionPair firstKey = ConversionPair.of(from, through);
            Conversion<?,?> first = this.conversions.get(firstKey);
            ConversionPair secondKey = ConversionPair.of(through, to);
            Conversion<?,?> second = this.conversions.get(secondKey);
            ConversionPair route = ConversionPair.of(from, to);
            CompositeConversion composite = new CompositeConversion(firstKey, secondKey, first != null ? first : cast, second != null ? second : cast);
            this.conversions.put(route, composite);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public TypeConverter(Conversion... conversions) {
        for (Conversion<?,?> conversion : conversions) {
            for (ConversionPair key : conversion.acceptedTypes()) {
                this.conversions.put(key, conversion);
            }
        }
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
     * Convert given value to given target
     *
     * @param fromValue
     *            the value to convert
     * @param toType
     *            target target
     * @param <T>
     *            target of the result
     * @return the value converted to given target
     * @throws TypeCastException
     *             if conversion was not possible
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object fromValue, Class<T> toType) throws TypeCastException {
        return convert(fromValue, toType, (String) null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T convert(Object fromValue, Class<T> toType, String format) throws TypeCastException {
        return converter().doConvert(fromValue, toType, format);
    }

    @SuppressWarnings("unchecked")
    public static <T> T convert(Object fromValue, Class<T> toType, Format format) throws TypeCastException {
        return converter().doConvert(fromValue, toType, format);
    }

    @SuppressWarnings("unchecked")
    private <T> T doConvert(Object fromValue, Class<T> toType, Object format) {
        if (fromValue == null) {
            return null;
        }
        if (toType == null) {
            throw new NullPointerException("target type for conversion cannot be null");
        }
        if (toType.isInstance(fromValue)) {
            return (T) fromValue;
        }
        T result = null;

        Class<?> fromType = fromValue.getClass();
        if (toType.isArray()) {
            Class<?> componentType = toType.getComponentType();
            Object fromArray = null;
            if (fromType.isArray()) {
                fromArray = fromValue;
            } else if (String.class.equals(fromType)) {
                String delimeter = format instanceof String ? (String) format : ",";
                fromArray = ((String) fromValue).split(delimeter);
            }
            if (fromArray != null) {
                int size = Array.getLength(fromArray);
                result = (T) Array.newInstance(componentType, size);
                for (int i = 0; i < size; i++) {
                    Object fromItem = Array.get(fromArray, i);
                    Object toItem = doConvert(fromItem, componentType, format);
                    Array.set(result, i, toItem);
                }
            }
        } else if (toType.isEnum()) {
            if (Integer.class.isAssignableFrom(fromType)) {
                int index = (Integer) fromValue;
                result = toType.getEnumConstants()[index];
            } else if (String.class.isInstance(fromValue)) {
                for (T val : toType.getEnumConstants()) {
                    if (val.toString().equals(fromValue)) {
                        result = val;
                        break;
                    }
                }
            }
        }
        if (fromType.isEnum()) {
            if (Integer.class.isAssignableFrom(toType)) {
                result = (T) (Integer) Enum.class.cast(fromValue).ordinal();
            } else if (toType == String.class) {
                result = (T) String.valueOf(fromValue);
            }
        }
        if (result == null) {
            ConversionPair key = new ConversionPair(fromType, toType);
            Conversion<Object, T> conversion = (Conversion<Object, T>) conversions.get(key);
            if (conversion != null) {
                result = conversion.convert(fromValue, toType, format);
            }
        }
        if (result == null && toType == String.class) {
            result = (T) String.valueOf(fromValue);
        }
        if (result == null) {
            result = fromFactoryMethod(toType, fromValue);
        }
        if (result != null) {
            return result;
        }
        throw new TypeCastException(fromValue, fromType, toType);
    }

    @SuppressWarnings("unchecked")
    private static <F,T> T fromFactoryMethod(Class<T> target, F value) {
        try {
            Constructor<?> ctor = target.getConstructor(value.getClass());
            return (T) ctor.newInstance(value);
        } catch (Exception e) {
            // ignore all failures
        }
        for (Method method : target.getMethods()) {
            try {
                if (Modifier.isStatic(method.getModifiers())
                        && target.isAssignableFrom(method.getReturnType())
                        && method.getParameterCount() == 1
                        && method.getParameterTypes()[0].isAssignableFrom(value.getClass())) {
                    return (T) method.invoke(null, value);
                }
            } catch (Exception e) {
                // ignore all failures
            }
        }
        return null;
    }


}

