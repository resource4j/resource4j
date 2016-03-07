package com.github.resource4j.resources.refreshable;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.parsers.ResourceParsers;
import com.github.resource4j.objects.providers.ClasspathResourceObjectProvider;
import com.github.resource4j.objects.providers.mutable.HeapResourceObjectRepository;
import com.github.resource4j.refreshable.RefreshableResources;
import com.github.resource4j.resources.AbstractResourcesTest;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import org.junit.Test;

import java.time.Clock;
import java.util.Arrays;
import java.util.Locale;

import static com.github.resource4j.objects.ByteArrayResourceObjectBuilder.anObject;
import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;
import static com.github.resource4j.resources.discovery.PropertyBundleBuilder.aPropertiesBundle;
import static com.github.resource4j.test.Builders.given;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class RefreshableResourcesTest extends AbstractResourcesTest {

    @Override
    protected Resources createResources() {
        ClasspathResourceObjectProvider provider = new ClasspathResourceObjectProvider();
        RefreshableResources resources = new RefreshableResources();
        resources.setObjectProviders(singletonList(provider));
        return resources;
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

        RefreshableResources resources = new RefreshableResources();
        resources.setObjectProviders(Arrays.asList(mSlow, mFast));

        ResourceObject object = resources.contentOf(objectPath, withoutContext());

        assertEquals("slow", object.parsedTo(ResourceParsers.string()).asIs());

    }

    @Test
    public void testBasicObjectLoadingUseCase() throws Exception {
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

        RefreshableResources resources = new RefreshableResources();
        resources.setObjectProviders(Arrays.asList(mSlow, mFast));

        ResourceObject object = resources.contentOf(objectPath, ctx);

        assertEquals("slow ctx", object.parsedTo(ResourceParsers.string()).asIs());

    }

    @Test
    public void testBasicValueResolutionUseCase() throws Exception {
        String bundleName = "folder.bundle";
        String objectName = bundleName;
        ResourceKey key = ResourceKey.key(bundleName, "value");

        Clock clock = Clock.systemUTC();

        ResourceResolutionContext ctx = in("ctx");

        ResourceObject bundleSlowCtx = given(aPropertiesBundle().with("value","slow-ctx"));
        ResourceObject bundleSlowCommon = given(aPropertiesBundle().with("value","slow"));

        HeapResourceObjectRepository pSlow = new HeapResourceObjectRepository(clock);
        pSlow.put(objectName, ctx, bundleSlowCtx::asStream);
        pSlow.put(objectName, withoutContext(), bundleSlowCommon::asStream);

        ResourceObject bundleFastCommon = given(aPropertiesBundle().with("value","fast"));
        HeapResourceObjectRepository pFast = new HeapResourceObjectRepository(clock);
        pFast.put(objectName, ctx, bundleFastCommon::asStream);

        ExpensiveResourceObjectProvider mSlow = managed("slow", pSlow);
        mSlow.whenRequested(bundleName, ctx).sleep(1000);
        ExpensiveResourceObjectProvider mFast = managed("fast", pFast);

        RefreshableResources resources = new RefreshableResources();
        resources.setObjectProviders(Arrays.asList(mSlow, mFast));

        OptionalString value = resources.get(key, ctx);
        assertEquals("slow-ctx", value.asIs());
    }

    protected static ExpensiveResourceObjectProvider managed(String name, HeapResourceObjectRepository repository) {
        return new ExpensiveResourceObjectProvider(name, repository);
    }

}
