package com.github.resource4j.generic.objects.factory;

import com.github.resource4j.ResourceObject;

public interface ResourceObjectFactory {

    ResourceObject getObject(String name, String resolvedName);
    
}
