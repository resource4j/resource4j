package com.github.resource4j.spring;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.example.legal.EULA;
import com.github.resource4j.spring.annotations.support.FileNamePattern;

public class ResourceFileNameAlgorithmTest {

	@Test
	public void testAbsolutePathRemainsUnchanged() {
		String fileName = FileNamePattern.build(EULA.class, "beanName", "/files/content.xml");
		assertEquals("/files/content.xml", fileName);
	}
	
	@Test
	public void testRelativePathPrecededByBeanPackage() {
		String fileName = FileNamePattern.build(EULA.class, "beanName", "files/content.xml");
		assertEquals("/com/example/legal/files/content.xml", fileName);
	}

	@Test
	public void testSimpleFileNamePrecededByBeanPackage() {
		String fileName = FileNamePattern.build(EULA.class, "beanName", "content.xml");
		assertEquals("/com/example/legal/content.xml", fileName);
	}
	
	@Test
	public void testRelativePathAndMaskAppendedToClassNameAndProvidedExtension() {
		String fileName = FileNamePattern.build(EULA.class, "beanName", "files/*.xml");
		assertEquals("/com/example/legal/files/beanName.xml", fileName);
	}

	@Test
	public void testSimpleMaskAppendedToClassNameAndProvidedExtension() {
		String fileName = FileNamePattern.build(EULA.class, "beanName", "*.xml");
		assertEquals("/com/example/legal/beanName.xml", fileName);
	}
	
	@Test
	public void testAbsolutePathAndMaskAppendedToClassNameAndProvidedExtension() {
		String fileName = FileNamePattern.build(EULA.class, "beanName", "/data/*.xml");
		assertEquals("/data/beanName.xml", fileName);
	}
	
	@Test
	public void testExtensionOnlyAppendedToBeanPackage() {
		String fileName = FileNamePattern.build(EULA.class, "beanName", ".xml");
		assertEquals("/com/example/legal/.xml", fileName);
	}
	
	@Test
	public void testMaskOnlyAppendedToBeanPackageAndReplacedWithBeanName() {
		String fileName = FileNamePattern.build(EULA.class, "beanName", "*");
		assertEquals("/com/example/legal/beanName", fileName);
	}

	@Test
	public void testMaskInAbsolutePathSubstitutedByBeanName() {
		String fileName = FileNamePattern.build(EULA.class, "beanName", "/*");
		assertEquals("/beanName", fileName);
	}	
}
