package com.github.resource4j.objects.providers.mutable;

import com.github.resource4j.ResourceObjectException;
import com.github.resource4j.objects.FileResourceObject;
import com.github.resource4j.objects.exceptions.InaccessibleResourceObjectException;
import com.github.resource4j.objects.exceptions.InvalidResourceObjectException;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.exceptions.ResourceObjectRepositoryException;
import com.github.resource4j.objects.providers.AbstractFileResourceObjectProvider;
import com.github.resource4j.objects.providers.events.ResourceObjectRepositoryEventDispatcher;
import com.github.resource4j.objects.providers.events.ResourceObjectRepositoryListener;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Clock;

import static com.github.resource4j.objects.providers.events.ResourceObjectRepositoryEvent.*;

public class FileResourceObjectRepository
        extends AbstractFileResourceObjectProvider<FileResourceObjectRepository>
        implements ResourceObjectRepository {

	private static final Logger LOG = LoggerFactory.getLogger(FileResourceObjectRepository.class);
	
	private File base;

    private Clock clock;

    private ResourceObjectRepositoryEventDispatcher dispatcher = new ResourceObjectRepositoryEventDispatcher(this);

    @Override
    public String toString() {
        return "file:" + this.base.getAbsolutePath();
    }

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
    protected FileResourceObjectRepository self() {
        return this;
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
	public boolean contains(String name, ResourceResolutionContext context) {
        String resolvedName = resolver().resolve(name, context);
		File file = new File(base, resolvedName);
		return isResourceFile(file);
	}

    protected static boolean isResourceFile(File file) {
        return file.exists() && !file.isDirectory() && file.canRead();
    }

    @Override
	public void put(String name, ResourceResolutionContext context, byte[] data) throws ResourceObjectException {
        String resolvedName = resolver().resolve(name, context);
        File file = new File(base, resolvedName);
        if (file.isDirectory()) {
            throw new InvalidResourceObjectException("Directory with given name already exists", name, resolvedName);
        }
        boolean created = false;
        try {
            if (!file.exists()) {
                created = file.createNewFile();
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
        if (created) {
            dispatcher.repositoryUpdated(created(toString(), name, context));
        } else {
            dispatcher.repositoryUpdated(modified(toString(), name, context));
        }
	}

	@Override
	public void remove(String name, ResourceResolutionContext context) {
        String resolvedName = resolver().resolve(name, context);
        File file = new File(base, resolvedName);
        if (isResourceFile(file)) {
            boolean isDeleted = file.delete();
            if (!isDeleted) {
                throw new ResourceObjectRepositoryException("File was not deleted", "<n/a>", resolvedName);
            }
        }
        dispatcher.repositoryUpdated(deleted(toString(), name, context));
	}

    @Override
    public void addListener(ResourceObjectRepositoryListener listener) {
        dispatcher.addListener(listener);
    }

    @Override
    public void removeListener(ResourceObjectRepositoryListener listener) {
        dispatcher.removeListener(listener);
    }
}
