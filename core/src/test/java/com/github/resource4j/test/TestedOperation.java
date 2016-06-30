package com.github.resource4j.test;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.function.Predicate;

public class TestedOperation<P,R> {

    private static final Runnable NO_OP = () -> {};

    public static <MP,MR> TestedOperation<MP,MR> test(Function<MP,MR> operation) {
        return new TestedOperation<>(operation);
    }

    public static <T> ConditionalConsumer<T> sleep(Predicate<T> predicate, long millis) {
        return new ConditionalConsumer<>(predicate, () -> doWait(millis));
    }

    public static <T> ConditionalConsumer<T> await(Predicate<T> predicate, CountDownLatch latch) {
        return new ConditionalConsumer<>(predicate, () -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                // ignore
            }
        });
    }

    public static <T> ConditionalConsumer<T> notify(Predicate<T> predicate, CountDownLatch latch) {
        return new ConditionalConsumer<>(predicate, latch::countDown);
    }

    private static void doWait(long millis) {
        Clock clock = Clock.systemUTC();
        long delta = millis;
        long time = clock.millis() + delta;
        while (delta > 0) {
            try {
                Thread.sleep(delta);
            } catch (InterruptedException e) {
                break;
            }
            delta = time - clock.millis();
        }
    }

    private Function<P,R> operation;

    private List<ConditionalConsumer<P>> preprocessors = new ArrayList<>();

    private List<ConditionalConsumer<R>> postprocessors = new ArrayList<>();

    public TestedOperation<P,R> before(ConditionalConsumer<P> preprocessor) {
        preprocessors.add(preprocessor);
        return this;
    }

    public TestedOperation<P,R> after(ConditionalConsumer<R> postprocessor) {
        postprocessors.add(postprocessor);
        return this;
    }

    public TestedOperation(Function<P, R> operation) {
        this.operation = operation;
    }

    public R execute(P param) {
        for (ConditionalConsumer<P> preprocessor : preprocessors) {
            if (preprocessor.accept(param)) {
                break;
            }
        }
        R returnValue = operation.apply(param);
        for (ConditionalConsumer<R> postprocessor : postprocessors) {
            if (postprocessor.accept(returnValue)) {
                break;
            }
        }
        return returnValue;
    }

    public static class ConditionalConsumer<P> {
        private Predicate<P> predicate;
        private Runnable run;

        public ConditionalConsumer(Predicate<P> predicate, Runnable run) {
            this.predicate = predicate;
            this.run = run;
        }

        public boolean accept(P param) {
            boolean accepted = predicate.test(param);
            if (accepted) {
                run.run();
            }
            return accepted;
        }

    }


}
