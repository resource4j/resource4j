package com.github.resource4j.resources;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.resources.context.ResourceResolutionContext.in;

import java.time.Clock;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.ByteArrayResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.values.GenericOptionalString;
import com.github.resource4j.resources.context.ResourceResolutionContext;

/**
 *
 * @author Ivan Gammel
 */
public class InMemoryResources extends AbstractResources implements EditableResources {

    private final String instanceId = "memory:" + hashCode();

	private Clock clock;
	
    private Map<ResourceResolutionContext, Map<String, byte[]>> files = new ConcurrentHashMap<>();
    
    private Map<ResourceResolutionContext, Map<ResourceKey,String>> properties = new ConcurrentHashMap<>();
    
    private Map<ResourceResolutionContext, SortedSet<ResourceResolutionContext>> matchingContexts = new HashMap<>();

    public InMemoryResources() {
        this(Clock.systemUTC());
    }

    public InMemoryResources(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void put(ResourceKey key, Locale locale, String value) {
    	put(key, in(locale), value);
    }

    @Override
    public void put(ResourceKey key, ResourceResolutionContext context, String value) {
        Map<ResourceKey, String> localeResources = properties.get(context);
        if (localeResources == null) {
            localeResources = new ConcurrentHashMap<>();
            properties.put(context, localeResources);
        }
        localeResources.put(key, value);
        registerContext(context);
    }

    private void registerContext(ResourceResolutionContext context) {
		SortedSet<ResourceResolutionContext> matches = new TreeSet<>(ResourceResolutionContext.COMPARATOR);
		for (ResourceResolutionContext ctx : properties.keySet()) {
			if (ctx.contains(context)) {
				matches.add(ctx);
			}
		}
		matchingContexts.put(context, matches);
		for (ResourceResolutionContext ctx : properties.keySet()) {
			if (context.contains(ctx)) {
				SortedSet<ResourceResolutionContext> thatMatches = matchingContexts.get(ctx);
				thatMatches.add(context);
			}
		}
	}

	@Override
    public void remove(ResourceKey key, Locale locale) {
    	remove(key, in(locale));
    }
    
    @Override
    public void remove(ResourceKey key, ResourceResolutionContext context) {
        Map<ResourceKey, String> localeResources = properties.get(context);
        if (localeResources == null) {
            return;
        }
        localeResources.remove(key);
    }
    @Override
	public OptionalString get(ResourceKey key, ResourceResolutionContext context) {
    	String value = doGet(key, context);
        if (value == null) {
        	synchronized (properties) {
        		value = doGet(key, context);
	        	SortedSet<ResourceResolutionContext> matches = matchingContexts.get(context);
	        	if (matches == null) {
	        		registerContext(context);
	        		matches = matchingContexts.get(context);
	        	}
        		for (ResourceResolutionContext ctx : matches) {
        			value = doGet(key, ctx);
        			if (value != null) {
        				break;
        			}
        		}
        	}
        }
		return new GenericOptionalString(instanceId, key, value);
    }

	private String doGet(ResourceKey key, ResourceResolutionContext context) {
		String value = null;
        Map<ResourceKey, String> contextResources = properties.get(context);
        if (contextResources != null) {
        	value = contextResources.get(key);
        }
		return value;
	}

    @Override
    public ResourceObject contentOf(String name, ResourceResolutionContext context) {
    	byte[] value = doGet(name, context);
        if (value == null) {
        	synchronized (properties) {
        		value = doGet(name, context);
	        	SortedSet<ResourceResolutionContext> matches = matchingContexts.get(context);
	        	if (matches == null) {
	        		registerContext(context);
	        		matches = matchingContexts.get(context);
	        	}
        		for (ResourceResolutionContext ctx : matches) {
        			value = doGet(name, ctx);
        			if (value != null) {
        				break;
        			}
        		}
        	}
        }
        if (value == null) {
        	throw new MissingResourceObjectException(name);
        }
		return new ByteArrayResourceObject(name, name, value, clock.millis());
    }

	private byte[] doGet(String name, ResourceResolutionContext context) {
		byte[] value = null;
        Map<String, byte[]> contextResources = files.get(context);
        if (contextResources != null) {
        	value = contextResources.get(name);
        }
		return value;
	}

	@Override
	public void put(String name, Locale locale, byte[] content) {
		put(name, in(locale), content);
	}

	@Override
	public void put(String name, ResourceResolutionContext context, byte[] content) {
		Map<String, byte[]> localeResources = files.get(context);
        if (localeResources == null) {
            localeResources = new ConcurrentHashMap<>();
            files.put(context, localeResources);
        }
        localeResources.put(name, content);
        registerContext(context);	
    }

}
