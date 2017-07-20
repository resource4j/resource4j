package com.github.resource4j.thymeleaf3;

import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;
import static org.junit.Assert.*;

import java.util.Locale;

import com.github.resource4j.resources.RefreshableResourcesConfigurator;
import com.github.resource4j.resources.ResourcesConfigurationBuilder;
import com.github.resource4j.spring.config.Resource4jAutoConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ Resource4jAutoConfiguration.class, Thymeleaf3ResourceConfiguration.class })
public class Resource4jResourceResolverTest {

	@Autowired
	private ITemplateEngine engine;
	
	@Test
	public void testRenderGermanPage() {
		String content = engine.process("example/pages/page.html", new Context(new Locale("de", "DE")));
		assertTrue(content.contains("Deutsch"));
	}
	
}
