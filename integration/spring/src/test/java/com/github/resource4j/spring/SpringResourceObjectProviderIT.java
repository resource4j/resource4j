package com.github.resource4j.spring;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.spring.test.TestConfiguration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

import static com.github.resource4j.objects.parsers.ResourceParsers.string;
import static com.github.resource4j.resources.context.ResourceResolutionContext.in;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class SpringResourceObjectProviderIT {

	@Autowired
	private Resources resources;
	
	@Test
	public void testResourceFileLoadedBySpringFactory() {
		ResourceObject file = resources.contentOf("example/document/test.txt", in(Locale.US));
		assertTrue(file instanceof SpringResourceObject);
		assertEquals("Text for US locale", file.parsedTo(string()).notNull().asIs());
	}
	
}
