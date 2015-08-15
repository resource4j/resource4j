package com.github.resource4j.spring;

import static com.github.resource4j.spring.annotations.support.AutowiredResourceCallback.buildFileName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.example.legal.EULA;

public class ResourceFileNameAlgorithmTest {

	@Test
	public void testAbsolutePathRemainsUnchanged() {
		String fileName = buildFileName(EULA.class, "beanName", "/files/content.xml");
		assertEquals("/files/content.xml", fileName);
	}
	
	@Test
	public void testRelativePathPrecededByBeanPackage() {
		String fileName = buildFileName(EULA.class, "beanName", "files/content.xml");
		assertEquals("/com/example/legal/files/content.xml", fileName);
	}

	@Test
	public void testSimpleFileNamePrecededByBeanPackage() {
		String fileName = buildFileName(EULA.class, "beanName", "content.xml");
		assertEquals("/com/example/legal/content.xml", fileName);
	}
	
	@Test
	public void testRelativePathAndMaskAppendedToClassNameAndProvidedExtension() {
		String fileName = buildFileName(EULA.class, "beanName", "files/*.xml");
		assertEquals("/com/example/legal/files/beanName.xml", fileName);
	}

	@Test
	public void testSimpleMaskAppendedToClassNameAndProvidedExtension() {
		String fileName = buildFileName(EULA.class, "beanName", "*.xml");
		assertEquals("/com/example/legal/beanName.xml", fileName);
	}
	
	@Test
	public void testAbsolutePathAndMaskAppendedToClassNameAndProvidedExtension() {
		String fileName = buildFileName(EULA.class, "beanName", "/data/*.xml");
		assertEquals("/data/beanName.xml", fileName);
	}
	
	@Test
	public void testExtensionOnlyAppendedToBeanPackage() {
		String fileName = buildFileName(EULA.class, "beanName", ".xml");
		assertEquals("/com/example/legal/.xml", fileName);
	}
	
	@Test
	public void testMaskOnlyAppendedToBeanPackageAndReplacedWithBeanName() {
		String fileName = buildFileName(EULA.class, "beanName", "*");
		assertEquals("/com/example/legal/beanName", fileName);
	}

	@Test
	public void testMaskInAbsolutePathSubstitutedByBeanName() {
		String fileName = buildFileName(EULA.class, "beanName", "/*");
		assertEquals("/beanName", fileName);
	}	
}
