package com.github.resource4j;

import example.api.Action;
import example.api.ExampleEnum;
import org.junit.jupiter.api.Test;

import static com.github.resource4j.ResourceKey.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ResourceKeyTest {

    @Test
    public void testCreateBundleByName() {
        ResourceKey bundle = bundle("test");
        assertEquals("test", bundle.getBundle());
        assertNull(bundle.getId());
        assertEquals("test.", bundle.toString());
        assertEquals(bundle, plain(bundle.toString()));
    }

    @Test
    public void testCreateBundleByClass() {
        ResourceKey bundle = bundle(Action.class);
        assertEquals("example.api.Action", bundle.getBundle());
        assertNull(bundle.getId());
        assertEquals("example.api.Action.", bundle.toString());
        assertEquals(bundle, plain(bundle.toString()));
    }
    
    @Test
    public void testCreateKeyByClassAndId() {
        ResourceKey bundle = key(Action.class, "value");
        assertEquals("example.api.Action", bundle.getBundle());
        assertEquals("value", bundle.getId());
        assertEquals("example.api.Action.value", bundle.toString());
        assertEquals(bundle, plain(bundle.toString()));
    }
    
    @Test
    public void testGetBundleKey() {
    	ResourceKey key = key("bundle", "value");
    	ResourceKey bundle = key.bundle();
    	assertEquals("bundle", bundle.getBundle());
    	assertNull(bundle.getId());
    }
    
    @Test
    public void testCreateKeyByEnumValue() {
        ResourceKey key = key(ExampleEnum.VALUE);
        assertEquals("example.api.ExampleEnum", key.getBundle());
        assertEquals("VALUE", key.getId());
        assertEquals("example.api.ExampleEnum.VALUE", key.toString());
        assertEquals(key, plain(key.toString()));
    }
    
    @Test
    public void testCreateKeyByEnumValueInGivenBundle() {
        ResourceKey key = key(Action.class, ExampleEnum.VALUE);
        assertEquals("example.api.Action", key.getBundle());
        assertEquals("VALUE", key.getId());
        assertEquals("example.api.Action.VALUE", key.toString());
        assertEquals(key, plain(key.toString()));
    }
    
    @Test
    public void testCreateKeyByEnumValueAndChild() {
        ResourceKey key = key(ExampleEnum.VALUE, "child");
        assertEquals("example.api.ExampleEnum", key.getBundle());
        assertEquals("VALUE_child", key.getId());
        assertEquals("example.api.ExampleEnum.VALUE_child", key.toString());
        assertEquals(key, plain(key.toString()));
    }
    
    @Test
    public void testCreatePlainKey() {
    	ResourceKey key = plain("simple.test");
    	assertEquals("simple", key.getBundle());
    	assertEquals("test", key.getId());
    	assertEquals("simple.test", key.toString());
    	assertEquals(key, plain(key.toString()));
    }
    
    @Test
    public void testCreateKeyInDefaultBundle() {
    	ResourceKey key = key("value");
    	assertEquals(null, key.getBundle());
    	assertEquals("value", key.getId());
    	assertEquals(".value", key.toString());
    	assertEquals(key, plain(key.toString()));
    }
    
    @Test
    public void testCreatePlainKeyInDefaultBundle() {
    	ResourceKey key = plain("value");
    	assertEquals(null, key.getBundle());
    	assertEquals("value", key.getId());
    }
    
    @Test
    public void testCreateChildKey() {
    	ResourceKey key = plain("bundle.value").child("child");
    	assertEquals("bundle", key.getBundle());
    	assertEquals("value_child", key.getId());
    	assertEquals("bundle.value_child", key.toString());
    	assertEquals(key, plain(key.toString()));
    }
    
    @Test
    public void testCreateChildOfBundle() {
    	ResourceKey key = bundle("bundle").child("value");
    	assertEquals("bundle", key.getBundle());
    	assertEquals("value", key.getId());
    	assertEquals("bundle.value", key.toString());
    	assertEquals(key, plain(key.toString()));
    }
}
