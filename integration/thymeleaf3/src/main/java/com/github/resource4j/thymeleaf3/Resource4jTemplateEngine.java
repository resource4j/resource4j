package com.github.resource4j.thymeleaf3;

import com.github.resource4j.resources.Resources;
import org.thymeleaf.*;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Resource4jTemplateEngine implements ITemplateEngine {

    private ITemplateEngine engine;

    public Resource4jTemplateEngine(ITemplateEngine engine) {
        this.engine = engine;
    }

    public Resource4jTemplateEngine(Resources resources,
                                    Consumer<AbstractConfigurableTemplateResolver> resolverConfig) {
        this(configureEngine(resources, resolverConfig));
    }

    protected static Resource4jTemplateEngine configureEngine(Resources resources,
                                                              Consumer<AbstractConfigurableTemplateResolver> resolverConfig) {
        TemplateEngine delegate = new TemplateEngine();
        delegate.setMessageResolver(new Resource4jMessageResolver(resources));
        Resource4jTemplateResolver resolver = new Resource4jTemplateResolver(resources);
        resolverConfig.accept(resolver);
        delegate.setTemplateResolver(resolver);
        Resource4jTemplateEngine e = new Resource4jTemplateEngine(delegate);
        return e;
    }

    protected ITemplateEngine engine() {
        return engine;
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
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("locale", context.getLocale());
        engine.process(new TemplateSpec(template, templateSelectors, (String) null, attributes), context, writer);
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
