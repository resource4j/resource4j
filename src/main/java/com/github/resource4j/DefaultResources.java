package com.github.resource4j;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.ResourceKey.key;

import java.net.URL;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.WeakHashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.resource4j.generic.GenericOptionalString;
import com.github.resource4j.generic.GenericOptionalValue;
import com.github.resource4j.generic.GenericResourceProvider;

public class DefaultResources implements Resources {

	private final static Logger LOG = LoggerFactory.getLogger(Resources.class);  
	
	private final static ResourceKey DEFAULT_FRAMEWORK_RESOURCES = bundle("com.esoftworks.framework.default");
	private final static ResourceKey DEFAULT_APPLICATION_RESOURCES = bundle("resources"); 
	
	private Map<String,ResourceBundle> bundles = new WeakHashMap<String,ResourceBundle>();
	
	private Map<ResourceKey,String> cachedResources = new WeakHashMap<ResourceKey, String>();
	
	public DefaultResources() {
	}
	
	@Override
	public OptionalString get(String key) {
		return get(DEFAULT_APPLICATION_RESOURCES.child(key));
	}

	@Override
	public OptionalString get(ResourceKey key) {
		String result = lookup(key);
		return new GenericOptionalString(key, result);
	}

	protected String lookup(ResourceKey key) {
		String result = cachedResources.get(key);
		if (result != null) return result;
		result = doLookup(key);
		if (result == null) {
			result = doLookup(DEFAULT_APPLICATION_RESOURCES.child(key.getBundle()).child(key.getId()));
		}
		if (result == null) {
			result = doLookup(DEFAULT_APPLICATION_RESOURCES.child(key.getId()));
		}
		if (result == null) {
			result = doLookup(DEFAULT_FRAMEWORK_RESOURCES.child(key.getId()));
		}
		if (result != null) {
			cachedResources.put(key, result);
		}
		return result;
	}
	
	protected String doLookup(ResourceKey key) {
		String baseName = key.getBundle();
		ResourceBundle bundle = bundles.get(baseName);
		if (bundle == null) {
			try {
				bundle = ResourceBundle.getBundle(baseName);
				bundles.put(baseName, bundle);
			} catch (MissingResourceException e) {
				LOG.trace(e.getMessage());
			}
		}
		if (bundle != null) {
			try {
				return bundle.getString(key.getId());
			} catch (Exception e) {
				LOG.trace("Value not found in bundle: {} - {}", key, e.getMessage());
			}
		}
		return null;
	}

	@Override
	public <T> OptionalString get(Class<T> clazz, String key) {
		return get(key(clazz, key));
	}

	@Override
	public <E extends Enum<E>> OptionalString get(E value, String key) {
		return get(key(value.getClass(), value.name()).child(key));
	}

	@Override
	public OptionalValue<Icon> icon(ResourceKey key) {
		String iconName = lookup(key);
		if (iconName != null)  {
			URL url = getClass().getResource(iconName);
			if (url == null) {
				// try to find image in default location
				String prefix = get(DEFAULT_FRAMEWORK_RESOURCES.child("location.images")).asIs();
				url = getClass().getResource(prefix+"/"+iconName);
			}
			if (url != null) {
				return new GenericOptionalValue<Icon>(key, new ImageIcon(url));
			}
		}
		LOG.trace("Missing icon: {}", key);
		return new GenericOptionalValue<Icon>(key, null);
	}

	@Override
	public ResourceProvider forKey(ResourceKey key) {
		return new GenericResourceProvider(this, key);
	}

}
