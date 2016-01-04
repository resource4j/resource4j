package com.github.resource4j.objects.providers;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.objects.parsers.ResourceParsers.binary;
import static com.github.resource4j.test.TestClock.testFixed;
import static java.time.Clock.systemUTC;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.test.TestClock;
import org.junit.Before;
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
		objects().get(testFile, testFile);
	}

}
