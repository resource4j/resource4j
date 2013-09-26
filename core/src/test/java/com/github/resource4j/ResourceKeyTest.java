package com.github.resource4j;

import static com.github.resource4j.ResourceKey.bundle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import example.api.Action;

public class ResourceKeyTest {

    private static final String TEST_BUNDLE = "test";

    @Test
    public void testCreateBundleByName() {
        ResourceKey bundle = bundle(TEST_BUNDLE);
        assertEquals(TEST_BUNDLE, bundle.getBundle());
        assertNull(bundle.getId());
    }

    @Test
    public void testCreateBundleByClass() {
        ResourceKey bundle = bundle(Action.class);
        assertEquals("example.api.Action", bundle.getBundle());
        assertNull(bundle.getId());
    }
}
