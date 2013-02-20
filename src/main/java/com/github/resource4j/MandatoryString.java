package com.github.resource4j;

public interface MandatoryString extends MandatoryValue<String>, ResourceString {
	
	@Override
	<T> MandatoryValue<T> ofType(Class<T> type);
	
}
