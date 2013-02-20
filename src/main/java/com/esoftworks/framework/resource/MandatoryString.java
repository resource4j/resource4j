package com.esoftworks.framework.resource;

public interface MandatoryString extends MandatoryValue<String>, ResourceString {
	
	@Override
	<T> MandatoryValue<T> ofType(Class<T> type);
	
}
