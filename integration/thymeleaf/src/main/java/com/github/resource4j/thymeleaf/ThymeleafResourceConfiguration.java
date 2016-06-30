package com.github.resource4j.thymeleaf;

import com.github.resource4j.resources.Resources;
import com.github.resource4j.spring.config.Resource4jAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.util.List;
import java.util.Set;

@Configuration
@ConditionalOnBean(Resources.class)
@AutoConfigureAfter(Resource4jAutoConfiguration.class)
public class ThymeleafResourceConfiguration {

	@Autowired
	private Resources resources;

	@Autowired(required = false)
	private Set<IDialect> dialects;

	@Bean
	public Resource4jResourceResolver resourceResolver() {
		return new Resource4jResourceResolver(resources);
	}
	
	@Bean
	public Resource4jMessageResolver messageResolver() {
		return new Resource4jMessageResolver(resources);
	}
	
	@Bean
	public TemplateResolver defaultTemplateResolver() {
		TemplateResolver resolver = new TemplateResolver();
		resolver.setCacheable(false);
		resolver.setResourceResolver(resourceResolver());
		resolver.setTemplateMode(StandardTemplateModeHandlers.HTML5.getTemplateModeName());
		resolver.setCharacterEncoding("UTF-8");
		resolver.setSuffix(".html");
		return resolver;
	}

	@Bean
	public SpringTemplateEngine defaultTemplateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(defaultTemplateResolver());
		engine.setMessageResolver(messageResolver());
		if (dialects != null) {
            if (!dialects.stream().filter(dialect -> dialect instanceof SpringStandardDialect).findAny().isPresent()) {
                dialects.add(new SpringStandardDialect());
            }
			engine.setDialects(dialects);
		}
		return engine;
	}


}
