package com.github.resource4j;

import java.util.Locale;

import javax.swing.Icon;


public interface ResourceProvider {
	
	OptionalString get(String name, Locale locale);

	OptionalValue<Icon> icon(String name, Locale locale);

}
