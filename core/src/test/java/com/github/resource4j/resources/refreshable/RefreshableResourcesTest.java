package com.github.resource4j.resources.refreshable;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.providers.HeapResourceObjectRepository;
import com.github.resource4j.refreshable.RefreshableResources;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.resources.discovery.PropertyBundleBuilder;
import org.junit.Test;

import java.time.Clock;
import java.util.Arrays;

import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;
import static com.github.resource4j.resources.discovery.PropertyBundleBuilder.aPropertiesBundle;
import static com.github.resource4j.test.Builders.given;
import static org.junit.Assert.assertEquals;

public class RefreshableResourcesTest {

    @Test
    public void test1() throws Exception {

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

        ExpensiveResourceObjectProvider mSlow = managed(pSlow);
        ExpensiveResourceObjectProvider mFast = managed(pFast);

        RefreshableResources resources = new RefreshableResources();
        resources.setObjectProviders(Arrays.asList(mSlow, mFast));

        OptionalString value = resources.get(key, ctx);
        assertEquals("slow-ctx", value.asIs());
    }

    protected static ExpensiveResourceObjectProvider managed(HeapResourceObjectRepository repository) {
        return new ExpensiveResourceObjectProvider(repository);
    }


}
