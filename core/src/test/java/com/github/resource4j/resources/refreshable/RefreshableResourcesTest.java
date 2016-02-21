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

import static com.github.resource4j.resources.discovery.PropertyBundleBuilder.aPropertiesBundle;
import static com.github.resource4j.test.Builders.given;
import static org.junit.Assert.assertEquals;

public class RefreshableResourcesTest {

    @Test
    public void test1() throws Exception {

        ResourceKey key = ResourceKey.key("folder.bundle", "value");

        Clock clock = Clock.systemUTC();


        ResourceObject bundleSlowCtx = given(aPropertiesBundle().with("value","slow-ctx"));
        ResourceObject bundleSlowCommon = given(aPropertiesBundle().with("value","slow"));

        HeapResourceObjectRepository pSlow = new HeapResourceObjectRepository(clock);
        pSlow.put("folder.bundle", "/folder/bundle-ctx.properties", bundleSlowCtx::asStream);
        pSlow.put("folder.bundle", "/folder/bundle.properties", bundleSlowCommon::asStream);

        ResourceObject bundleFastCommon = given(aPropertiesBundle().with("value","fast"));
        HeapResourceObjectRepository pFast = new HeapResourceObjectRepository(clock);
        pFast.put("folder.bundle", "/folder/bundle-ctx.properties", bundleFastCommon::asStream);

        ExpensiveResourceObjectProvider mSlow = managed(pSlow);
        ExpensiveResourceObjectProvider mFast = managed(pFast);

        RefreshableResources resources = new RefreshableResources();
        resources.setObjectProviders(Arrays.asList(mSlow, mFast));

        ResourceResolutionContext ctx = ResourceResolutionContext.in("ctx");

        OptionalString value = resources.get(key, ctx);
        assertEquals("slow-ctx", value.asIs());
    }

    protected static ExpensiveResourceObjectProvider managed(HeapResourceObjectRepository repository) {
        return new ExpensiveResourceObjectProvider(repository);
    }


}
