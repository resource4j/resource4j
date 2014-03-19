package com.github.resource4j.examples.domain;

import org.springframework.context.annotation.*;

import com.github.resource4j.files.lookup.ResourceFileFactory;
import com.github.resource4j.resources.DefaultResources;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.spring.SpringResourceFileFactory;
import com.github.resource4j.thymeleaf.Resource4jMessageResolver;
import com.github.resource4j.thymeleaf.Resource4jResourceResolver;
import com.github.resource4j.thymeleaf.ThymeleafResourceConfiguration;

@Configuration
@ComponentScan(basePackages="com.github.resource4j.examples.domain.service")
@Import(ThymeleafResourceConfiguration.class)
public class CoreConfiguration {

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
	
}
