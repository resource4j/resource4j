package com.github.resource4j.generic;

import javax.swing.Icon;

import com.github.resource4j.OptionalString;
import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceProvider;
import com.github.resource4j.Resources;


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
