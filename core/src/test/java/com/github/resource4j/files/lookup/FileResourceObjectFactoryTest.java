package com.github.resource4j.files.lookup;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.files.parsers.ResourceParsers.binary;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.generic.objects.factory.FileResourceObjectFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.resource4j.MissingResourceObjectException;

public class FileResourceObjectFactoryTest {

	public static class ExistingFile {
		String name;
		byte[] content;
		
		public ExistingFile(String name, byte[] content) {
			super();
			this.name = name;
			this.content = content;
		}
		
	}
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	private FileResourceObjectFactory factory;
	
	@Before
	public void init() {
		factory = new FileResourceObjectFactory(folder.getRoot());
	}
	
	@Test
	public void testLoadingResourceFromFile() throws Exception {
		ExistingFile file = givenExistsTextFile();
		ResourceObject resourceFile = factory.getObject(file.name, file.name);
		
		byte[] parsed = resourceFile.parsedTo(binary()).asIs();
		assertEquals(file.content.length, parsed.length);
		for (int i = 0; i < parsed.length; i++) {
			assertEquals(file.content[i], parsed[i]);
		}
	}
	
	protected ExistingFile givenExistsTextFile() throws IOException {
		String sampleText = "Test";
		String fileName = "test.txt";
		File file = folder.newFile(fileName);
		try (FileWriter writer = new FileWriter(file)) {
			writer.append(sampleText);
			writer.flush();
		}
		return new ExistingFile(fileName, sampleText.getBytes());
	}

	@Test(expected=MissingResourceObjectException.class)
	public void testMissingResourceFileThrownOnDirectory() throws Exception {
		String testFile = "test.dir";
		folder.newFolder(testFile);
		factory.getObject(testFile, testFile);
	}
	
	@Test(expected=MissingResourceObjectException.class)
	public void testMissingResourceFileThrownIfFileDoesntExist() throws Exception {
		String testFile = "nonexistent";
		factory.getObject(testFile, testFile);
	}
	
	@Test(expected=MissingResourceObjectException.class)
	public void testMissingResourceFileThrownOnInvalidName() throws Exception {
		String testFile = ":invalid:";
		factory.getObject(testFile, testFile);
	}
}
