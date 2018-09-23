package com.github.resource4j.objects.providers;

import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.providers.mutable.FileResourceObjectRepository;
import com.github.resource4j.objects.providers.mutable.ResourceObjectRepository;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.time.Clock;

public class FileResourceObjectRepositoryTest extends AbstractResourceObjectRepositoryTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

    @Override
    protected ResourceObjectRepository createRepository(Clock clock) {
        return new FileResourceObjectRepository(folder.getRoot(), clock);
    }

    @Test(expected=MissingResourceObjectException.class)
	public void testMissingResourceFileThrownOnDirectory() throws Exception {
		String testFile = "test.dir";
		folder.newFolder(testFile);
		repository().get(testFile, ResourceResolutionContext.withoutContext());
	}

}
