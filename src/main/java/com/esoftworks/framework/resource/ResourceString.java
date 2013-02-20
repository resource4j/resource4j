package com.esoftworks.framework.resource;

import java.text.Format;

public interface ResourceString extends ResourceValue<String> {
	
	<T> ResourceValue<T> ofType(Class<T> type);
	
	<T> T as(Class<T> type);
	
	<T> T as(Class<T> type, String format);
	
	<T> T as(Class<T> type, Format format);

}
