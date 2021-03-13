package com.github.resource4j.extras.xstream;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.ResourceObjectException;
import com.github.resource4j.objects.ByteArrayResourceObject;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.resource4j.extras.xstream.XStreamParser.xml;
import static org.junit.jupiter.api.Assertions.*;

public class XStreamParserTest {

	@XStreamAlias("model")
	public static class Model {
		@XStreamAsAttribute
		private String message;
		@XStreamAlias("value")
		private int value;
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}		
	} 

	@Test
	public void testXStreamParser() throws ResourceObjectException, IOException {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><model message=\"Lorem ipsum\"><value>1</value></model>";
		
		ResourceObject file = new ByteArrayResourceObject("test", "test", xml.getBytes(), 0);
		Model object = file.parsedTo(xml(Model.class)).asIs();
		assertEquals("Lorem ipsum", object.getMessage());
		assertEquals(1, object.getValue());
	}
	
	
}
