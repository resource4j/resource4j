package com.github.resource4j;

import javax.swing.Icon;


public interface Resources {

	String ICON = "icon";
	
	String LABEL = "label";
	
	OptionalString get(ResourceKey key);

	OptionalString get(String key);
	
	<T> OptionalString get(Class<T> clazz, String key);
	
	<E extends Enum<E>> OptionalString get(E value, String key);

	OptionalValue<Icon> icon(ResourceKey key);
	
	ResourceProvider forKey(ResourceKey key);
	
}
