package com.github.resource4j.thymeleaf;

import com.github.resource4j.objects.providers.ClasspathResourceObjectProvider;
import com.github.resource4j.objects.providers.MappingResourceObjectProvider;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.resources.RefreshableResources;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.spring.ResourceValueBeanPostProcessor;
import com.github.resource4j.spring.SpringResourceObjectProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;

@Configuration
public class ThymeleafResourceConfiguration implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	@Bean
	public ResourceObjectProvider mappingProvider() {
		MappingResourceObjectProvider factory = new MappingResourceObjectProvider();
		Map<String, ResourceObjectProvider> mappings = new LinkedHashMap<>();
		mappings.put(".+\\.properties$", new ClasspathResourceObjectProvider());
		SpringResourceObjectProvider springResourceFactory = new SpringResourceObjectProvider();
		springResourceFactory.setApplicationContext(applicationContext);
		mappings.put(".+", springResourceFactory);
		factory.setMappings(mappings);
		return factory;
	}
	
	@Bean
	public Resources resources() {
		RefreshableResources resources = new RefreshableResources(
				configure()
					.sources(mappingProvider())
					.get());
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
	
	@Bean
	public ResourceValueBeanPostProcessor resourceValueBeanPostProcessor() {
		return new ResourceValueBeanPostProcessor();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	

}
