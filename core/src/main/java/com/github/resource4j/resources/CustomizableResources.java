package com.github.resource4j.resources;

import static com.github.resource4j.ResourceKey.bundle;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.github.resource4j.*;
import com.github.resource4j.objects.providers.ClasspathResourceObjectProvider;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.resources.discovery.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.resource4j.values.GenericOptionalString;
import com.github.resource4j.resources.context.ResourceResolutionContext;

/**
 * @author Ivan Gammel
 */
public class CustomizableResources extends AbstractResources {

    public static final ResourceKey DEFAULT_APPLICATION_RESOURCES = bundle("i18n.resources");

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    protected ResourceKey defaultResourceBundle;

    private ResourceFileEnumerationStrategy fileEnumerationStrategy;

    private ResourceObjectProvider fileFactory;

    private ResourceBundleParser bundleParser;

    public CustomizableResources() {
        this.defaultResourceBundle = DEFAULT_APPLICATION_RESOURCES;
        LOG.debug("Configured default resource bundle: {}", defaultResourceBundle);
    }

    public CustomizableResources(String defaultBundle) {
        this.defaultResourceBundle = ResourceKey.bundle(defaultBundle);
        LOG.debug("Configured default resource bundle: {}", defaultBundle);
    }
    
    @PostConstruct
    public void verifyConfiguration() {
    	getFileEnumerationStrategy();
    	getFileFactory();
    	getBundleParser();
    }

    public void setDefaultResourceBundle(String defaultBundle) {
        this.defaultResourceBundle = ResourceKey.bundle(defaultBundle);
        LOG.debug("Configured default resource bundle: {}", defaultBundle);
    }

    public void setFileEnumerationStrategy(ResourceFileEnumerationStrategy fileEnumerationStrategy) {
        this.fileEnumerationStrategy = fileEnumerationStrategy;
        LOG.debug("Configured file enumeration strategy: {}", fileEnumerationStrategy.getClass().getSimpleName());
    }
    
    public ResourceFileEnumerationStrategy getFileEnumerationStrategy() {
    	if (fileEnumerationStrategy == null) {
    		setFileEnumerationStrategy(new BasicResourceFileEnumerationStrategy());
    	}
    	return fileEnumerationStrategy;
    }

    public void setFileFactory(ResourceObjectProvider fileFactory) {
        this.fileFactory = fileFactory;
        LOG.debug("Configured file factory: {}", fileFactory.getClass().getSimpleName());
    }
    
    public ResourceObjectProvider getFileFactory() {
    	if (fileFactory == null) {
    		setFileFactory(new ClasspathResourceObjectProvider());
    	}
    	return fileFactory;
    }

    public void setBundleParser(ResourceBundleParser bundleParser) {
        this.bundleParser = bundleParser;
        LOG.debug("Configured resource bundle parser: {}", bundleParser.getClass().getSimpleName());
    }

    public ResourceBundleParser getBundleParser() {
    	if (bundleParser == null) {
    		setBundleParser(new PropertyResourceBundleParser());
    	}
    	return bundleParser;
    }

    protected String getFileName(String bundle) {
        if (bundle == null) {
            return null;
        }
        if (bundle.indexOf('/') > 0) {
            return bundle;
        }
        String extension = "";
        Class<? extends ResourceBundleParser> parser = getBundleParser().getClass();
        ContentType type = parser.getAnnotation(ContentType.class);
        if (type != null) {
            extension = type.extension();
        }
        return bundle.replace('.','/') + '.' + extension;
    }

	@Override
	public OptionalString get(ResourceKey key, ResourceResolutionContext context) {
        String bundleName = getFileName(key.getBundle());
        String defaultBundleName = getFileName(defaultResourceBundle.getBundle());
        String[] bundleOptions = bundleName != null
                ? new String[] { bundleName, defaultBundleName }
                : new String[] { defaultBundleName };
        String fullKey = key.getBundle() + '#' + key.getId();
        String shortKey = key.getId();
        List<String> options = getFileEnumerationStrategy().enumerateFileNameOptions(bundleOptions, context);
        
        Throwable suppressedException = null;
        String value = null;
        String resolvedSource = null;
        
        for (String option : options) {
            try {
                ResourceObject object = getFileFactory().get(key.getBundle(), option);
                Map<String, String> properties = getBundleParser().parse(object);
                if (properties.containsKey(shortKey)) {
                    value = properties.get(shortKey);
                    resolvedSource = object.actualName();
					break;
                } else if (properties.containsKey(fullKey)) {
                	value = properties.get(fullKey);
                    resolvedSource = object.actualName();
                	break;
                }
            } catch (MissingResourceObjectException e) {
                suppressedException = e;
            }
        }
        return new GenericOptionalString(resolvedSource, key, value, suppressedException);
    }

    @Override
    public ResourceObject contentOf(String name, ResourceResolutionContext context) {
        List<String> options = getFileEnumerationStrategy().enumerateFileNameOptions(new String[] { name }, context);
        ResourceObject object = null;
        for (String option : options) {
            try {
                object = getFileFactory().get(name, option);
                break;
            } catch (MissingResourceObjectException e) {
                LOG.trace("Object not found: {}", option);
            }
        }
        if (object != null) {
            return object;
        }
        throw new MissingResourceObjectException(name);
    }

}
