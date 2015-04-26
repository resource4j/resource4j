package com.github.resource4j.files.lookup;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.files.parsers.ResourceParsers.binary;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.resource4j.files.MissingResourceFileException;
import com.github.resource4j.files.ResourceFile;

public class AbsolutePathResourceFileFactoryTest {

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
	
	private AbsolutePathResourceFileFactory factory;
	
	@Before
	public void init() {
		factory = new AbsolutePathResourceFileFactory(folder.getRoot());
	}
	
	@Test
	public void testLoadingResourceFromFile() throws Exception {
		ExistingFile file = givenExistsTextFile();
		ResourceFile resourceFile = factory.getFile(bundle(file.name), file.name);
		assertEquals(file.content, resourceFile.parsedTo(binary()).asIs());
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

	@Test(expected=MissingResourceFileException.class)
	public void testMissingResourceFileThrownOnDirectory() throws Exception {
		String testFile = "test.dir";
		folder.newFolder(testFile);
		factory.getFile(bundle(testFile), testFile);
	}
	
	@Test(expected=MissingResourceFileException.class)
	public void testMissingResourceFileThrownIfFileDoesntExist() throws Exception {
		String testFile = "nonexistent";
		factory.getFile(bundle(testFile), testFile);
	}
	
	@Test(expected=MissingResourceFileException.class)
	public void testMissingResourceFileThrownOnInvalidName() throws Exception {
		String testFile = ":invalid:";
		factory.getFile(bundle(testFile), testFile);
	}
}
