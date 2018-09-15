package example.i18n;

import com.github.resource4j.spring.annotations.InjectValue;

public class Greeting {

	@InjectValue(bundle = "main")
	private String name;
	
	@InjectValue(bundleClass = Messages.class)
	private String welcome;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWelcome() {
		return welcome;
	}

	public void setWelcome(String message) {
		this.welcome = message;
	}
	
}
