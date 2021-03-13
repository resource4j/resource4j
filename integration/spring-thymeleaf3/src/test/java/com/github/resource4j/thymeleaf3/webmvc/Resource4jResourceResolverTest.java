package com.github.resource4j.thymeleaf3.webmvc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Configuration
@EnableAutoConfiguration
@ContextConfiguration(classes={ Resource4jResourceResolverTest.class })
public class Resource4jResourceResolverTest {

	@Autowired
	private ITemplateEngine engine;
	
	@Test
	public void testRenderGermanPage() {
		String content = engine.process("example/pages/page", new Context(new Locale("de", "DE")));
		assertTrue(content.contains("Deutsch"));
	}
	
}
