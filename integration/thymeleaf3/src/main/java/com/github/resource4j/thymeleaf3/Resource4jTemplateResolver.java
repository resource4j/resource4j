package com.github.resource4j.thymeleaf3;

import com.github.resource4j.resources.Resources;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.StringTemplateResource;

import java.util.Locale;
import java.util.Map;

import static com.github.resource4j.objects.parsers.ResourceParsers.string;

public class Resource4jTemplateResolver extends AbstractConfigurableTemplateResolver {

    private Resources resources;

    public Resource4jTemplateResolver(Resources resources) {
        this.resources = resources;
    }

    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration,
                                                        String ownerTemplate, String template,
                                                        String resourceName,
                                                        String characterEncoding,
                                                        Map<String, Object> templateResolutionAttributes) {
        Locale locale = (Locale) templateResolutionAttributes.get("locale");
        String string = resources.contentOf(resourceName, locale).parsedTo(string()).asIs();
        return new StringTemplateResource(string);
    }
}
