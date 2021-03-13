package com.github.resource4j.resources.cache;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.objects.providers.mutable.HeapResourceObjectRepository;
import com.github.resource4j.resources.RefreshableResources;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.resources.discovery.PropertyBundleBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Clock;

import static com.github.resource4j.ResourceKey.key;
import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;
import static com.github.resource4j.resources.cache.TestCache.test;
import static com.github.resource4j.resources.cache.impl.GuavaCache.guava;
import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static com.github.resource4j.resources.discovery.PropertyBundleBuilder.aPropertiesBundle;
import static com.google.common.cache.CacheBuilder.newBuilder;
import static org.junit.jupiter.api.Assertions.*;

public class GuavaCacheConfigurationTest {

    @Test
    public void shouldCorrectlyEvictValue() throws IOException {
        String bundleName = "folder.bundle";
        String objectName = "folder/bundle.properties";

        int items = 20;
        int cacheSize = 15;

        Clock clock = Clock.systemUTC();
        ResourceResolutionContext ctx = in("ctx");
        PropertyBundleBuilder builder = aPropertiesBundle(objectName, "folder/bundle-ctx.properties");
        for (int i = 0; i < items; i++) {
            builder.with("value_" + i, "" + i);
        }
        HeapResourceObjectRepository source = new HeapResourceObjectRepository(clock);
        source.put(objectName, ctx, builder.build()::asStream);

        TestCache.Counter counter = new TestCache.Counter();

        Resources resources = new RefreshableResources(configure()
                .sources(source)
                .cacheValues(test(guava(newBuilder().maximumSize(cacheSize)), counter)));

        // expect all misses here
        for (int i = 0; i < items; i++) {
            ResourceKey key = key(bundleName, "value").child(String.valueOf(i));
            resources.get(key, ctx);
        }

        assertEquals(items, counter.misses());
        counter.reset();

        // now expect misses only for older items
        for (int i = items - 1; i >= 0; i--) {
            ResourceKey key = key(bundleName, "value").child(String.valueOf(i));
            if (i > items - cacheSize - 1) {
                assertEquals(0, counter.misses());
            }
            resources.get(key, ctx);
        }
        assertEquals(items - cacheSize, counter.misses());

    }

}
