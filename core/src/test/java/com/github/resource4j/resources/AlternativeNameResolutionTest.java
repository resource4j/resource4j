package com.github.resource4j.resources;

import org.junit.Test;

import java.util.Locale;

import static com.github.resource4j.ResourceKey.key;
import static com.github.resource4j.objects.parsers.ResourceParsers.propertyMap;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.bind;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.classpath;
import static com.github.resource4j.objects.providers.resolvers.DefaultObjectNameResolver.defaultResolver;
import static com.github.resource4j.resources.BundleFormat.format;
import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;
import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static org.junit.Assert.assertEquals;

public class AlternativeNameResolutionTest {

    @Test
    public void shouldCorrectlyHandleMultipleProviders() {
        RefreshableResources resources = new RefreshableResources(configure()
                .sources(bind(classpath()
                            .with(defaultResolver()
                                    .withFileNameSeparator("a")
                                    .withComponentSeparator("b")
                                    .withSectionSeparator("c"))).to("alternative"))
                .formats(format(propertyMap()))
                .get());
        String value = resources.get(key("alt", "value"), in("1", "2", Locale.US)).notNull().asIs();
        assertEquals("12", value);
    }

}
