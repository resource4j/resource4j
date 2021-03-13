package com.github.resource4j.objects.providers;

import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.providers.mutable.FileResourceObjectRepository;
import com.github.resource4j.objects.providers.mutable.ResourceObjectRepository;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Clock;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileResourceObjectRepositoryTest extends AbstractResourceObjectRepositoryTest {

    @Override
    protected ResourceObjectRepository createRepository(Clock clock, Path folder) {
        return new FileResourceObjectRepository(folder.toFile(), clock);
    }

    @Test
	public void testMissingResourceFileThrownOnDirectory(@TempDir Path folder) throws Exception {
    	assertThrows(MissingResourceObjectException.class, () -> {
			String testFile = "/test.dir";
			folder.resolve(testFile).toFile().mkdir();
			repository().get(testFile, ResourceResolutionContext.withoutContext());
		});
	}

}
