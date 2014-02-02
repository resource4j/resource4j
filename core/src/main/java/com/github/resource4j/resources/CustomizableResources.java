package com.github.resource4j.resources;

import static com.github.resource4j.ResourceKey.bundle;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.MissingResourceFileException;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.lookup.*;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

/**
 *
 * @author Ivan Gammel
 *
 */
public class CustomizableResources extends AbstractResources {

    /**
     *
     */
    public static final ResourceKey DEFAULT_APPLICATION_RESOURCES = bundle("i18n.resources");

    protected final Logger LOG = LoggerFactory.getLogger(getClass());
    
    /**
     *
     */
    protected ResourceKey defaultResourceBundle;

    protected ResourceFileEnumerationStrategy fileEnumerationStrategy = new BasicResourceFileEnumerationStrategy();

    protected ResourceFileFactory fileFactory = new ClasspathResourceFileFactory();

    protected ResourceBundleParser bundleParser = new PropertyResourceBundleParser();

    public CustomizableResources() {
        this.defaultResourceBundle = DEFAULT_APPLICATION_RESOURCES;
    }

    public CustomizableResources(String defaultBundle) {
        this.defaultResourceBundle = ResourceKey.bundle(defaultBundle);
        LOG.debug("Configured default resource bundle: {}", defaultBundle);
    }

    public void setDefaultResourceBundle(String defaultBundle) {
        this.defaultResourceBundle = ResourceKey.bundle(defaultBundle);
        LOG.debug("Configured default resource bundle: {}", defaultBundle);
    }

    public void setFileEnumerationStrategy(ResourceFileEnumerationStrategy fileEnumerationStrategy) {
        this.fileEnumerationStrategy = fileEnumerationStrategy;
        LOG.debug("Configured file enumeration strategy: {}", fileEnumerationStrategy.getClass().getSimpleName());
    }

    public void setFileFactory(ResourceFileFactory fileFactory) {
        this.fileFactory = fileFactory;
        LOG.debug("Configured file factory: {}", fileFactory.getClass().getSimpleName());
    }

    public void setBundleParser(ResourceBundleParser bundleParser) {
        this.bundleParser = bundleParser;
        LOG.debug("Configured resource bundle parser: {}", bundleParser.getClass().getSimpleName());
    }

    /**
     *
     * @param key
     * @param locale
     * @return
     */
    protected String lookup(ResourceKey key, ResourceResolutionContext context) {
        String bundleName = bundleParser.getResourceFileName(key);
        String defaultBundleName = bundleParser.getResourceFileName(defaultResourceBundle);
        String[] bundleOptions = bundleName != null
                ? new String[] { bundleName, defaultBundleName }
                : new String[] { defaultBundleName };
        String fullKey = key.getBundle() + '.' + key.getId();
        String shortKey = key.getId();
        List<String> options = fileEnumerationStrategy.enumerateFileNameOptions(bundleOptions, context);
        for (String option : options) {
            try {
                ResourceFile file = fileFactory.getFile(key, option);
                Map<String, String> properties = bundleParser.parse(file);
                if (properties.containsKey(fullKey)) {
                    return properties.get(fullKey);
                }
                if (properties.containsKey(shortKey)) {
                    return properties.get(shortKey);
                }
            } catch (MissingResourceFileException e) {
                // ignore it
            }
        }
        return null;
    }

    @Override
    public ResourceFile contentOf(String name, ResourceResolutionContext context) {
        ResourceKey key = bundle(name);
        List<String> options = fileEnumerationStrategy.enumerateFileNameOptions(new String[] { name }, context);
        for (String option : options) {
            try {
                ResourceFile file = fileFactory.getFile(key, option);
                file.asStream().close();
                return file;
            } catch (MissingResourceFileException e) {
            } catch (IOException e) {
            }
        }
        throw new MissingResourceFileException(key);
    }

}
