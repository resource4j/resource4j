package com.github.resource4j.files.lookup;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.files.parsers.ResourceParsers.string;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.resource4j.files.MissingResourceFileException;
import com.github.resource4j.files.ResourceFile;

public class AbsolutePathResourceFileFactoryTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	private AbsolutePathResourceFileFactory factory;
	
	@Before
	public void init() {
		factory = new AbsolutePathResourceFileFactory(folder.getRoot());
	}
	
	@Test
	public void testLoadingResourceFromFile() throws Exception {
		String testFile = "test.txt";
		String sampleText = "Test";
		File file = folder.newFile(testFile);
		try (FileWriter writer = new FileWriter(file)) {
			writer.append(sampleText);
			writer.flush();
		}
		ResourceFile resourceFile = factory.getFile(bundle(testFile), testFile);
		assertEquals(sampleText, resourceFile.parsedTo(string()).asIs());
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
