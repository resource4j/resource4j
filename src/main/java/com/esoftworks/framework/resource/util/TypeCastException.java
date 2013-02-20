package com.esoftworks.framework.resource.util;

/**
 * @author Ivan Gammel
 */
public class TypeCastException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private Class<?> valueType;
    private Class<?> expectedType;
    private Object value;

    public TypeCastException(Object value, Class<?> from, Class<?> to) {
        this(value, from, to, null);
    }

    public TypeCastException(Object value, Class<?> from, Class<?> to, Throwable cause) {
        super("Cannot convert value "+value+" from "+from.getName()+" to "+to.getName(), cause);
        this.value = value;
        this.valueType = from;
        this.expectedType = to;
    }

    public Class<?> getValueType() {
        return valueType;
    }

    public Class<?> getExpectedType() {
        return expectedType;
    }

    public Object getValue() {
        return value;
    }
}