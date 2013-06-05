package com.github.resource4j;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.ResourceKey.key;

import java.util.Locale;

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
 */
public abstract class AbstractResources implements Resources {

    /**
     *
     */
    protected final static Logger LOG = LoggerFactory.getLogger(Resources.class);

    /**
     *
     */
    public final static ResourceKey DEFAULT_APPLICATION_RESOURCES = bundle("i18n.resources");

    /**
     *
     */
    protected final ResourceKey defaultResourceBundle;

    protected AbstractResources() {
        this.defaultResourceBundle = DEFAULT_APPLICATION_RESOURCES;
    }

    protected AbstractResources(String defaultBundle) {
        this.defaultResourceBundle = bundle(defaultBundle);
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

//
//    /*
//     * (non-Javadoc)
//     * @see com.github.resource4j.Resources#icon(com.github.resource4j.ResourceKey, java.util.Locale)
//     */
//    @Override
//    public OptionalValue<Icon> icon(ResourceKey key, Locale locale) {
//        return contentOf(key, locale).parsedTo(icon());
//        String iconName = lookup(key, locale);
//        if (iconName != null)  {
//            ImageIcon icon = lookupIcon(locale, iconName);
//            if (icon == null) {
//                String prefix = get(defaultResourceBundle.child("location.images"), locale).asIs();
//                if (prefix == null) prefix = "i18n/images";
//                icon = lookupIcon(locale, prefix + "/" + iconName);
//            }
//            if (icon != null) {
//                return new GenericOptionalValue<Icon>(key, icon);
//            }
//        }
//        LOG.trace("Missing icon: {}", key);
//        return new GenericOptionalValue<Icon>(key, null);
//    }

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


    /**
     * @param locale
     * @param iconName
     * @return
     */
    protected abstract ImageIcon lookupIcon(Locale locale, String iconName);

}
