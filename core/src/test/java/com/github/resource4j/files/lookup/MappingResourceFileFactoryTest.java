package com.github.resource4j.files.lookup;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.files.parsers.ResourceParsers.string;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.resource4j.files.MissingResourceFileException;
import com.github.resource4j.files.ResourceFile;

public class MappingResourceFileFactoryTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	private MappingResourceFileFactory factory;
	
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
		ResourceFileFactory textFiles = new AbsolutePathResourceFileFactory(textFolder);
		ResourceFileFactory allFiles = new AbsolutePathResourceFileFactory(folder.newFolder("all"));
		
		
		Map<String,ResourceFileFactory> mappings = new LinkedHashMap<>();
		mappings.put(".*\\.txt$", textFiles);
		mappings.put(".+", allFiles);
		
		factory = new MappingResourceFileFactory();
		factory.setMappings(mappings);
		ResourceFile file = factory.getFile(bundle(textFileName), textFileName);
		assertEquals(textContent , file.parsedTo(string()).asIs());
	}
	
	@Test(expected=MissingResourceFileException.class)
	public void testMappingSelectedInCorrectSequence2() throws Exception {
		File textFolder = folder.newFolder("texts");
		String textFileName = "test.txt";
		String textContent = "Hello, world!";
		File textFile = new File(textFolder, textFileName);
		try (FileWriter writer = new FileWriter(textFile)) {
			writer.append(textContent);
			writer.flush();
		}
		ResourceFileFactory textFiles = new AbsolutePathResourceFileFactory(textFolder);
		ResourceFileFactory allFiles = new AbsolutePathResourceFileFactory(folder.newFolder("all"));
		
		
		Map<String,ResourceFileFactory> mappings = new LinkedHashMap<>();
		mappings.put(".+", allFiles);
		mappings.put(".*\\.txt$", textFiles);
		
		factory = new MappingResourceFileFactory();
		factory.setMappings(mappings);
		factory.getFile(bundle(textFileName), textFileName);
	}
	
}
