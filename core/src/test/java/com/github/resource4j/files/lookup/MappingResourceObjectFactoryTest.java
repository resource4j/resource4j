package com.github.resource4j.files.lookup;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.files.parsers.ResourceParsers.string;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.generic.objects.factory.FileResourceObjectFactory;
import com.github.resource4j.generic.objects.factory.MappingResourceObjectFactory;
import com.github.resource4j.generic.objects.factory.ResourceObjectFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.resource4j.MissingResourceObjectException;

public class MappingResourceObjectFactoryTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	private MappingResourceObjectFactory factory;
	
	@Test
	public void testMappingSelectedInCorrectSequence() throws Exception {
		File textFolder = folder.newFolder("texts");
		String textFileName = "test.txt";
		String textContent = "Hello, world!";
		File textFile = new File(textFolder, textFileName);
		try (FileWriter writer = new FileWriter(textFile)) {
			writer.append(textContent);
			writer.flush();
		}
		ResourceObjectFactory textFiles = new FileResourceObjectFactory(textFolder);
		ResourceObjectFactory allFiles = new FileResourceObjectFactory(folder.newFolder("all"));
		
		
		Map<String,ResourceObjectFactory> mappings = new LinkedHashMap<>();
		mappings.put(".*\\.txt$", textFiles);
		mappings.put(".+", allFiles);
		
		factory = new MappingResourceObjectFactory();
		factory.setMappings(mappings);
		ResourceObject file = factory.getObject(textFileName, textFileName);
		assertEquals(textContent , file.parsedTo(string()).asIs());
	}
	
	@Test(expected=MissingResourceObjectException.class)
	public void testMappingSelectedInCorrectSequence2() throws Exception {
		File textFolder = folder.newFolder("texts");
		String textFileName = "test.txt";
		String textContent = "Hello, world!";
		File textFile = new File(textFolder, textFileName);
		try (FileWriter writer = new FileWriter(textFile)) {
			writer.append(textContent);
			writer.flush();
		}
		ResourceObjectFactory textFiles = new FileResourceObjectFactory(textFolder);
		ResourceObjectFactory allFiles = new FileResourceObjectFactory(folder.newFolder("all"));
		
		
		Map<String,ResourceObjectFactory> mappings = new LinkedHashMap<>();
		mappings.put(".+", allFiles);
		mappings.put(".*\\.txt$", textFiles);
		
		factory = new MappingResourceObjectFactory();
		factory.setMappings(mappings);
		factory.getObject(textFileName, textFileName);
	}
	
}
