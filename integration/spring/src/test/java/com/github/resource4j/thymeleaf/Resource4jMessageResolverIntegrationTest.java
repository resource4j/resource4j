package com.github.resource4j.thymeleaf;

import static org.junit.Assert.assertTrue;

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
public class Resource4jMessageResolverIntegrationTest {
	
	@Autowired
	private TemplateEngine engine;
	
	@Test
	public void testRenderRussianPageSubstitutesRussianString() {
		String content = engine.process("page", new Context(new Locale("ru", "RU")));
		assertTrue(content.contains("Пример страницы"));
	}
	
	@Test
	public void testRenderRussianPageUsesDefaultBundle() {
		String content = engine.process("page", new Context(new Locale("ru", "RU")));
		assertTrue(content.contains("Latin"));
	}
}
