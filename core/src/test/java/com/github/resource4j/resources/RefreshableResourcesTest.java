package com.github.resource4j.resources;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.parsers.ResourceParsers;
import com.github.resource4j.objects.providers.ExpensiveResourceObjectProvider;
import com.github.resource4j.objects.providers.mutable.HeapResourceObjectRepository;
import com.github.resource4j.objects.providers.mutable.ResourceObjectRepository;
import com.github.resource4j.objects.providers.mutable.ResourceValueRepository;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import org.junit.Test;

import java.time.Clock;
import java.util.Locale;

import static com.github.resource4j.ResourceKey.key;
import static com.github.resource4j.objects.ByteArrayResourceObjectBuilder.anObject;
import static com.github.resource4j.objects.parsers.ResourceParsers.propertyMap;
import static com.github.resource4j.objects.providers.resolvers.ResourceObjectProviderPredicates.name;
import static com.github.resource4j.resources.BundleFormat.format;
import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;
import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;
import static com.github.resource4j.resources.discovery.PropertyBundleBuilder.aPropertiesBundle;
import static com.github.resource4j.test.Builders.given;
import static org.junit.Assert.*;

public class RefreshableResourcesTest extends AbstractResourcesTest {

    @Override
    protected Resources createResources() {
        return new RefreshableResources();
    }

    @Test
    public void cycleDetectorShouldNotBlockBigBundles() {
        RefreshableResources resources = new RefreshableResources(configure().maxCycleDepth(10).get());
        assertTrue(resources.getMaxDepth() < 30);
        for (int i = 0; i < 30; i++) {
            String idx = i < 10 ? "0" + i : String.valueOf(i);
            String value = resources.get(key("big", "value" + idx)).asIs();
            assertNotNull(value);
        }
    }

    @Test
    public void shouldReturnNewValueAfterBundleIsAddedToRepository() throws Exception {
        Clock clock = Clock.systemUTC();
        String bundleName = "folder.bundle";
        String objectName = "folder/bundle.properties";

        ResourceKey key = key(bundleName, "value");
        ResourceResolutionContext ctx = withoutContext();

        ResourceObjectRepository repository = new HeapResourceObjectRepository(clock);

        RefreshableResources resources = new RefreshableResources(configure().sources(repository).get());

        OptionalString v1 = resources.get(key, ctx);
        assertNull(v1.asIs());

        ResourceObject bundle = given(aPropertiesBundle(objectName, objectName)
                .with(key.getId(),"1"));

        repository.put(objectName, ctx, bundle::asStream);
        OptionalString v2 = resources.get(key, ctx);
        assertEquals("1", v2.notNull().asIs());
    }

    @Test
    public void shouldReturnNewValueAfterItIsAddedToRepository() throws Exception {
        Clock clock = Clock.systemUTC();
        String bundleName = "folder.bundle";
        ResourceKey key = key(bundleName, "value");
        ResourceResolutionContext ctx = withoutContext();

        ResourceValueRepository repository = new HeapResourceObjectRepository(clock);

        RefreshableResources resources = new RefreshableResources(
                configure()
                        .sources(repository)
                        .get());

        OptionalString v1 = resources.get(key, ctx);
        assertNull(v1.asIs());

        repository.put(key, ctx, "1");

        OptionalString v2 = resources.get(key, ctx);
        assertEquals("1", v2.notNull().asIs());
    }

    @Test
    public void shouldReturnNewValueAfterItIsModifiedInRepository() throws Exception {
        Clock clock = Clock.systemUTC();
        String bundleName = "folder.bundle";
        ResourceKey key = key(bundleName, "value");
        ResourceResolutionContext ctx = withoutContext();

        ResourceValueRepository repository = new HeapResourceObjectRepository(clock);

        RefreshableResources resources = new RefreshableResources(
                configure()
                        .sources(repository)
                        .get());
        repository.put(key, ctx, "1");

        OptionalString v1 = resources.get(key, ctx);
        assertEquals("1", v1.notNull().asIs());

        repository.put(key, ctx, "2");

        OptionalString v2 = resources.get(key, ctx);
        assertEquals("2", v2.notNull().asIs());
    }

