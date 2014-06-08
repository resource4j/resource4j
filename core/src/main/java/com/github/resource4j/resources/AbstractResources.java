package com.github.resource4j.resources;

import static com.github.resource4j.resources.resolution.ResourceResolutionContext.in;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;

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
        return get(key, in(locale));
    }

    /*
     * (non-Javadoc)
     * @see com.github.resource4j.Resources#forKey(com.github.resource4j.ResourceKey)
     */
    @Override
    public ResourceProvider forKey(ResourceKey key) {
        return new GenericResourceProvider(this, key);
    }

    /*
     * (non-Javadoc)
     * @see com.github.resource4j.resources.Resources#contentOf(java.lang.String, java.util.Locale)
     */
	@Override
	public ResourceFile contentOf(String name, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return contentOf(name, in(locale));
	}

}
