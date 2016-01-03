package com.github.resource4j.resources.cache;

import static com.github.resource4j.ResourceKey.key;
import static com.github.resource4j.resources.cache.CachedValue.cached;
import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleResourceCacheTest {

    private static final String VALUE = "value";
    private static final String ANOTHER_VALUE = "another";
    private static final String KEY_ID = "id";
    private static final String ANOTHER_KEY_ID = "id2";
    private static final String KEY_BUNDLE = "bundle";
	private static final String ANY_SOURCE = "/tmp/example";
    private SimpleResourceCache<String> cache;

    @Before
    public void setup() {
        cache = new SimpleResourceCache<>();
    }

    @After
    public void shutdown() {
        cache = null;
    }

    @Test
    public void testCachedValueAccessible() {
        cache.put(key(KEY_BUNDLE, KEY_ID), in(Locale.US), cached(VALUE, ANY_SOURCE));
        CachedValue<String> cachedValue = cache.get(key(KEY_BUNDLE, KEY_ID), in(Locale.US));
        assertNotNull(cachedValue);
        assertEquals(VALUE, cachedValue.get());
    }

    @Test
    public void testSecondPutReplacesPreviousValue() {
        cache.put(key(KEY_BUNDLE, KEY_ID), in(Locale.US), cached(VALUE, ANY_SOURCE));
        cache.put(key(KEY_BUNDLE, KEY_ID), in(Locale.US), cached(ANOTHER_VALUE, ANY_SOURCE));

        CachedValue<String> cachedValue = cache.get(key(KEY_BUNDLE, KEY_ID), in(Locale.US));
        assertNotNull(cachedValue);
        assertEquals(ANOTHER_VALUE, cachedValue.get());
    }

    @Test
    public void testPutIfAbsentDoesNotOverrideExistingValue() {
        cache.put(key(KEY_BUNDLE, KEY_ID), in(Locale.US), cached(VALUE, ANY_SOURCE));
        cache.putIfAbsent(key(KEY_BUNDLE, KEY_ID), in(Locale.US), cached(ANOTHER_VALUE, ANY_SOURCE));

        CachedValue<String> cachedValue = cache.get(key(KEY_BUNDLE, KEY_ID), in(Locale.US));
        assertNotNull(cachedValue);
        assertEquals(VALUE, cachedValue.get());
    }

    @Test
    public void testValueNotInCacheAfterEvict() {
        cache.put(key(KEY_BUNDLE, KEY_ID), in(Locale.US), cached(VALUE, ANY_SOURCE));
        cache.evict(key(KEY_BUNDLE, KEY_ID), in(Locale.US));
        CachedValue<String> cachedValue = cache.get(key(KEY_BUNDLE, KEY_ID), in(Locale.US));
        assertNull(cachedValue);
    }

    @Test
    public void testCacheIsEmptyAfterClearing() {
        cache.put(key(KEY_BUNDLE, KEY_ID), in(Locale.US), cached(VALUE, ANY_SOURCE));
        cache.put(key(KEY_BUNDLE, ANOTHER_KEY_ID), in(Locale.US), cached(ANOTHER_VALUE, ANY_SOURCE));
        cache.clear();

        CachedValue<String> cachedValue = cache.get(key(KEY_BUNDLE, KEY_ID), in(Locale.US));
        assertNull(cachedValue);

        CachedValue<String> anotherCachedValue = cache.get(key(KEY_BUNDLE, ANOTHER_KEY_ID), in(Locale.US));
        assertNull(anotherCachedValue);

    }

}
