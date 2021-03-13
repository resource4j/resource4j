package com.github.resource4j.spring;

import example.config.MailAddress;
import example.config.MailConfig;
import example.config.MailMock;
import example.config.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MailConfig.class)
public class InjectValueParamsTest {
	
	@Autowired
	private MailMock mock;
	
	@Autowired
	private MailService service;

	@Test
	public void testValueWithParametersInjected() throws Exception {
		MailAddress recipient = new MailAddress("user@example.com","John Doe");
		service.sendMessage(recipient, "Test");
		assertNotNull(mock.getSender());
		assertEquals("support@acme.com", mock.getSender().getEmail());
		assertEquals("Acme Customer Support", mock.getSender().getName());
	}
	
}
