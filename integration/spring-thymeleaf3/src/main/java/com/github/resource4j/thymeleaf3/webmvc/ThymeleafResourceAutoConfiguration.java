package com.github.resource4j.thymeleaf3.webmvc;

import com.github.resource4j.resources.Resources;
import com.github.resource4j.spring.Resource4jMessageSource;
import com.github.resource4j.spring.config.Resource4jAutoConfiguration;
import com.github.resource4j.thymeleaf3.Resource4jTemplateResolver;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.MimeType;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.LinkedHashMap;

@Configuration
@ConditionalOnBean({
        Resources.class
})
@EnableConfigurationProperties(ThymeleafProperties.class)
@AutoConfigureAfter({
        Resource4jAutoConfiguration.class
})
@AutoConfigureBefore({
        ThymeleafAutoConfiguration.class
})
public class ThymeleafResourceAutoConfiguration {

    public static final String DEFAULT_SUFFIX = ".html";

    private final Resources resources;

    public ThymeleafResourceAutoConfiguration(Resources resources) {
        this.resources = resources;
    }

    @Bean
    public ISpringTemplateEngine templateEngine(ThymeleafProperties properties,
                                                ObjectProvider<ITemplateResolver> templateResolvers,
                                                ObjectProvider<IDialect> dialects) {
        Resource4jSpringTemplateEngine engine = new Resource4jSpringTemplateEngine();
        engine.setTemplateEngineMessageSource(messageSource());
        engine.setEnableSpringELCompiler(properties.isEnableSpringElCompiler());
        engine.setRenderHiddenMarkersBeforeCheckboxes(properties.isRenderHiddenMarkersBeforeCheckboxes());
        templateResolvers.orderedStream().forEach(engine::addTemplateResolver);
        dialects.orderedStream().forEach(engine::addDialect);
        return engine;
    }

    @Bean
	public Resource4jMessageSource messageSource() {
	    return new Resource4jMessageSource(resources);
	}

	@Bean
	public ITemplateResolver defaultTemplateResolver(ThymeleafProperties properties) {
        Resource4jTemplateResolver resolver = new Resource4jTemplateResolver(resources);
        if (properties != null) {
            resolver.setPrefix(properties.getPrefix());
            resolver.setSuffix(properties.getSuffix());
            resolver.setTemplateMode(properties.getMode());
            if (properties.getEncoding() != null) {
                resolver.setCharacterEncoding(properties.getEncoding().name());
            }
            resolver.setCacheable(properties.isCache());
            Integer order = properties.getTemplateResolverOrder();
            if (order != null) {
                resolver.setOrder(order);
            }
            resolver.setCheckExistence(properties.isCheckTemplate());

        } else {
            resolver.setSuffix(DEFAULT_SUFFIX);
        }
        return resolver;
	}

    @Bean
    @ConditionalOnMissingBean(name = "thymeleafViewResolver")
    public ThymeleafViewResolver thymeleafViewResolver(ThymeleafProperties properties,
                                                       ISpringTemplateEngine templateEngine) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine);
        resolver.setCharacterEncoding(properties.getEncoding().name());
        resolver.setContentType(
                appendCharset(properties.getServlet().getContentType(), resolver.getCharacterEncoding()));
        resolver.setProducePartialOutputWhileProcessing(
                properties.getServlet().isProducePartialOutputWhileProcessing());
        resolver.setExcludedViewNames(properties.getExcludedViewNames());
        resolver.setViewNames(properties.getViewNames());
        // This resolver acts as a fallback resolver (e.g. like a
        // InternalResourceViewResolver) so it needs to have low precedence
        resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 5);
        resolver.setCache(properties.isCache());
        return resolver;
    }

    private String appendCharset(MimeType type, String charset) {
        if (type.getCharset() != null) {
            return type.toString();
        }
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        parameters.put("charset", charset);
        parameters.putAll(type.getParameters());
        return new MimeType(type, parameters).toString();
    }

}
