package com.github.resource4j.resources.context;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class ResourceResolutionContext implements Serializable {

	private static final long serialVersionUID = 13975482752L;

	public static final String DEFAULT_COMPONENT_SEPARATOR = "-";

    /**
     * Helper method to wrap varags into array with {@link #context(ResourceResolutionComponent[],Map)}
     * @param resolutionParams
     * @since 3.1
     * @return
     */
	public static ResourceResolutionComponent[] resolve(Object... resolutionParams) {
        ResourceResolutionComponent[] components = new ResourceResolutionComponent[resolutionParams.length];
        for (int i = 0; i < resolutionParams.length; i++) {
            if (resolutionParams[i] == null) {
                components[i] = new StringResolutionComponent("");
            } else if (resolutionParams[i] instanceof ResourceResolutionComponent) {
                components[i] = (ResourceResolutionComponent) resolutionParams[i];
            } else if (resolutionParams[i] instanceof Locale) {
                components[i] = new LocaleResolutionComponent((Locale) resolutionParams[i]);
            } else if (resolutionParams[i] instanceof String) {
                components[i] = new StringResolutionComponent((String) resolutionParams[i]);
            } else if (resolutionParams[i] instanceof String[]) {
                components[i] = new StringResolutionComponent((String[]) resolutionParams[i]);
            } else {
                components[i] = new StringResolutionComponent(String.valueOf(resolutionParams[i]));
            }
        }
	    return components;
    }

    /**
     * Convenience wrapper for message parameters
     * @param params
     * @return
     */
    public static Map<String, Object> with(Object... params) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < params.length; i++) {
            map.put(String.valueOf(i), params[i]);
        }
        return map;
    }

    /**
     * Build resolution context in which message will be discovered and built
     * @param components resolution components, used to identify message bundle
     * @param messageParams message parameters will be substituted in message and used in pattern matching
     * @since 3.1
     * @return immutable resolution context instance for given parameters
     */
	public static ResourceResolutionContext context(ResourceResolutionComponent[] components,
                                                    Map<String, Object> messageParams) {
        return new ResourceResolutionContext(components, messageParams);
    }

    public static ResourceResolutionContext withoutContext() {
		return new ResourceResolutionContext(new ResourceResolutionComponent[0], null);
	}

    public static ResourceResolutionContext in(Object... objects) {
        return context(resolve(objects), null);
    }

	public static ResourceResolutionContext withParams(Object... params) {
        return parameterized(with(params));
    }

    public static ResourceResolutionContext with(String name, Object value) {
	    Map<String, Object> params = new HashMap<>();
	    params.put(name, value);
        return parameterized(params);
    }

    public static ResourceResolutionContext parameterized(Map<String, Object> params) {
        return new ResourceResolutionContext(new ResourceResolutionComponent[0], params);
    }

	private final ResourceResolutionComponent[] components;

    private final Map<String,Object> parameters;

    public ResourceResolutionContext(ResourceResolutionComponent[] components) {
        this.components = components;
        this.parameters = Collections.emptyMap();
    }

	public ResourceResolutionContext(ResourceResolutionComponent[] components, Map<String, Object> parameters) {
		this.components = components;
		this.parameters = parameters == null ? null : Collections.unmodifiableMap(parameters);
	}
	
	public ResourceResolutionComponent[] components() {
		return this.components;
	}

	public Map<String,Object> parameters() {
	    return this.parameters != null ? this.parameters : Collections.emptyMap();
    }

	public boolean isEmpty() {
		return this.components.length == 0;
	}
	
	public ResourceResolutionContext parent() {
        if (isEmpty()) {
            throw new IllegalStateException("Context is already empty: parent does not exist");
        }

        int length = this.components.length;
        int lastIndex = length - 1;
        ResourceResolutionComponent last = this.components[lastIndex];
        if (!last.isReducible()) {
            length = length - 1;
        }

        ResourceResolutionComponent[] components = new ResourceResolutionComponent[length];
        System.arraycopy(this.components, 0, components, 0, lastIndex);

        if (last.isReducible()) {
            components[lastIndex] = last.reduce();
        }
		return new ResourceResolutionContext(components, parameters);
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
        return Arrays.equals(components, other.components);
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (ResourceResolutionComponent component : components) {
			if (builder.length() > 0) {
				builder.append(DEFAULT_COMPONENT_SEPARATOR);
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

	public static Stream<ResourceResolutionContext> parentsOf(ResourceResolutionContext context) {
		return StreamSupport.stream(((Iterable<ResourceResolutionContext>)
				() -> new Iterator<ResourceResolutionContext>() {
            private ResourceResolutionContext current = context;
            @Override
            public boolean hasNext() {
                return !current.isEmpty();
            }
            @Override
            public ResourceResolutionContext next() {
				current = current.parent();
                return current;
            }
        }).spliterator(), false);
	}

    public ResourceResolutionContext with(Map<String, Object> params) {
        return this.merge(params);
    }

    public ResourceResolutionContext merge(Map<String, Object> params) {
        Map<String, Object> map;
        if (params != null && params.size() > 0) {
            if (this.parameters != null && this.parameters.size() > 0) {
                map = new HashMap<>(this.parameters);
                map.putAll(params);
            } else {
                map = params;
            }
        } else {
            map = this.parameters;
        }
        return new ResourceResolutionContext(this.components, map);
    }
}