    @Test
    public void shouldNotFindValueAfterItIsRemovedFromRepository() throws Exception {
        Clock clock = Clock.systemUTC();
        String bundleName = "folder.bundle";
        ResourceKey key = key(bundleName, "value");
        ResourceResolutionContext ctx = withoutContext();

        ResourceValueRepository repository = new HeapResourceObjectRepository(clock);

        RefreshableResources resources = new RefreshableResources(
                configure()
                        .sources(repository)
                        .get());
        repository.put(key, ctx, "1");

        OptionalString v1 = resources.get(key, ctx);
        assertEquals("1", v1.notNull().asIs());

        repository.remove(key, ctx);

        OptionalString v2 = resources.get(key, ctx);
        assertNull(v2.asIs());
    }

    @Test
    public void test_value_loaded_with_source_supporting_requested_format() throws Exception {
        String bundleName = "folder.bundle";
        String objectName = "folder/bundle.properties";
        ResourceKey key = key(bundleName, "value");

        Clock clock = Clock.systemUTC();

        ResourceResolutionContext ctx = in("ctx");

        ResourceObject bundleSlowCtx = given(aPropertiesBundle(objectName, "folder/bundle-ctx.properties").with("value","slow-ctx"));
        ResourceObject bundleSlowCommon = given(aPropertiesBundle(objectName, objectName).with("value","slow"));

        HeapResourceObjectRepository pSlow = new HeapResourceObjectRepository(clock);
        pSlow.put(objectName, ctx, bundleSlowCtx::asStream);
        pSlow.put(objectName, withoutContext(), bundleSlowCommon::asStream);

        ResourceObject bundleFastCommon = given(aPropertiesBundle().with("value","fast"));
        HeapResourceObjectRepository pFast = new HeapResourceObjectRepository(clock);
        pFast.put(objectName, ctx, bundleFastCommon::asStream);

        ExpensiveResourceObjectProvider mSlow = managed("slow", pSlow);
        mSlow.whenRequested(bundleName, ctx).sleep(1000);
        ExpensiveResourceObjectProvider mFast = managed("fast", pFast);

        RefreshableResources resources = new RefreshableResources(
                configure()
                        .sources(mSlow.objectsLike(name(".\\.xml$")),
                                mFast.objectsLike(name(".\\.properties$")))
                        .formats(format(propertyMap(), ".properties"))
                        .get());

        OptionalString value = resources.get(key, ctx);
        assertEquals("fast", value.asIs());
    }

    @Test
    public void test_highest_priority_source_used_when_resource_in_multiple_sources() throws Exception {
        String objectPath = "location/example.object";

        Clock clock = Clock.systemUTC();

        ResourceObject bundleSlowCommon = given(anObject(objectPath, withoutContext()).withContent("slow"));

        HeapResourceObjectRepository pSlow = new HeapResourceObjectRepository(clock);
        pSlow.put(objectPath, withoutContext(), bundleSlowCommon::asStream);

        ResourceObject bundleFastCommon = given(anObject(objectPath, withoutContext()).withContent("fast"));
        HeapResourceObjectRepository pFast = new HeapResourceObjectRepository(clock);
        pFast.put(objectPath, withoutContext(), bundleFastCommon::asStream);

        ExpensiveResourceObjectProvider mSlow = managed("slow", pSlow);
        ExpensiveResourceObjectProvider mFast = managed("fast", pFast);

        RefreshableResources resources = new RefreshableResources(configure().sources(mSlow, mFast).get());

        ResourceObject object = resources.contentOf(objectPath, withoutContext());

        assertEquals("slow", object.parsedTo(ResourceParsers.string()).asIs());
    }

