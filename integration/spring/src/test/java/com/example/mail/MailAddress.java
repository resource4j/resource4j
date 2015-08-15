package com.example.mail;

public class MailAddress {
	
	private String email;
	
	private String name;

	public MailAddress() {
	}
	
	public MailAddress(String email) {
	}
	
	public MailAddress(String email, String name) {
		super();
		this.email = email;
		this.name = name;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		if (name == null) {
			return email;
		} else {
			return name + " <" + email + ">";
		}
	}
	
}
