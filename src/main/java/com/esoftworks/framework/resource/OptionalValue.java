package com.esoftworks.framework.resource;

public interface OptionalValue<V> extends ResourceValue<V> {

	V orDefault(V defaultValue);
	
	MandatoryValue<V> or(V defaultValue);
	
	MandatoryValue<V> notNull() throws MissingValueException;

}
