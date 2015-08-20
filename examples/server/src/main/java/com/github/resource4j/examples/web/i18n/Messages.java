package com.github.resource4j.examples.web.i18n;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.github.resource4j.spring.annotations.InjectValue;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Messages {
	
	@InjectValue
	private int answer;
	
	@InjectValue
	private String message;

	public int getAnswer() {
		return answer;
	}

	public String getMessage() {
		return message;
	}
	
}
