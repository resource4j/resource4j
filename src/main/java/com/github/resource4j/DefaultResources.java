package com.github.resource4j;

import static com.github.resource4j.ResourceKey.bundle;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.WeakHashMap;

import javax.swing.ImageIcon;

import com.github.resource4j.files.ResourceFile;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public class DefaultResources extends AbstractResources {

    private final static ResourceKey DEFAULT_FRAMEWORK_RESOURCES = bundle("com.github.resource4j.default");

    private Map<Locale, Map<ResourceKey,String>> cachedResources = new HashMap<Locale, Map<ResourceKey, String>>();

    public DefaultResources() {
    }

    public DefaultResources(String defaultBundle) {
        super(defaultBundle);
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

    /**
     * @param locale
     * @param iconName
     * @return
     */
    protected ImageIcon lookupIcon(Locale locale, String iconName) {
        URL url = getClass().getResource(iconName);
        ImageIcon icon = null;
        if (url != null) {
            icon = new ImageIcon(url);
        }
        return icon;
    }

    @Override
    public ResourceFile contentOf(ResourceKey key, Locale locale) {
        return null;
    }
}
