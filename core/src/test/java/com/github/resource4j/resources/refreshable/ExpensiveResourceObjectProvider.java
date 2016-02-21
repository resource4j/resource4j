package com.github.resource4j.resources.refreshable;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;
import com.github.resource4j.objects.providers.HeapResourceObjectRepository;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.test.TestedOperation;

import java.util.concurrent.CountDownLatch;

public class ExpensiveResourceObjectProvider implements ResourceObjectProvider {

    private HeapResourceObjectRepository repository;

    private TestedOperation<GetParams, ResourceObject> getMethodTest = new TestedOperation<>(
            params -> repository.get(params.name, params.resolvedName));

    public ExpensiveResourceObjectProvider(HeapResourceObjectRepository repository) {
        this.repository = repository;
    }

    public PreprocessorDSL whenRequested(String name, String resolvedName, byte[] data) {
        repository.put(name, resolvedName, data);
        return new PreprocessorDSL(resolvedName);
    }

    @Override
    public ResourceObject get(String name, String resolvedName) throws ResourceObjectAccessException {
        return getMethodTest.execute(new GetParams(name, resolvedName));
    }

    /**
     * Structure for passing params to test operations
     */
    private static class GetParams {
        public String name;
        public String resolvedName;

        public GetParams(String name, String resolvedName) {
            this.name = name;
            this.resolvedName = resolvedName;
        }
    }

    public class PreprocessorDSL {

        private String resolvedName;

        public PreprocessorDSL(String resolvedName) {
            this.resolvedName = resolvedName;
        }

        public Runnable sleep(long millis) {
            return () ->
                    getMethodTest.before(TestedOperation.sleep(params -> params.resolvedName.equals(resolvedName), millis));
        }

        public Runnable await(CountDownLatch latch) {
            return () ->
                    getMethodTest.before(TestedOperation.await(params -> params.resolvedName.equals(resolvedName), latch));
        }

    }

}
