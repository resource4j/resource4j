package com.github.resource4j.objects.providers;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.providers.events.ResourceObjectEventType;
import com.github.resource4j.objects.providers.mutable.ResourceObjectRepository;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.test.TestClock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Clock;
import java.util.Locale;

import static com.github.resource4j.objects.ByteArrayResourceObjectBuilder.anObject;
import static com.github.resource4j.objects.parsers.ResourceParsers.binary;
import static com.github.resource4j.objects.providers.ResourceObjectRepositoryLogger.contain;
import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;
import static com.github.resource4j.test.Builders.given;
import static com.github.resource4j.test.TestClock.testFixed;
import static java.time.Clock.systemUTC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractResourceObjectRepositoryTest {

    public TestClock clock = testFixed(systemUTC());

    private ResourceObjectRepository objects;

    @BeforeEach
    public void init(@TempDir Path path) {
        objects = createRepository(clock, path);
        clock.reset();
    }

    protected abstract ResourceObjectRepository createRepository(Clock clock, Path tmp);

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

        assertEquals(original.name(), found.name(), "object name");
        assertTrue(found.actualName().endsWith(original.actualName()),
                found.actualName() + " ends with " + original.actualName());
        assertEquals(original.size(), found.size(),"object size");
        assertTrue(Math.abs(found.lastModified() - millis) < 1000, "last modified");
        assertEquals(original.key(), found.key(), "object key");

        byte[] content = original.parsedTo(binary()).asIs();
        byte[] parsed = found.parsedTo(binary()).asIs();
        assertEquals(content.length, parsed.length, "actual content size");
        for (int i = 0; i < parsed.length; i++) {
            assertEquals(content[i], parsed[i], "content");
        }
    }

    @Test
    public void testRepositoryReturnsObjectStoredWithContext() throws Exception {
        long millis = clock.millis();

        ResourceObject original = given(anObject("name", "-en_US", ".txt"));
        objects.put(original.name(), in(Locale.US), original::asStream);

        ResourceObject found = objects.get(original.name(), in(Locale.US));

        assertEquals(original.name(), found.name(), "object name");
        assertTrue(found.actualName().endsWith(original.actualName()),
                found.actualName() + " ends with " + original.actualName());
        assertEquals(original.size(), found.size(), "object size");
        assertTrue(Math.abs(found.lastModified() - millis) < 1000, "last modified");
        assertEquals(original.key(), found.key(), "object key");

        byte[] content = original.parsedTo(binary()).asIs();
        byte[] parsed = found.parsedTo(binary()).asIs();
        assertEquals(content.length, parsed.length, "actual content size");
        for (int i = 0; i < parsed.length; i++) {
            assertEquals(content[i], parsed[i], "content");
        }
    }

    @Test
    public void testRepositoryThrowsExceptionIfRequestedObjectNotExist() throws Exception {
        String testFile = "nonexistent";
        assertThrows(MissingResourceObjectException.class,
                () -> objects.get(testFile, withoutContext()));
    }

    @Test
    public void testRepositoryThrowsExceptionIfRequestedObjectNameInvalid() throws Exception {
        String testFile = ":invalid:";
        assertThrows(MissingResourceObjectException.class,
                () -> objects.get(testFile, withoutContext()));
    }

}
