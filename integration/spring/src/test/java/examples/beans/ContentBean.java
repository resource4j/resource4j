package examples.beans;

import com.github.resource4j.spring.AutowiredResource;

public class ContentBean {

	@AutowiredResource(source = "*.txt")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
