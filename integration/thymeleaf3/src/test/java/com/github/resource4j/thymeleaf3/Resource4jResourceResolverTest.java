package com.github.resource4j.thymeleaf3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
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
