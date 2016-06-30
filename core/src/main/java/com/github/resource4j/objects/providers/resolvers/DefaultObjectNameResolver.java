package com.github.resource4j.objects.providers.resolvers;

import com.github.resource4j.resources.context.ResourceResolutionComponent;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;
import static java.lang.String.join;

public class DefaultObjectNameResolver implements ObjectNameResolver {

    public static final String FILE_NAME_SEPARATOR = "-";

    public static final String COMPONENT_SEPARATOR = "-";

    public static final String SECTION_SEPARATOR = "_";

    private String fileNameSeparator = FILE_NAME_SEPARATOR;

    private String componentSeparator = COMPONENT_SEPARATOR;

    private String sectionSeparator = SECTION_SEPARATOR;

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

}
