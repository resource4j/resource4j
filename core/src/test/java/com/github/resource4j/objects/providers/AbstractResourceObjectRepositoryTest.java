package com.github.resource4j.objects.providers;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.providers.events.ResourceObjectEventType;
import com.github.resource4j.objects.providers.mutable.ResourceObjectRepository;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.test.TestClock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.Clock;
import java.util.Locale;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.objects.ByteArrayResourceObjectBuilder.anObject;
import static com.github.resource4j.objects.parsers.ResourceParsers.binary;
import static com.github.resource4j.objects.providers.ResourceObjectRepositoryLogger.contain;
import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;
import static com.github.resource4j.test.Builders.given;
import static com.github.resource4j.test.TestClock.testFixed;
import static java.time.Clock.systemUTC;
import static org.junit.Assert.*;

public abstract class AbstractResourceObjectRepositoryTest {

    @Rule
    public TestClock clock = testFixed(systemUTC());

    private ResourceObjectRepository objects;

    @Before
    public void init() {
        objects = createRepository(clock);
    }

    protected abstract ResourceObjectRepository createRepository(Clock clock);

    protected ResourceObjectRepository repository() {
        return objects;
    }

    @Test
    public void shouldNotifyOnObjectAdded() throws Exception {
        ResourceObject object = given(anObject());
        ResourceObjectRepositoryLogger events = new ResourceObjectRepositoryLogger();
        objects.addListener(events);
        objects.put(object.name(), withoutContext(), object::asStream);
        assertEquals(1, events.received());
        assertThat(events, contain(e ->
                e.type() == ResourceObjectEventType.CREATED
                && e.objectName().equals(object.name())
                && e.context().equals(withoutContext())
        ));
    }

    @Test
    public void shouldNotifyOnObjectRemoved() throws Exception {
        ResourceObject object = given(anObject());
        objects.put(object.name(), withoutContext(), object::asStream);

        ResourceObjectRepositoryLogger events = new ResourceObjectRepositoryLogger();
        objects.addListener(events);
        objects.remove(object.name(), withoutContext());
        assertEquals(1, events.received());
        assertThat(events, contain(e ->
                e.type() == ResourceObjectEventType.DELETED
                        && e.objectName().equals(object.name())
                        && e.context().equals(withoutContext())
        ));
    }

    @Test
    public void shouldNotifyOnObjectModified() throws Exception {
        ResourceObjectRepositoryLogger events = new ResourceObjectRepositoryLogger();
        objects.addListener(events);

        ResourceResolutionContext ctx = withoutContext();
        String path = "main.txt";
        ResourceObject object1 = given(anObject(path, ctx).withContent("1"));
        objects.put(object1.name(), withoutContext(), object1::asStream);

        ResourceObject object2 = given(anObject(path, ctx).withContent("2"));
        objects.put(object2.name(), withoutContext(), object2::asStream);

        assertEquals(2, events.received());
        assertThat(events, contain(e ->
                e.type() == ResourceObjectEventType.MODIFIED
                        && e.objectName().equals(object2.name())
                        && e.context().equals(withoutContext())
        ));
    }

    @Test
    public void shouldNotNotifyUnsubscribedListener() throws Exception {
        ResourceObjectRepositoryLogger events = new ResourceObjectRepositoryLogger();
        objects.addListener(events);

        ResourceResolutionContext ctx = withoutContext();
        String path = "main.txt";
        ResourceObject object1 = given(anObject(path, ctx).withContent("1"));
        objects.put(object1.name(), withoutContext(), object1::asStream);

        objects.removeListener(events);

        ResourceObject object2 = given(anObject(path, ctx).withContent("2"));
        objects.put(object2.name(), withoutContext(), object2::asStream);

        assertEquals(1, events.received());
        assertThat(events, contain(e ->
                e.type() == ResourceObjectEventType.CREATED
                        && e.objectName().equals(object1.name())
                        && e.context().equals(withoutContext())
        ));
    }

    @Test
    public void testRepositoryContainsStoredObject() throws Exception {
        ResourceObject object = given(anObject());
        objects.put(object.name(), withoutContext(), object::asStream);
        assertTrue(objects.contains(object.name(), withoutContext()));
    }

    @Test
    public void testRepositoryReturnsObjectStoredWithoutContext() throws Exception {
        long millis = clock.millis();

        ResourceObject original = given(anObject());
        objects.put(original.name(), withoutContext(), original::asStream);

        ResourceObject found = objects.get(original.name(), withoutContext());

        assertEquals("object name", original.name(), found.name());
        assertTrue(found.actualName() + " ends with " + original.actualName(),
                found.actualName().endsWith(original.actualName()));
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

    @Test
    public void testRepositoryReturnsObjectStoredWithContext() throws Exception {
        long millis = clock.millis();

        ResourceObject original = given(anObject("name", "-en_US", ".txt"));
        objects.put(original.name(), in(Locale.US), original::asStream);

        ResourceObject found = objects.get(original.name(), in(Locale.US));

        assertEquals("object name", original.name(), found.name());
        assertTrue(found.actualName() + " ends with " + original.actualName(),
                found.actualName().endsWith(original.actualName()));
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
        objects.get(testFile, withoutContext());
    }

    @Test(expected=MissingResourceObjectException.class)
    public void testRepositoryThrowsExceptionIfRequestedObjectNameInvalid() throws Exception {
        String testFile = ":invalid:";
        objects.get(testFile, withoutContext());
    }

}
