package com.github.resource4j.extras.xstream;

import java.io.IOException;

import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.ResourceFileException;
import com.github.resource4j.files.parsers.AbstractValueParser;
import com.github.resource4j.files.parsers.ResourceFileFormatException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class XStreamParser<T> extends AbstractValueParser<T> {

	private Class<T> type;
	
	private XStream xstream;
	
	public static <T> XStreamParser<T> xml(Class<T> type) {
		XStream xstream = new XStream(new StaxDriver());
		xstream.processAnnotations(type);
		return new XStreamParser<>(type, xstream);
	}

	public static <T> XStreamParser<T> xml(Class<T> type, XStream xstream) {
		return new XStreamParser<>(type, xstream);
	}
	
	public XStreamParser(Class<T> type, XStream xstream) {
		this.type = type;
		this.xstream = xstream;
	}
	
	@Override
	protected T parse(ResourceFile file) throws IOException, ResourceFileException {
		Object object = Object.class;
		try {
			object = xstream.fromXML(file.asStream());
			return type.cast(object);
		} catch (XStreamException e) {
			throw new ResourceFileFormatException(file, "Cannot parse resource file");
		} catch (ClassCastException e) {
			throw new ResourceFileFormatException(file, 
					"Resource data type " + object.getClass().getName() 
					+ " does not match requested " + type.getName());
		}
	}

}
