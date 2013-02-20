package com.esoftworks.framework.resource;

import javax.swing.Icon;


public interface ResourceProvider {
	
	OptionalString get(String name);

	OptionalValue<Icon> icon(String name);

}
