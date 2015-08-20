package example.config;

import org.springframework.stereotype.Component;

@Component
public class MailMock {

	private MailAddress sender;
	
	private MailAddress recepient;
	
	private String message;
	
	public MailMock() {		
	}
	
	public void send(MailAddress from, MailAddress to, String message) {
		this.sender = from;
		this.recepient = to;
		this.message = message;
	}

	public MailAddress getSender() {
		return sender;
	}

	public MailAddress getRecepient() {
		return recepient;
	}

	public String getMessage() {
		return message;
	}
	
}
