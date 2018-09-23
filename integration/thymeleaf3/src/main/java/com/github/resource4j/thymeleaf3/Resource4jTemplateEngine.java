package com.github.resource4j.thymeleaf3;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.IThrottledTemplateProcessor;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.IContext;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Resource4jTemplateEngine implements ITemplateEngine {

    private ITemplateEngine engine;

    public Resource4jTemplateEngine(ITemplateEngine engine) {
        this.engine = engine;
    }

    @Override
    public IEngineConfiguration getConfiguration() {
        return engine.getConfiguration();
    }

    @Override
    public String process(String template, IContext context) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("locale", context.getLocale());
        return engine.process(new TemplateSpec(template, attributes), context);

    }

    @Override
    public String process(String template, Set<String> templateSelectors, IContext context) {
        return engine.process(template, templateSelectors, context);
    }

    @Override
    public String process(TemplateSpec templateSpec, IContext context) {
        return engine.process(templateSpec, context);
    }

    @Override
    public void process(String template, IContext context, Writer writer) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("locale", context.getLocale());
        engine.process(new TemplateSpec(template, attributes), context, writer);
    }

    @Override
    public void process(String template, Set<String> templateSelectors, IContext context, Writer writer) {
        engine.process(template, templateSelectors, context, writer);
    }

    @Override
    public void process(TemplateSpec templateSpec, IContext context, Writer writer) {
        engine.process(templateSpec, context, writer);
    }

    @Override
    public IThrottledTemplateProcessor processThrottled(String template, IContext context) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("locale", context.getLocale());
        return engine.processThrottled(new TemplateSpec(template, attributes), context);
    }

    @Override
    public IThrottledTemplateProcessor processThrottled(String template, Set<String> templateSelectors, IContext context) {
        return engine.processThrottled(template, templateSelectors, context);
    }

    @Override
    public IThrottledTemplateProcessor processThrottled(TemplateSpec templateSpec, IContext context) {
        return engine.processThrottled(templateSpec, context);
    }
}