    @Test
    public void test_object_loaded_from_slow_source_with_higher_priority() throws Exception {
        String objectPath = "location/example.object";

        Clock clock = Clock.systemUTC();
        ResourceResolutionContext ctx = in(Locale.US);

        ResourceObject bundleSlowCtx = given(anObject(objectPath, ctx).withContent("slow ctx"));

        HeapResourceObjectRepository pSlow = new HeapResourceObjectRepository(clock);
        pSlow.put(objectPath, ctx, bundleSlowCtx::asStream);

        ResourceObject bundleFastCommon = given(anObject(objectPath, withoutContext()).withContent("fast common"));
        HeapResourceObjectRepository pFast = new HeapResourceObjectRepository(clock);
        pFast.put(objectPath, withoutContext(), bundleFastCommon::asStream);

        ExpensiveResourceObjectProvider mSlow = managed("slow", pSlow);
        ExpensiveResourceObjectProvider mFast = managed("fast", pFast);

        RefreshableResources resources = new RefreshableResources(configure().sources(mSlow, mFast).get());

        ResourceObject object = resources.contentOf(objectPath, ctx);

        assertEquals("slow ctx", object.parsedTo(ResourceParsers.string()).asIs());

    }

    @Test
    public void test_value_reloaded_when_source_modified() throws Exception {
        String bundleName = "folder.bundle";
        String objectName = "folder/bundle.properties";
        ResourceKey key = key(bundleName, "value");
        Clock clock = Clock.systemUTC();
        ResourceResolutionContext ctx = in("ctx");
        ResourceObject bundle1 = given(
                aPropertiesBundle(objectName, "folder/bundle-ctx.properties").with("value","1"));

        HeapResourceObjectRepository source = new HeapResourceObjectRepository(clock);
        source.put(objectName, ctx, bundle1::asStream);

        RefreshableResources resources = new RefreshableResources(configure().sources(source).get());

        OptionalString value = resources.get(key, ctx);
        assertEquals("1", value.asIs());

        ResourceObject bundle2 = given(
                aPropertiesBundle(objectName, "folder/bundle-ctx.properties").with("value","2"));
        source.put(objectName, ctx, bundle2::asStream);
        value = resources.get(key, ctx);
        assertEquals("2", value.asIs());
    }

    @Test
    public void test_value_loaded_from_slow_source_with_higher_priority() throws Exception {
        String bundleName = "folder.bundle";
        String objectName = "folder/bundle.properties";
        ResourceKey key = key(bundleName, "value");

        Clock clock = Clock.systemUTC();

        ResourceResolutionContext ctx = in("ctx");

        ResourceObject bundleSlowCtx = given(aPropertiesBundle(objectName, "folder/bundle-ctx.properties").with("value","slow-ctx"));
        ResourceObject bundleSlowCommon = given(aPropertiesBundle(objectName, objectName).with("value","slow"));

        HeapResourceObjectRepository pSlow = new HeapResourceObjectRepository(clock);
        pSlow.put(objectName, ctx, bundleSlowCtx::asStream);
        pSlow.put(objectName, withoutContext(), bundleSlowCommon::asStream);

        ResourceObject bundleFastCommon = given(aPropertiesBundle().with("value","fast"));
        HeapResourceObjectRepository pFast = new HeapResourceObjectRepository(clock);
        pFast.put(objectName, ctx, bundleFastCommon::asStream);

        ExpensiveResourceObjectProvider mSlow = managed("slow", pSlow);
        mSlow.whenRequested(bundleName, ctx).sleep(1000);
        ExpensiveResourceObjectProvider mFast = managed("fast", pFast);

        RefreshableResources resources = new RefreshableResources(configure().sources(mSlow, mFast).get());

        OptionalString value = resources.get(key, ctx);
        assertEquals("slow-ctx", value.asIs());
    }

    protected static ExpensiveResourceObjectProvider managed(String name, HeapResourceObjectRepository repository) {
        return new ExpensiveResourceObjectProvider(name, repository);
    }

}
