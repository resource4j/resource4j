package com.example.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.resource4j.spring.AutowiredResource;
import com.github.resource4j.spring.annotations.InjectBundle;
import com.github.resource4j.spring.annotations.InjectValue;

@Service
@InjectBundle(value = "application", id = "mail")
public class MailService {

	private static final Logger LOG = LoggerFactory.getLogger(MailService.class);
	
	@InjectValue
	private String server;
	
	@InjectValue
	private String user;
	
	@InjectValue
	private String password;
	
	@InjectValue
	private MailAddress sender;
	
	public void sendMessage(MailAddress recepient, String message) throws MailDeliveryException {
		LOG.info("Sending e-mail via {} from {} to {}", server, sender, recepient);
	}
	
}
