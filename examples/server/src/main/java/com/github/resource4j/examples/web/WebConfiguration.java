package com.github.resource4j.examples.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.spring3.view.ThymeleafViewResolver;

import com.github.resource4j.examples.domain.CoreConfiguration;

@Configuration
@ComponentScan(basePackages="com.github.resource4j.examples.web.controller")
@EnableWebMvc
@Import(CoreConfiguration.class)
public class WebConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine);
		resolver.setOrder(1);
		return resolver;
	}
	
}
