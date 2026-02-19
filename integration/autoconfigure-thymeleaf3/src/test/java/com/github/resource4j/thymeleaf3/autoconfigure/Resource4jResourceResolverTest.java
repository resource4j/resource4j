package com.github.resource4j.thymeleaf3.autoconfigure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.ISpringTemplateEngine;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Configuration
@EnableAutoConfiguration
@ContextConfiguration(classes={ Resource4jResourceResolverTest.class })
public class Resource4jResourceResolverTest {

	@Autowired
	private ISpringTemplateEngine engine;
	
	@Test
	public void testRenderGermanPage() {
		String content = engine.process("example/pages/page", new Context(new Locale("de", "DE")));
		assertTrue(content.contains("Deutsch"));
	}
	
}
