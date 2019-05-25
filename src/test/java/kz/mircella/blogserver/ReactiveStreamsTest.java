package kz.mircella.blogserver;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


public class ReactiveStreamsTest {
    Logger logger = LoggerFactory.getLogger(ReactiveStreamsTest.class);

    @Test
    public void fluxFromValue() {
        Flux<String> moviesFlux = Flux.just("Harry Potter", "Lord of The Rings");
        Flux<Long> intervalFlux = Flux.interval(Duration.ofMillis(100)).take(10);
        Flux<String> myFlux = moviesFlux.zipWith(intervalFlux).map(new Function<Tuple2<String, Long>, String>() {
            @Override
            public String apply(Tuple2<String, Long> objects) {
                return objects.getT1();
            }
        });
        myFlux.subscribe(new Consumer<String>() {
            @Override
            public void accept(String string) {
                logger.info("Element "+ string);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                logger.info("Error "+throwable.getLocalizedMessage());
            }
        }, new Runnable() {
            @Override
            public void run() {
                logger.info("Completed");
            }
        }, new Consumer<Subscription>() {
            @Override
            public void accept(Subscription subscription) {
                logger.info("Subscription was requested");
            }
        });
    }

    @Test
    public void assertFlux() {
        Flux<User> flux = Flux.just(new User("swhite"), new User("jpinkman"));
        StepVerifier.create(flux).assertNext(new Consumer<User>() {
            @Override
            public void accept(User user) {
                Assertions.assertThat(user.getUsername()).isEqualTo("swhite");
            }
        }).assertNext(new Consumer<User>() {
            @Override
            public void accept(User user) {
                Assertions.assertThat(user.getUsername()).isEqualTo("jpinkman");
            }
        }).verifyComplete();
    }

    @Test
    public void assertLongFlux() {
        Flux<Long> flux = Flux.fromIterable(Arrays.asList(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L));
        StepVerifier.create(flux).expectNextCount(10).thenAwait(Duration.ofSeconds(10));
    }

    @Test
    public void assertFluxWithVirtualTime() {
        Flux<Long> flux = Flux.generate(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return 0L;
            }
        }, new BiFunction<Long, SynchronousSink<Long>, Long>() {
            @Override
            public Long apply(Long aLong, SynchronousSink<Long> longSynchronousSink) {
                if(aLong < 3600) {
                longSynchronousSink.next(aLong);
                return ++aLong;
                } else {
                    longSynchronousSink.complete();
                    return null;
                }
            }
        });
        flux.subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                logger.info("Element "+ aLong);
            }
        });
//        StepVerifier.withVirtualTime(new Supplier<Mono<Long>>() {
//            @Override
//            public Mono<Long> get() {
//                return Mono.delay(Duration.ofHours(3));
//            }
//        }).expectSubscription()
//                .thenAwait(Duration.ofMinutes(1))
//                .expectNextCount(3600)
//                .expectNoEvent(Duration.ofMinutes(179))
//                .verifyComplete();
    }

    static class User {
        public String getUsername() {
            return username;
        }

        String username;

        public User(String username){
            this.username = username;
        }
    }
}
