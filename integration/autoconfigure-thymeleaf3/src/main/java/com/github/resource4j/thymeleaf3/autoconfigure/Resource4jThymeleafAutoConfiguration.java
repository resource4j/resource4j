package com.github.resource4j.thymeleaf3.autoconfigure;

import com.github.resource4j.resources.Resources;
import com.github.resource4j.spring.Resource4jMessageSource;
import com.github.resource4j.spring.config.Resource4jAutoConfiguration;
import com.github.resource4j.thymeleaf3.ContextSpecificAttributeResolver;
import com.github.resource4j.thymeleaf3.LocaleAttributeResolver;
import com.github.resource4j.thymeleaf3.Resource4jTemplateResolver;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.thymeleaf.autoconfigure.ThymeleafAutoConfiguration;
import org.springframework.boot.thymeleaf.autoconfigure.ThymeleafProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.thymeleaf.spring6.ISpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.List;

import static java.util.stream.Collectors.joining;

@AutoConfiguration
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
public class Resource4jThymeleafAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(Resource4jThymeleafAutoConfiguration.class);

    public static final String DEFAULT_SUFFIX = ".html";

    private final Resources resources;

    public Resource4jThymeleafAutoConfiguration(Resources resources) {
        this.resources = resources;
    }

    @Bean
    public LocaleAttributeResolver localeAttributeResolver() {
        return new LocaleAttributeResolver();
    }

    @Order(1)
    @Bean
    public BeanPostProcessor engineWrapper(List<ContextSpecificAttributeResolver> contextSpecificAttributeResolvers) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(@NonNull Object bean,
                                                         @NonNull String beanName) throws BeansException {
                if (bean instanceof ISpringTemplateEngine engine) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("SpringTemplateEngine is now context-aware, passing following attributes to template resolver: {}",
                                contextSpecificAttributeResolvers.stream().map(ContextSpecificAttributeResolver::name).collect(joining(",")));
                    }
                    return new ContextAwareSpringTemplateEngine(engine, contextSpecificAttributeResolvers);
                }
                return bean;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(name = "messageSource")
    public Resource4jMessageSource messageSource() {
        return new Resource4jMessageSource(resources);
    }

    @Bean
    @ConditionalOnMissingBean(name = "defaultTemplateResolver")
    public ITemplateResolver defaultTemplateResolver(ThymeleafProperties properties) {
        Resource4jTemplateResolver resolver = new Resource4jTemplateResolver(resources);
        if (properties != null) {
            resolver.setPrefix(properties.getPrefix());
            resolver.setSuffix(properties.getSuffix());
            resolver.setTemplateMode(properties.getMode());
            resolver.setCharacterEncoding(properties.getEncoding().name());
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


}
