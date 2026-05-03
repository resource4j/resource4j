package com.github.resource4j.thymeleaf3.autoconfigure;

import com.github.resource4j.thymeleaf3.ContextAwareTemplateEngine;
import com.github.resource4j.thymeleaf3.ContextSpecificAttributeResolver;
import org.springframework.context.MessageSource;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.spring6.ISpringTemplateEngine;

import java.util.Collection;
import java.util.List;

public record ContextAwareSpringTemplateEngine(ISpringTemplateEngine engine,
                                               List<ContextSpecificAttributeResolver> contextSpecificAttributeResolvers)
        implements ISpringTemplateEngine, ContextAwareTemplateEngine<ISpringTemplateEngine> {

    public ContextAwareSpringTemplateEngine {
        if (engine == null) {
            throw new NullPointerException("engine == null");
        }
    }

    @Override
    public void setTemplateEngineMessageSource(MessageSource templateEngineMessageSource) {
        engine.setTemplateEngineMessageSource(templateEngineMessageSource);
    }

    @Override
    public Collection<Class<?>> getAllowedClassOverridesForViews() {
        return engine.getAllowedClassOverridesForViews();
    }

    @Override
    public void setAllowedClassOverridesForViews(Collection<Class<?>> allowedClassOverridesForViews) {
        engine.setAllowedClassOverridesForViews(allowedClassOverridesForViews);
    }

    @Override
    public IEngineConfiguration getConfiguration() {
        return engine.getConfiguration();
    }

}
