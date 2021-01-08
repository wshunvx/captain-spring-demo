package com.spring.demo.spring.webflux.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Service
public class FooService {

    private final ExecutorService pool = Executors.newFixedThreadPool(8);
    
    private final Scheduler scheduler = Schedulers.fromExecutor(pool);

    public Mono<String> emitSingle() {
        return Mono.just(ThreadLocalRandom.current().nextInt(0, 2000))
            .map(e -> e + "d");
    }

    public Flux<Integer> emitMultiple() {
        int start = ThreadLocalRandom.current().nextInt(0, 6000);
        return Flux.range(start, 10);
    }

    public Mono<String> doSomethingSlow() {
        return Mono.fromCallable(() -> {
            Thread.sleep(2000);
            System.out.println("doSomethingSlow: " + Thread.currentThread().getName());
            return "ok";
        }).publishOn(scheduler);
    }
    
    public Mono<Map<String, String>> randomExc() {
		long os = System.currentTimeMillis();
		/* simulate performing network call to retrieve order */
        try {
            Thread.sleep((int) (Math.random() * 200) + 25);
        } catch (InterruptedException e) {
            // do nothing
        }

        /* fail rarely ... but allow failure as this one has no fallback */
        if (Math.random() > 0.5) {
            throw new RuntimeException("random failure loading order over network");
        }

        /* latency spike 5% of the time */
        if (Math.random() > 0.9) {
            // random latency spike
            try {
                Thread.sleep((int) (Math.random() * 300) + 50);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
        long es = System.currentTimeMillis();
        Map<String, String> fallback = new HashMap<>();
        fallback.put("hello", "world, ts=" + (es - os));
        /* success ... create Order with data "from" the remote service response */
        return Mono.just(fallback);
	}
}
