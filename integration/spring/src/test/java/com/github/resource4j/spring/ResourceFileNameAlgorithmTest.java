package com.github.resource4j.spring;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.resource4j.spring.annotations.support.FileNamePattern;

import example.document.DocumentTemplate;

public class ResourceFileNameAlgorithmTest {

	@Test
	public void testAbsolutePathRemainsUnchanged() {
		String fileName = FileNamePattern.build(DocumentTemplate.class, "beanName", "/files/content.xml");
		assertEquals("/files/content.xml", fileName);
	}
	
	@Test
	public void testRelativePathPrecededByBeanPackage() {
		String fileName = FileNamePattern.build(DocumentTemplate.class, "beanName", "files/content.xml");
		assertEquals("/example/document/files/content.xml", fileName);
	}

	@Test
	public void testSimpleFileNamePrecededByBeanPackage() {
		String fileName = FileNamePattern.build(DocumentTemplate.class, "beanName", "content.xml");
		assertEquals("/example/document/content.xml", fileName);
	}
	
	@Test
	public void testRelativePathAndMaskAppendedToClassNameAndProvidedExtension() {
		String fileName = FileNamePattern.build(DocumentTemplate.class, "beanName", "files/*.xml");
		assertEquals("/example/document/files/beanName.xml", fileName);
	}

	@Test
	public void testSimpleMaskAppendedToClassNameAndProvidedExtension() {
		String fileName = FileNamePattern.build(DocumentTemplate.class, "beanName", "*.xml");
		assertEquals("/example/document/beanName.xml", fileName);
	}
	
	@Test
	public void testAbsolutePathAndMaskAppendedToClassNameAndProvidedExtension() {
		String fileName = FileNamePattern.build(DocumentTemplate.class, "beanName", "/data/*.xml");
		assertEquals("/data/beanName.xml", fileName);
	}
	
	@Test
	public void testExtensionOnlyAppendedToBeanPackage() {
		String fileName = FileNamePattern.build(DocumentTemplate.class, "beanName", ".xml");
		assertEquals("/example/document/.xml", fileName);
	}
	
	@Test
	public void testMaskOnlyAppendedToBeanPackageAndReplacedWithBeanName() {
		String fileName = FileNamePattern.build(DocumentTemplate.class, "beanName", "*");
		assertEquals("/example/document/beanName", fileName);
	}

	@Test
	public void testMaskInAbsolutePathSubstitutedByBeanName() {
		String fileName = FileNamePattern.build(DocumentTemplate.class, "beanName", "/*");
		assertEquals("/beanName", fileName);
	}	
}
