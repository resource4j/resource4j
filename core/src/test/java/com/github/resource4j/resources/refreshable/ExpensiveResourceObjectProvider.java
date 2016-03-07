package com.github.resource4j.resources.refreshable;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;
import com.github.resource4j.objects.providers.mutable.HeapResourceObjectRepository;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.test.TestedOperation;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class ExpensiveResourceObjectProvider implements ResourceObjectProvider {

    private final String name;

    private HeapResourceObjectRepository repository;

    private TestedOperation<GetParams, ResourceObject> getMethodTest = new TestedOperation<>(
            params -> repository.get(params.name, params.ctx));

    public String toString() {
        return name + ":" + this.repository.toString();
    }

    public ExpensiveResourceObjectProvider(String name, HeapResourceObjectRepository repository) {
        this.name = name;
        this.repository = repository;
    }

    public PreprocessorDSL whenRequested(String name, ResourceResolutionContext ctx, byte[] data) {
        repository.put(name, ctx, data);
        return new PreprocessorDSL(name, ctx);
    }

    public PreprocessorDSL whenRequested(String name, ResourceResolutionContext ctx) {
        return new PreprocessorDSL(name, ctx);
    }

    @Override
    public ResourceObject get(String name, ResourceResolutionContext ctx) throws ResourceObjectAccessException {
        return getMethodTest.execute(new GetParams(name, ctx));
    }

    /**
     * Structure for passing params to test operations
     */
    private static class GetParams {
        public String name;
        public ResourceResolutionContext ctx;
        public GetParams(String name, ResourceResolutionContext ctx) {
            this.name = name;
            this.ctx = ctx;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GetParams getParams = (GetParams) o;
            return Objects.equals(name, getParams.name) &&
                    Objects.equals(ctx, getParams.ctx);
        }
        @Override
        public int hashCode() {
            return Objects.hash(name, ctx);
        }
    }

    public class PreprocessorDSL {

        private GetParams params;

        public PreprocessorDSL(String name, ResourceResolutionContext ctx) {
            this.params = new GetParams(name, ctx);
        }

        public void sleep(long millis) {
            getMethodTest.before(TestedOperation.sleep(params -> params.equals(this.params), millis));
        }

        public void await(CountDownLatch latch) {
            getMethodTest.before(TestedOperation.await(params -> params.equals(this.params), latch));
        }

    }

}
