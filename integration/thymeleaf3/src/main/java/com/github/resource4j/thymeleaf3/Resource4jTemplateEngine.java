package com.github.resource4j.thymeleaf3;

import com.github.resource4j.resources.Resources;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;

import java.util.List;
import java.util.function.Consumer;

public record Resource4jTemplateEngine(ITemplateEngine engine,
                                       List<ContextSpecificAttributeResolver> contextSpecificAttributeResolvers)
        implements ContextAwareTemplateEngine<ITemplateEngine> {

    public static Resource4jTemplateEngine createEngine(Resources resources,
                                                        Consumer<AbstractConfigurableTemplateResolver> resolverConfig) {
        TemplateEngine delegate = new TemplateEngine();
        delegate.setMessageResolver(new Resource4jMessageResolver(resources));
        Resource4jTemplateResolver resolver = new Resource4jTemplateResolver(resources);
        resolverConfig.accept(resolver);
        delegate.setTemplateResolver(resolver);
        return new Resource4jTemplateEngine(delegate, List.of(new LocaleAttributeResolver()));
    }

    @Override
    public IEngineConfiguration getConfiguration() {
        return engine.getConfiguration();
    }


}
