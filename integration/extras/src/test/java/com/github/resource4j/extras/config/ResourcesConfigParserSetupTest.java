package com.github.resource4j.extras.config;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.RefreshableResources;
import org.junit.Test;

import static com.github.resource4j.extras.config.ConfigMapParser.configMap;
import static com.github.resource4j.resources.BundleFormat.format;
import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;
import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;
import static org.junit.Assert.assertEquals;

public class ResourcesConfigParserSetupTest {

    @Test
    public void shouldUseConfigBundleAsSourceWhenConfigured() {
        RefreshableResources resources = new RefreshableResources(
                configure()
                        .formats(format(configMap(), ".conf"))
                        .get());
        ResourceKey key = ResourceKey.key("test", "mail.server.host");
        OptionalString value = resources.get(key, withoutContext());
        assertEquals("localhost", value.asIs());
    }

}
