package com.github.resource4j.objects.providers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.util.function.Supplier;

import com.github.resource4j.ResourceObjectException;
import com.github.resource4j.objects.FileResourceObject;
import com.github.resource4j.objects.exceptions.InaccessibleResourceObjectException;
import com.github.resource4j.objects.exceptions.InvalidResourceObjectException;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.exceptions.ResourceObjectRepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileResourceObjectRepository implements ResourceObjectRepository {

	private static final Logger LOG = LoggerFactory.getLogger(FileResourceObjectRepository.class);
	
	private File base;

    private Clock clock;

	public FileResourceObjectRepository(String basePath, Clock clock) {
		this(new File(basePath), clock);
	}
	
	public FileResourceObjectRepository(File base, Clock clock) {
		if (!base.exists()) {
			throw new IllegalArgumentException("Resource path does not exist: " + base.getAbsolutePath());
		}
		if (!base.isDirectory()) {
			throw new IllegalArgumentException("Resource path is not directory: " + base.getAbsolutePath());
		}
		this.base = base;
        this.clock = clock;
		LOG.debug("Resource path configured: " + base.getAbsolutePath());
	}

	@Override
	public FileResourceObject get(String name, String actualName) throws MissingResourceObjectException {
		File file = new File(base, actualName);
		LOG.trace("Requested file: {}", file.getAbsolutePath());
		if (!isResourceFile(file)) {
			throw new MissingResourceObjectException(name, actualName);
		}
		return new FileResourceObject(name, file);
	}

	@Override
	public boolean contains(String resolvedName) {
		File file = new File(base, resolvedName);
		return isResourceFile(file);
	}

    protected static boolean isResourceFile(File file) {
        return file.exists() && !file.isDirectory() && file.canRead();
    }

    @Override
	public void put(String name, String resolvedName, byte[] data) throws ResourceObjectException {
        File file = new File(base, resolvedName);
        if (file.isDirectory()) {
            throw new InvalidResourceObjectException("Directory with given name already exists", name, resolvedName);
        }
        try {
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (!created) {
                    throw new ResourceObjectRepositoryException("File was not created", name, resolvedName);
                }
            }
            if (!file.canWrite()) {
                throw new InaccessibleResourceObjectException("Write access denied to file", name, resolvedName);
            }
            try (FileOutputStream ostream = new FileOutputStream(file)) {
                ostream.write(data);
                ostream.flush();
                if (!file.setLastModified(clock.millis())) {
                    LOG.warn("Failed to set Last Modified time for file {}", file.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            throw new ResourceObjectRepositoryException("Could not write data to file", e, name, resolvedName);
        }
	}

	@Override
	public void remove(String resolvedName) {
        File file = new File(base, resolvedName);
        if (isResourceFile(file)) {
            boolean isDeleted = file.delete();
            if (!isDeleted) {
                throw new ResourceObjectRepositoryException("File was not deleted", "<n/a>", resolvedName);
            }
        }
	}
}
