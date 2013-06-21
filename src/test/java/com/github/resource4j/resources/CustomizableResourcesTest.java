package com.github.resource4j.resources;

import com.github.resource4j.resources.CustomizableResources;
import com.github.resource4j.resources.Resources;

public class CustomizableResourcesTest extends AbstractResourcesTest {

    @Override
    protected Resources createResources() {
        return new CustomizableResources();
    }

}
