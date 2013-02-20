package com.github.resource4j;

import javax.swing.Icon;


public interface ResourceProvider {
	
	OptionalString get(String name);

	OptionalValue<Icon> icon(String name);

}
