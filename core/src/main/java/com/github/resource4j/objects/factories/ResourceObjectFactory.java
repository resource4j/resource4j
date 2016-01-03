package com.github.resource4j.objects.factories;

import com.github.resource4j.ResourceObject;

public interface ResourceObjectFactory {

    ResourceObject getObject(String name, String resolvedName);
    
}
