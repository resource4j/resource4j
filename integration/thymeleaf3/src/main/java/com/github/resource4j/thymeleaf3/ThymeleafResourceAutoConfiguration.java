package com.github.resource4j.thymeleaf3;

import com.github.resource4j.resources.Resources;
import com.github.resource4j.spring.config.Resource4jAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@ConditionalOnBean({
        Resources.class,
        SpringTemplateEngine.class
})
@AutoConfigureAfter({
        Resource4jAutoConfiguration.class,
        ThymeleafAutoConfiguration.class
})
public class ThymeleafResourceAutoConfiguration {

    public static final String DEFAULT_SUFFIX = ".html";

    @Autowired
	private Resources resources;

	@Autowired
	private SpringTemplateEngine engine;

	@Autowired(required = false)
	private ThymeleafProperties properties;

    @Bean
	public Resource4jMessageSource messageSource() {
	    return new Resource4jMessageSource(resources);
	}
	
	@Bean
	public ITemplateResolver defaultTemplateResolver() {
        Resource4jTemplateResolver resolver = new Resource4jTemplateResolver(resources);
        if (this.properties != null) {
            resolver.setPrefix(this.properties.getPrefix());
            resolver.setSuffix(this.properties.getSuffix());
            resolver.setTemplateMode(this.properties.getMode());
            if (this.properties.getEncoding() != null) {
                resolver.setCharacterEncoding(this.properties.getEncoding().name());
            }
            resolver.setCacheable(this.properties.isCache());
            Integer order = this.properties.getTemplateResolverOrder();
            if (order != null) {
                resolver.setOrder(order);
            }
            resolver.setCheckExistence(this.properties.isCheckTemplate());

        } else {
            resolver.setSuffix(DEFAULT_SUFFIX);
        }
        return resolver;
	}

    @Bean
    @Primary
	public ITemplateEngine defaultTemplateEngine() {
        engine.setTemplateResolver(defaultTemplateResolver());
        engine.setTemplateEngineMessageSource(messageSource());
		return new Resource4jTemplateEngine(engine);
	}


}
