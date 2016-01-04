package com.github.resource4j.objects.providers;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.test.TestClock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.Clock;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.objects.ByteArrayResourceObjectBuilder.anObject;
import static com.github.resource4j.objects.parsers.ResourceParsers.binary;
import static com.github.resource4j.test.Builders.given;
import static com.github.resource4j.test.TestClock.testFixed;
import static java.time.Clock.systemUTC;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public abstract class AbstractResourceObjectRepositoryTest {

    @Rule
    public TestClock clock = testFixed(systemUTC());

    private ResourceObjectRepository objects;

    @Before
    public void init() {
        objects = createRepository(clock);
    }

    protected abstract ResourceObjectRepository createRepository(Clock clock);

    protected ResourceObjectRepository objects() {
        return objects;
    }

    @Test
    public void testRepositoryContainsStoredObject() throws Exception {
        ResourceObject object = given(anObject());
        objects.put(object.name(), object.actualName(), object::asStream);
        assertTrue(objects.contains(object.actualName()));
    }

        @Test
    public void testRepositoryReturnsStoredObject() throws Exception {
        long millis = clock.millis();

        ResourceObject original = given(anObject());
        objects.put(original.name(), original.actualName(), original::asStream);

        ResourceObject found = objects.get(original.name(), original.actualName());

        assertEquals("object name", original.name(), found.name());
        assertTrue("object resolved name", found.actualName().endsWith(original.actualName()));
        assertEquals("object size", original.size(), found.size());
        assertTrue("last modified", Math.abs(found.lastModified() - millis) < 1000);
        assertEquals("object key", original.key(), found.key());

        byte[] content = original.parsedTo(binary()).asIs();
        byte[] parsed = found.parsedTo(binary()).asIs();
        assertEquals("actual content size", content.length, parsed.length);
        for (int i = 0; i < parsed.length; i++) {
            assertEquals("content", content[i], parsed[i]);
        }
    }

    @Test(expected=MissingResourceObjectException.class)
    public void testRepositoryThrowsExceptionIfRequestedObjectNotExist() throws Exception {
        String testFile = "nonexistent";
        objects.get(testFile, testFile);
    }

    @Test(expected=MissingResourceObjectException.class)
    public void testRepositoryThrowsExceptionIfRequestedObjectNameInvalid() throws Exception {
        String testFile = ":invalid:";
        objects.get(testFile, testFile);
    }

}
