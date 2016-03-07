package com.github.resource4j.objects.providers;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.objects.ByteArrayResourceObjectBuilder.anObject;
import static com.github.resource4j.objects.parsers.ResourceParsers.string;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.filesIn;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.inHeap;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.patternMatching;
import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static com.github.resource4j.test.Builders.given;
import static com.github.resource4j.test.TestClock.testFixed;
import static java.time.Clock.systemUTC;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Locale;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.providers.mutable.FileResourceObjectRepository;
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
        files.put(object.name(), in(Locale.US), object::asStream);

		ResourceObject file = provider.get(object.name(), in(Locale.US));
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
        files.put(object.name(), in(Locale.US), object::asStream);

        provider.get(object.name(), in(Locale.US));
	}
	
}
