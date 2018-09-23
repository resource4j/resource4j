package com.github.resource4j.resources;

import org.junit.Test;

import static com.github.resource4j.ResourceKey.key;
import static com.github.resource4j.objects.parsers.ResourceParsers.propertyMap;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.*;
import static com.github.resource4j.resources.BundleFormat.format;
import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;
import static org.junit.Assert.assertEquals;

public class MultipleLoadersTest {

    @Test
    public void shouldCorrectlyHandleMultipleProviders() {
        RefreshableResources resources = new RefreshableResources(configure()
                .sources(patternMatching()
                            .when(".*\\.properties$", bind(classpath()).to("multi/bundles"))
                            .when(".*\\.strings", bind(classpath()).to("multi/strings"))
                            .otherwise(bind(classpath()).to("multi/other"))
                )
                .formats(format(propertyMap()), format(propertyMap(), ".strings"))
                .get());
        String value = resources.get(key("multi", "value")).notNull().asIs();
        assertEquals("json", value);
    }

    @Test
    public void shouldCorrectlyHandleMultipleProviders2() {
        RefreshableResources resources = new RefreshableResources(configure()
                .sources(patternMatching()
                        .when(".*\\.properties$", bind(classpath()).to("multi/bundles"))
                        .when(".*\\.strings", bind(classpath()).to("multi/strings"))
                        .otherwise(bind(classpath()).to("multi/other"))
                )
                .formats(format(propertyMap(), ".strings"), format(propertyMap()))
                .get());
        String value = resources.get(key("multi", "property")).notNull().asIs();
        assertEquals("bundles", value);
    }

}
