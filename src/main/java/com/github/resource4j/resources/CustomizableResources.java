package com.github.resource4j.resources;

import static com.github.resource4j.ResourceKey.bundle;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.MissingResourceFileException;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.lookup.ClasspathResourceFileFactory;
import com.github.resource4j.files.lookup.DefaultResourceFileEnumerationStrategy;
import com.github.resource4j.files.lookup.PropertyResourceBundleParser;
import com.github.resource4j.files.lookup.ResourceBundleParser;
import com.github.resource4j.files.lookup.ResourceFileEnumerationStrategy;
import com.github.resource4j.files.lookup.ResourceFileFactory;

/**
 *
 * @author Ivan Gammel
 *
 */
public class CustomizableResources extends AbstractResources {


    /**
     *
     */
    public final static ResourceKey DEFAULT_APPLICATION_RESOURCES = bundle("i18n.resources");

    /**
     *
     */
    protected ResourceKey defaultResourceBundle;

    protected ResourceFileEnumerationStrategy fileEnumerationStrategy = new DefaultResourceFileEnumerationStrategy();

    protected ResourceFileFactory fileFactory = new ClasspathResourceFileFactory();

    protected ResourceBundleParser bundleParser = new PropertyResourceBundleParser();

    public CustomizableResources() {
        this.defaultResourceBundle = DEFAULT_APPLICATION_RESOURCES;
    }

    public CustomizableResources(String defaultBundle) {
        this.defaultResourceBundle = ResourceKey.bundle(defaultBundle);
    }

    public void setDefaultResourceBundle(String defaultBundle) {
        this.defaultResourceBundle = ResourceKey.bundle(defaultBundle);
    }

    public void setFileEnumerationStrategy(ResourceFileEnumerationStrategy fileEnumerationStrategy) {
        this.fileEnumerationStrategy = fileEnumerationStrategy;
    }

    public void setFileFactory(ResourceFileFactory fileFactory) {
        this.fileFactory = fileFactory;
    }

    public void setBundleParser(ResourceBundleParser bundleParser) {
        this.bundleParser = bundleParser;
    }

    /**
    *
    * @param key
    * @param locale
    * @return
    */
   protected String lookup(ResourceKey key, Locale locale) {
       String bundleName = bundleParser.getResourceFileName(key);
       String defaultBundleName = bundleParser.getResourceFileName(defaultResourceBundle);
       String[] bundleOptions = new String[] { bundleName, defaultBundleName };
       String fullKey = key.getBundle()+'.'+key.getId();
       String shortKey = key.getId();
       List<String> options = fileEnumerationStrategy.enumerateFileNameOptions(bundleOptions, locale);
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

           }
       }
       return null;
   }

   @Override
   public ResourceFile contentOf(String name, Locale locale) {
       ResourceKey key = bundle(name);
       List<String> options = fileEnumerationStrategy.enumerateFileNameOptions(new String[] { name }, locale);
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
