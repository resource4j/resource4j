package com.github.resource4j.resources.discovery;

public class PropertyResourceBundleParserTest extends AbstractResourceBundleParserTest {

    protected ResourceBundleParser aParser() {
        return new PropertyResourceBundleParser();
    }

    @Override
    protected BundleBuilder aBundle() {
        return new PropertyBundleBuilder();
    }

}
