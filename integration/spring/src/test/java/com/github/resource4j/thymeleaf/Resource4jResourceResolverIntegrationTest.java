package com.github.resource4j.thymeleaf;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/spring/thymeleafContext.xml")
public class Resource4jResourceResolverIntegrationTest {

	@Autowired
	private TemplateEngine engine;
	
	@Test
	public void testRenderGermanPage() {
		String content = engine.process("page", new Context(new Locale("de", "DE")));
		assertTrue(content.contains("Deutsch"));
	}
	
}
