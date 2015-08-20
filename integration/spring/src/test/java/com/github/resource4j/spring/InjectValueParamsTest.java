package com.github.resource4j.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import example.config.MailAddress;
import example.config.MailConfig;
import example.config.MailMock;
import example.config.MailService;


@RunWith(SpringJUnit4ClassRunner.class)
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
