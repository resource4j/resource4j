package com.github.resource4j.resources;

import com.github.resource4j.resources.DefaultResources;
import com.github.resource4j.resources.Resources;

public class DefaultResourcesTest extends AbstractResourcesTest {

    @Override
    protected Resources createResources() {
        return new DefaultResources();
    }

}
