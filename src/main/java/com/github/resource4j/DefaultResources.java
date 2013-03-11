package com.github.resource4j;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.ResourceKey.key;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
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

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public class DefaultResources implements Resources {

    private final static Logger LOG = LoggerFactory.getLogger(Resources.class);

    private final static ResourceKey DEFAULT_FRAMEWORK_RESOURCES = bundle("com.github.resource4j.default");
    private final static ResourceKey DEFAULT_APPLICATION_RESOURCES = bundle("i18n.resources");

    private Map<Locale, Map<ResourceKey,String>> cachedResources = new HashMap<Locale, Map<ResourceKey, String>>();

    private ResourceKey defaultResourceBundle = DEFAULT_APPLICATION_RESOURCES;

    public DefaultResources() {
    }

    public DefaultResources(String defaultBundle) {
        this.defaultResourceBundle = bundle(defaultBundle);
    }

    @Override
    public OptionalString get(String key, Locale locale) {
        return get(defaultResourceBundle.child(key), locale);
    }

    @Override
    public OptionalString get(ResourceKey key, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        String result = lookup(key, locale);
        return new GenericOptionalString(key, result);
    }

    protected Map<ResourceKey,String> getCache(Locale locale) {
        Map<ResourceKey, String> cache = cachedResources.get(locale);
        if (cache == null) {
            cache = new WeakHashMap<ResourceKey, String>();
            cachedResources.put(locale, cache);
        }
        return cache;
    }

    protected String lookup(ResourceKey key, Locale locale) {
        Map<ResourceKey, String> cache = getCache(locale);
        String result = cache.get(key);
        if (result != null) return result;
        result = doLookup(key, locale);
        if (result == null) {
            result = doLookup(defaultResourceBundle.child(key.getBundle()).child(key.getId()), locale);
        }
        if (result == null) {
            result = doLookup(defaultResourceBundle.child(key.getId()), locale);
        }
        if (result == null) {
            result = doLookup(DEFAULT_FRAMEWORK_RESOURCES.child(key.getId()), locale);
        }
        if (result != null) {
            cache.put(key, result);
        }
        return result;
    }

    protected String doLookup(ResourceKey key, Locale locale) {
        String baseName = key.getBundle();
        ResourceBundle bundle = null;
        try {
            bundle = ResourceBundle.getBundle(baseName, locale);
        } catch (MissingResourceException e) {
            LOG.trace(e.getMessage());
        }
        if (bundle != null) {
            try {
                return bundle.getString(key.getId());
            } catch (MissingResourceException e) {
                LOG.trace("Value not found in bundle: {} - {}", key, e.getMessage());
            }
        }
        return null;
    }

    @Override
    public <T> OptionalString get(Class<T> clazz, String key, Locale locale) {
        return get(key(clazz, key), locale);
    }

    @Override
    public <E extends Enum<E>> OptionalString get(E value, String key, Locale locale) {
        return get(key(value.getClass(), value.name()).child(key), locale);
    }

    @Override
    public OptionalValue<Icon> icon(ResourceKey key, Locale locale) {
        String iconName = lookup(key, locale);
        if (iconName != null)  {
            URL url = getClass().getResource(iconName);
            if (url == null) {
                // try to find image in default location
                String prefix = get(defaultResourceBundle.child("location.images"), locale).asIs();
                if (prefix == null) prefix = "i18n/images";
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
