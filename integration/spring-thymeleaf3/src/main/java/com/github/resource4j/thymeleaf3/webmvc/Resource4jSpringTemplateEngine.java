package com.github.resource4j.thymeleaf3.webmvc;

import com.github.resource4j.resources.Resources;
import com.github.resource4j.thymeleaf3.Resource4jMessageResolver;
import com.github.resource4j.thymeleaf3.Resource4jTemplateEngine;
import com.github.resource4j.thymeleaf3.Resource4jTemplateResolver;
import org.springframework.context.MessageSource;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.function.Consumer;

public class Resource4jSpringTemplateEngine extends Resource4jTemplateEngine
        implements ISpringTemplateEngine {

    public Resource4jSpringTemplateEngine() {
        this(new SpringTemplateEngine());
    }

    public Resource4jSpringTemplateEngine(Resources resources,
                                    Consumer<AbstractConfigurableTemplateResolver> resolverConfig) {
        this(configureEngine(resources, resolverConfig));
    }

    public Resource4jSpringTemplateEngine(ISpringTemplateEngine engine) {
        super(engine);
    }

    protected static Resource4jSpringTemplateEngine configureEngine(Resources resources,
                                                              Consumer<AbstractConfigurableTemplateResolver> resolverConfig) {
        SpringTemplateEngine delegate = new SpringTemplateEngine();
        delegate.setMessageResolver(new Resource4jMessageResolver(resources));
        Resource4jTemplateResolver resolver = new Resource4jTemplateResolver(resources);
        resolverConfig.accept(resolver);
        delegate.setTemplateResolver(resolver);
        Resource4jSpringTemplateEngine e = new Resource4jSpringTemplateEngine(delegate);
        return e;
    }
    @Override
    public void setTemplateEngineMessageSource(MessageSource templateEngineMessageSource) {
        engine().setTemplateEngineMessageSource(templateEngineMessageSource);
    }

    protected SpringTemplateEngine engine() {
        return (SpringTemplateEngine) super.engine();
    }

    public void setEnableSpringELCompiler(boolean enableSpringElCompiler) {
        engine().setEnableSpringELCompiler(enableSpringElCompiler);
    }

    public void setRenderHiddenMarkersBeforeCheckboxes(boolean flag) {
        engine().setRenderHiddenMarkersBeforeCheckboxes(flag);
    }

    public void addTemplateResolver(ITemplateResolver resolver) {
        engine().addTemplateResolver(resolver);
    }

    public void addDialect(IDialect dialect) {
        engine().addDialect(dialect);
    }
}
