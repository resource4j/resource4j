package com.github.resource4j.example.client.languages;

import java.util.Locale;

public enum Language {

	RUSSIAN("ru"),
	ENGLISH("en");
	
	private Locale locale;
	
	private Language(String lang) {
		this.locale = new Locale(lang);
	}
	
	public Locale getLocale() {
		return locale;
	}
	
}
