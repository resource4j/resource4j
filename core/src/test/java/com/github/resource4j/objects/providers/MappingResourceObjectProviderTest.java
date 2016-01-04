package com.github.resource4j.objects.providers;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.objects.ByteArrayResourceObjectBuilder.anObject;
import static com.github.resource4j.objects.parsers.ResourceParsers.string;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.filesIn;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.inHeap;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.patternMatching;
import static com.github.resource4j.test.Builders.given;
import static com.github.resource4j.test.TestClock.testFixed;
import static java.time.Clock.systemUTC;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.ByteArrayResourceObject;
import com.github.resource4j.objects.ByteArrayResourceObjectBuilder;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.test.TestClock;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class MappingResourceObjectProviderTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

    @Rule
	public TestClock clock = testFixed(systemUTC());

	@Test
	public void testMappingSelectedInCorrectSequence() throws Exception {
        FileResourceObjectRepository files = filesIn(folder.getRoot());
        MappingResourceObjectProvider provider = patternMatching()
            .when(".*\\.txt$", files)
            .otherwise(inHeap());

        ResourceObject object = given(anObject("test","-en_US",".txt"));
        files.put(object.name(), object.actualName(), object::asStream);

		ResourceObject file = provider.get(object.name(), object.actualName());
        assertEquals(object.name(), file.name());
        assertTrue(file.actualName().endsWith(object.actualName()));
		assertEquals(object.size(), file.size());
	}
	
	@Test(expected=MissingResourceObjectException.class)
	public void testMappingSelectedInCorrectSequence2() throws Exception {
        FileResourceObjectRepository files = filesIn(folder.getRoot());
        MappingResourceObjectProvider provider = patternMatching()
                .when(".+", inHeap())
                .otherwise(".*\\.txt$", files);

        ResourceObject object = given(anObject("test","-en_US",".txt"));
        files.put(object.name(), object.actualName(), object::asStream);

        provider.get(object.name(), object.actualName());
	}
	
}
