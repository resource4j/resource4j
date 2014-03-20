package com.github.resource4j.thymeleaf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.github.resource4j.files.lookup.ResourceFileFactory;
import com.github.resource4j.resources.DefaultResources;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.spring.SpringResourceFileFactory;

@Configuration
public class ThymeleafResourceConfiguration {
	
	@Bean
	public ResourceFileFactory fileFactory() {
		return new SpringResourceFileFactory();
	}
	
	@Bean
	public Resources resources() {
		DefaultResources resources = new DefaultResources();
		resources.setFileFactory(fileFactory());
		return resources;
	}
	
	@Bean
	public Resource4jResourceResolver resourceResolver() {
		return new Resource4jResourceResolver(resources());
	}
	
	@Bean
	public Resource4jMessageResolver messageResolver() {
		return new Resource4jMessageResolver(resources());
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
		return engine;
	}
	

}
