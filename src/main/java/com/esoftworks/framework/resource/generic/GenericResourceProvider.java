package com.esoftworks.framework.resource.generic;

import javax.swing.Icon;

import com.esoftworks.framework.resource.OptionalString;
import com.esoftworks.framework.resource.OptionalValue;
import com.esoftworks.framework.resource.ResourceKey;
import com.esoftworks.framework.resource.ResourceProvider;
import com.esoftworks.framework.resource.Resources;


public class GenericResourceProvider implements ResourceProvider {

	private Resources resources;
	
	private ResourceKey resourceKey;

	private ResourceKey[] defaultKeys;
	
	public GenericResourceProvider(Resources resources, ResourceKey resourceKey, ResourceKey... defaultKeys) {
		super();
		this.resources = resources;
		this.resourceKey = resourceKey;
		this.defaultKeys = defaultKeys;
	}

	@Override
	public OptionalString get(String name) {
		OptionalString string = resources.get(resourceKey.child(name));
		if (string.asIs() == null) {
			for (ResourceKey defaultKey : defaultKeys) {
				string = resources.get(defaultKey.child(name));
				if (string.asIs() != null) break;
			}
		}
		return string;
	}

	@Override
	public OptionalValue<Icon> icon(String name) {
		OptionalValue<Icon> icon = resources.icon(resourceKey.child(name));
		if (icon.asIs() == null) {
			for (ResourceKey defaultKey : defaultKeys) {
				icon = resources.icon(defaultKey.child(name));
				if (icon.asIs() != null) break;
			}
		}
		return icon;
	}

}
