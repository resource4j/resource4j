package com.github.resource4j.objects.providers;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.providers.mutable.FileResourceObjectRepository;
import com.github.resource4j.test.TestClock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Locale;

import static com.github.resource4j.objects.ByteArrayResourceObjectBuilder.anObject;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.*;
import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static com.github.resource4j.test.Builders.given;
import static com.github.resource4j.test.TestClock.testFixed;
import static java.time.Clock.systemUTC;
import static org.junit.jupiter.api.Assertions.*;

public class MappingResourceObjectProviderTest {

	public TestClock clock = testFixed(systemUTC());

	@BeforeEach
    public void init() {
	    clock.reset();
    }

	@Test
	public void testMappingSelectedInCorrectSequence(@TempDir Path folder) throws Exception {
        FileResourceObjectRepository files = filesIn(folder.toFile());
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
	
	@Test
	public void testMappingSelectedInCorrectSequence2(@TempDir Path folder) throws Exception {
	    assertThrows(MissingResourceObjectException.class, () -> {
            FileResourceObjectRepository files = filesIn(folder.toFile());
            MappingResourceObjectProvider provider = patternMatching()
                    .when(".+", inHeap())
                    .otherwise(".*\\.txt$", files);

            ResourceObject object = given(anObject("test","-en_US",".txt"));
            files.put(object.name(), in(Locale.US), object::asStream);

            provider.get(object.name(), in(Locale.US));
        });
	}
	
}
