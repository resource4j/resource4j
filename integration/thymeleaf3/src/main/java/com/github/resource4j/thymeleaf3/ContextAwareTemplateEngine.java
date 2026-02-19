package com.github.resource4j.thymeleaf3;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.IThrottledTemplateProcessor;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.IContext;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ContextAwareTemplateEngine<T extends ITemplateEngine> extends ITemplateEngine {

    T engine();

    List<ContextSpecificAttributeResolver> contextSpecificAttributeResolvers();

    @Override
    default String process(String template, IContext context) {
        return process(new TemplateSpec(template, new HashMap<>()), context);
    }

    @Override
    default String process(String template, Set<String> templateSelectors, IContext context) {
        return process(new TemplateSpec(template, templateSelectors, (String) null, new HashMap<>()), context);
    }

    @Override
    default String process(TemplateSpec templateSpec, IContext context) {
        return engine().process(withContextSpecificAttributes(templateSpec, context), context);
    }

    @Override
    default void process(String template, IContext context, Writer writer) {
        process(new TemplateSpec(template, new HashMap<>()), context, writer);
    }

    @Override
    default void process(String template, Set<String> templateSelectors, IContext context, Writer writer) {
        process(new TemplateSpec(template, templateSelectors, (String) null, new HashMap<>()), context, writer);
    }

    @Override
    default void process(TemplateSpec templateSpec, IContext context, Writer writer) {
        engine().process(withContextSpecificAttributes(templateSpec, context), context, writer);
    }

    @Override
    default IThrottledTemplateProcessor processThrottled(String template, IContext context) {
        return processThrottled(new TemplateSpec(template, new HashMap<>()), context);
    }

    @Override
    default IThrottledTemplateProcessor processThrottled(String template, Set<String> templateSelectors, IContext context) {
        return processThrottled(new TemplateSpec(template, templateSelectors, (String) null, new HashMap<>()), context);
    }

    @Override
    default IThrottledTemplateProcessor processThrottled(TemplateSpec templateSpec, IContext context) {
        return engine().processThrottled(withContextSpecificAttributes(templateSpec, context), context);
    }

    private TemplateSpec withContextSpecificAttributes(TemplateSpec spec, IContext context) {
        List<ContextSpecificAttributeResolver> resolvers = contextSpecificAttributeResolvers();
        if (resolvers == null || resolvers.isEmpty()) {
            return spec;
        }
        Map<String, Object> attributes = spec.getTemplateResolutionAttributes();
        boolean updateSpec = false;
        if (attributes == null) {
            attributes = new HashMap<>();
            updateSpec = true;
        }
        for (var resolver : resolvers) {
            var value = resolver.resolve(context);
            attributes.put(resolver.name(), value);
        }
        if (updateSpec) {
            spec = new TemplateSpec(spec.getTemplate(), spec.getTemplateSelectors(), spec.getTemplateMode(), attributes);
        }
        return spec;
    }

}
