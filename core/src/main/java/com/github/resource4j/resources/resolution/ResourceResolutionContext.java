package com.github.resource4j.resources.resolution;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public final class ResourceResolutionContext implements Serializable {

	private static final long serialVersionUID = "2.0".hashCode();
	
	public static final Comparator<ResourceResolutionContext> COMPARATOR = new Comparator<ResourceResolutionContext>() {
		@Override
		public int compare(ResourceResolutionContext c1,
				ResourceResolutionContext c2) {
			if (c1.contains(c2)) return 1;
			if (c2.contains(c1)) return -1;
			return 0;
		}
	};
	
	public static ResourceResolutionContext withoutContext() {
		return new ResourceResolutionContext(new ResourceResolutionComponent[0]);
	}
	
	public static ResourceResolutionContext in(Object... objects) {
		ResourceResolutionComponent[] components = new ResourceResolutionComponent[objects.length];
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] == null) {
				components[i] = new StringResolutionComponent("");
			} else if (objects[i] instanceof ResourceResolutionComponent) {
				components[i] = (ResourceResolutionComponent) objects[i];
			} else if (objects[i] instanceof Locale) {
				components[i] = new LocaleResolutionComponent((Locale) objects[i]);
			} else if (objects[i] instanceof String) {
				components[i] = new StringResolutionComponent((String) objects[i]);
			} else if (objects[i] instanceof String[]) {
				components[i] = new StringResolutionComponent((String[]) objects[i]);
			} else {
				components[i] = new StringResolutionComponent(String.valueOf(objects[i]));
			}
		}
		return new ResourceResolutionContext(components);
	}

	private ResourceResolutionComponent[] components;

	public ResourceResolutionContext(ResourceResolutionComponent[] components) {
		this.components = components;
	}
	
	public ResourceResolutionComponent[] components() {
		return this.components;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(components);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceResolutionContext other = (ResourceResolutionContext) obj;
		if (!Arrays.equals(components, other.components))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (ResourceResolutionComponent component : components) {
			if (builder.length() > 0) {
				builder.append('-');
			}
			List<String> sections = component.sections();
			boolean skipSeparator = true;
			for (String section : sections) {
				if (skipSeparator) {
					skipSeparator = false;
				} else {
					builder.append('_');
				}
				builder.append(section);
			}
		}
		return builder.toString();
	}

	public boolean contains(ResourceResolutionContext child) {
		if (child.components.length < this.components.length) {
			return false;
		}
		for (int i = 0; i < components.length; i++) {
			List<String> thisSections = this.components[i].sections();
			List<String> thatSections = child.components[i].sections();
			if (thatSections.size() < thisSections.size()) {
				return false;
			}
			for (int j = 0; j < thisSections.size(); j++) {
				if (!thisSections.get(j).equals(thatSections.get(j))) {
					return false;
				}
			}
		}
		return true;
	}
	
}
