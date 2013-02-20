package com.github.resource4j;

public interface OptionalValue<V> extends ResourceValue<V> {

	V orDefault(V defaultValue);
	
	MandatoryValue<V> or(V defaultValue);
	
	MandatoryValue<V> notNull() throws MissingValueException;

}
