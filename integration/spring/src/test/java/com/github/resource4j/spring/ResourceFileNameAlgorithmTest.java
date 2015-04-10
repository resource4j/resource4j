package com.github.resource4j.spring;

import static com.github.resource4j.spring.ResourceValueBeanPostProcessor.buildFileName;
import static org.junit.Assert.*;

import org.junit.Test;

import examples.beans.ContentBean;

public class ResourceFileNameAlgorithmTest {

	@Test
	public void testAbsolutePathRemainsUnchanged() {
		String fileName = buildFileName(ContentBean.class, "beanName", "/files/content.xml");
		assertEquals("/files/content.xml", fileName);
	}
	
	@Test
	public void testRelativePathPrecededByBeanPackage() {
		String fileName = buildFileName(ContentBean.class, "beanName", "files/content.xml");
		assertEquals("/examples/beans/files/content.xml", fileName);
	}

	@Test
	public void testSimpleFileNamePrecededByBeanPackage() {
		String fileName = buildFileName(ContentBean.class, "beanName", "content.xml");
		assertEquals("/examples/beans/content.xml", fileName);
	}
	
	@Test
	public void testRelativePathAndMaskAppendedToClassNameAndProvidedExtension() {
		String fileName = buildFileName(ContentBean.class, "beanName", "files/*.xml");
		assertEquals("/examples/beans/files/beanName.xml", fileName);
	}

	@Test
	public void testSimpleMaskAppendedToClassNameAndProvidedExtension() {
		String fileName = buildFileName(ContentBean.class, "beanName", "*.xml");
		assertEquals("/examples/beans/beanName.xml", fileName);
	}
	
	@Test
	public void testAbsolutePathAndMaskAppendedToClassNameAndProvidedExtension() {
		String fileName = buildFileName(ContentBean.class, "beanName", "/data/*.xml");
		assertEquals("/data/beanName.xml", fileName);
	}
	
	@Test
	public void testExtensionOnlyAppendedToBeanPackage() {
		String fileName = buildFileName(ContentBean.class, "beanName", ".xml");
		assertEquals("/examples/beans/.xml", fileName);
	}
	
	@Test
	public void testMaskOnlyAppendedToBeanPackageAndReplacedWithBeanName() {
		String fileName = buildFileName(ContentBean.class, "beanName", "*");
		assertEquals("/examples/beans/beanName", fileName);
	}

	@Test
	public void testMaskInAbsolutePathSubstitutedByBeanName() {
		String fileName = buildFileName(ContentBean.class, "beanName", "/*");
		assertEquals("/beanName", fileName);
	}	
}
