package com.github.resource4j;

public interface OptionalString extends OptionalValue<String>, ResourceString {

	@Override
	<T> OptionalValue<T> ofType(Class<T> type);
	
	MandatoryString or(String defaultValue);
	
	String orDefault(String defaultValue);
	
	MandatoryString notNull() throws MissingValueException;
	
}
