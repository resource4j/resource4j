package com.github.resource4j.resources;

import com.github.resource4j.MissingValueException;
import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.objects.providers.mutable.HeapResourceObjectRepository;
import com.github.resource4j.objects.providers.mutable.ResourceValueRepository;
import com.github.resource4j.resources.processors.BasicValuePostProcessor;
import com.github.resource4j.resources.processors.CyclicReferenceException;
import com.github.resource4j.resources.processors.ValuePostProcessingException;
import org.junit.jupiter.api.Test;

import java.time.Clock;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;
import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;
import static org.junit.jupiter.api.Assertions.*;

public class ValueSubstitutionResourcesTest {

    private ResourceValueRepository repository = new HeapResourceObjectRepository(Clock.systemUTC());
    private Resources resources = new RefreshableResources(configure()
            .sources(repository)
            .postProcessingBy(new BasicValuePostProcessor())
            .get());

    private String ref(ResourceKey key) {
        return "{" + key.getId() + "}";
    }
    private String longRef(ResourceKey key) {
        return "{" + key.getBundle() + "." + key.getId() + "}";
    }

    @Test
    public void shouldReturnSubstitutedValue() {
        ResourceKey bundle = bundle("test.bundle");
        ResourceKey key1 = bundle.child("value1");
        ResourceKey key2 = bundle.child("value2");
        String value = "OK";
        repository.put(key1, withoutContext(), value);
        repository.put(key2, withoutContext(), ref(key1));

        OptionalString result = resources.get(key2, withoutContext());
        assertEquals(value, result.asIs());
    }

    @Test
    public void shouldReturnSubstitutedValueFromAnotherBundle() {
        ResourceKey key1 = bundle("test.bundle1").child("value1");
        ResourceKey key2 = bundle("test.bundle2").child("value2");
        String value = "OK";
        repository.put(key1, withoutContext(), value);
        repository.put(key2, withoutContext(), longRef(key1));

        OptionalString result = resources.get(key2, withoutContext());
        assertEquals(value, result.asIs());
    }

    @Test
    public void shouldReturnSubstitutedValuesWithNesting() {
        ResourceKey bundle = bundle("test.bundle");
        ResourceKey key1 = bundle.child("value1");
        ResourceKey key2 = bundle.child("value2");
        ResourceKey key3 = bundle.child("value3");
        String value = "OK";
        repository.put(key1, withoutContext(), value);
        repository.put(key2, withoutContext(), ref(key1));
        repository.put(key3, withoutContext(), ref(key2));

        OptionalString result = resources.get(key3, withoutContext());
        assertEquals(value, result.asIs());
    }


    @Test
    public void shouldThrowMissingValueIfSubstitutionFails() {
        ResourceKey bundle = bundle("test.bundle");
        ResourceKey key1 = bundle.child("value1");
        ResourceKey key2 = bundle.child("value2");
        String value = "OK";
        repository.put(key1, withoutContext(), value);
        repository.put(key2, withoutContext(), "{something}");

        OptionalString result = resources.get(key2, withoutContext());
        MissingValueException e = null;
        try {
            result.notNull();
        } catch (MissingValueException ex) {
            e = ex;
        }
        assertNotNull(e);
        assertTrue(e.getCause() instanceof ValuePostProcessingException);
        assertEquals("{something}", ((ValuePostProcessingException) e.getCause()).getPartialResult());
    }

    @Test
    public void shouldDetectReferenceToSelf() {
        ResourceKey bundle = bundle("test.bundle");
        ResourceKey key = bundle.child("value1");
        repository.put(key, withoutContext(), ref(key));

        OptionalString result = resources.get(key, withoutContext());
        MissingValueException e = null;
        try {
            result.notNull();
        } catch (MissingValueException ex) {
            e = ex;
        }
        assertNotNull(e);
        assertTrue(e.getCause() instanceof CyclicReferenceException);
    }


    @Test
    public void shouldDetectCyclicDependencies() {
        ResourceKey bundle = bundle("test.bundle");
        ResourceKey key1 = bundle.child("value1");
        ResourceKey key2 = bundle.child("value2");
        ResourceKey key3 = bundle.child("value3");
        repository.put(key1, withoutContext(), ref(key3));
        repository.put(key2, withoutContext(), ref(key1));
        repository.put(key3, withoutContext(), ref(key2));

        OptionalString result = resources.get(key3, withoutContext());
        MissingValueException e = null;
        try {
            result.notNull();
        } catch (MissingValueException ex) {
            e = ex;
        }
        assertNotNull(e);
        assertTrue(e.getCause() instanceof CyclicReferenceException);
    }

}
