package com.github.resource4j.objects.providers.resolvers;

import com.github.resource4j.resources.context.ResourceResolutionComponent;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.util.function.Supplier;

import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;
import static java.lang.String.join;

public class DefaultObjectNameResolver implements ObjectNameResolver {

    public static final String FILE_NAME_SEPARATOR = ResourceResolutionContext.DEFAULT_COMPONENT_SEPARATOR;

    public static final String COMPONENT_SEPARATOR = ResourceResolutionContext.DEFAULT_COMPONENT_SEPARATOR;

    public static final String SECTION_SEPARATOR = ResourceResolutionContext.DEFAULT_SECTION_SEPARATOR;

    private String fileNameSeparator = FILE_NAME_SEPARATOR;

    private String componentSeparator = COMPONENT_SEPARATOR;

    private String sectionSeparator = SECTION_SEPARATOR;

    public static Builder defaultResolver() {
        return new Builder();
    }

    public DefaultObjectNameResolver() {
    }

    public DefaultObjectNameResolver(String fileNameSeparator, String componentSeparator, String sectionSeparator) {
        this.fileNameSeparator = fileNameSeparator;
        this.componentSeparator = componentSeparator;
        this.sectionSeparator = sectionSeparator;
    }

    @Override
    public String resolve(String name, ResourceResolutionContext context) {
        if (name == null) {
            throw new NullPointerException("name");
        } else if (name.isEmpty()) {
            throw new IllegalArgumentException("Empty file name cannot be resolved");
        }
        if (context == null) {
            context = withoutContext();
        }
        StringBuilder fileName = new StringBuilder().append(name);
        String extension = "";
        int extPosition = name.lastIndexOf('.');
        if (extPosition > 0) {
            fileName.delete(extPosition, fileName.length());
            extension = name.substring(extPosition);
        }
        if (!context.isEmpty()) {
            fileName.append(fileNameSeparator);
        }

        for (int i = 0; i < context.components().length; i++) {
            if (i > 0) {
                fileName.append(componentSeparator);
            }
            ResourceResolutionComponent component = context.components()[i];
            fileName.append(join(sectionSeparator, component.sections()));
        }

        fileName.append(extension);
        return fileName.toString();
    }

    public static class Builder implements Supplier<DefaultObjectNameResolver> {
        private String fileNameSeparator = FILE_NAME_SEPARATOR;
        private String componentSeparator = COMPONENT_SEPARATOR;
        private String sectionSeparator = SECTION_SEPARATOR;
        private Builder() {
        }

        public Builder withFileNameSeparator(String fileNameSeparator) {
            this.fileNameSeparator = fileNameSeparator;
            return this;
        }

        public Builder withComponentSeparator(String componentSeparator) {
            this.componentSeparator = componentSeparator;
            return this;
        }

        public Builder withSectionSeparator(String sectionSeparator) {
            this.sectionSeparator = sectionSeparator;
            return this;
        }

        @Override
        public DefaultObjectNameResolver get() {
            return new DefaultObjectNameResolver(fileNameSeparator, componentSeparator, sectionSeparator);
        }
    }

}
