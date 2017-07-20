package com.github.resource4j.thymeleaf3;

import com.github.resource4j.resources.Resources;
import com.github.resource4j.spring.config.Resource4jAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.dialect.SpringStandardDialect;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@ConditionalOnBean(Resources.class)
@AutoConfigureAfter(Resource4jAutoConfiguration.class)
public class Thymeleaf3ResourceConfiguration {

	@Autowired
	private Resources resources;

	@Bean
	public Resource4jMessageResolver messageResolver() {
	    return new Resource4jMessageResolver(resources);
	}
	
	@Bean
	public ITemplateResolver defaultTemplateResolver() {
	    return new Resource4jTemplateResolver(resources);
	}

	@Bean
	public ITemplateEngine defaultTemplateEngine() {
		TemplateEngine engine = new TemplateEngine();
		engine.setDialect(new SpringStandardDialect());
		engine.setTemplateResolver(defaultTemplateResolver());
		engine.setMessageResolver(messageResolver());
		return new Resource4jTemplateEngine(engine);
	}


}
