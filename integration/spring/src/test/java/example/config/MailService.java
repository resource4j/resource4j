package example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.resource4j.spring.annotations.InjectBundle;
import com.github.resource4j.spring.annotations.InjectValue;

@Service
@InjectBundle(value = "example/config/application", id = "mail")
public class MailService {
	
	@Autowired
	private MailMock mock;
	
	@InjectValue
	private String server;
	
	@InjectValue
	private String user;
	
	@InjectValue
	private String password;
	
	@InjectValue(params = {"email", "name"})
	private MailAddress sender;
	
	public void sendMessage(MailAddress recepient, String message) throws MailDeliveryException {
		mock.send(sender, recepient, message);
	}
	
}
