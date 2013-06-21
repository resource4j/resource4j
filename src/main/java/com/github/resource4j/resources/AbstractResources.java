package com.github.resource4j.resources;

import java.util.Locale;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.generic.GenericOptionalString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ivan Gammel
 */
public abstract class AbstractResources implements Resources {

    /**
     *
     */
    protected final static Logger LOG = LoggerFactory.getLogger(Resources.class);

    protected AbstractResources() {
    }

    /*
     * (non-Javadoc)
     * @see com.github.resource4j.Resources#get(com.github.resource4j.ResourceKey, java.util.Locale)
     */
    @Override
    public OptionalString get(ResourceKey key, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        String result = lookup(key, locale);
        return new GenericOptionalString(key, result);
    }

    /*
     * (non-Javadoc)
     * @see com.github.resource4j.Resources#forKey(com.github.resource4j.ResourceKey)
     */
    @Override
    public ResourceProvider forKey(ResourceKey key) {
        return new GenericResourceProvider(this, key);
    }

    //
    // Details of implementation to be defined in subclasses
    //
    /**
     *
     * @param key
     * @param locale
     * @return
     */
    protected abstract String lookup(ResourceKey key, Locale locale);

}
