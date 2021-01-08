package com.gen.demo.rest;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.hystrix.ReactiveHystrixCircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gen.demo.service.HttpBinService;

import reactor.core.publisher.Mono;

@RestController
public class DemoController {

	Logger LOG = LoggerFactory.getLogger(DemoController.class);

	private HttpBinService httpBin;
	
	private ReactiveHystrixCircuitBreakerFactory circuitBreakerFactory;

	public DemoController(ReactiveHystrixCircuitBreakerFactory circuitBreakerFactory, HttpBinService httpBinService) {
		this.circuitBreakerFactory = circuitBreakerFactory;
		this.httpBin = httpBinService;
	}

	/**
	 * 原始方式
	 * @return
	 */
	@GetMapping("/get")
	public Mono<Map> get() {
		return httpBin.map();
	}

	/**
	 * Circuit Breaker 方式
	 * @return
	 */
	@GetMapping("/delay")
	public Mono<Map> delay() {
		return circuitBreakerFactory.create("delay").run(httpBin.map(), t -> {
			Map<String, String> fallback = new HashMap<>();
			fallback.put("failed", "error");
			return Mono.just(fallback);
		});
	}
	
	@GetMapping("/breaker")
	public Mono<Map> breaker() {
		return circuitBreakerFactory.create("timeout").run(httpBin.map(), t -> {
			Map<String, String> fallback = new HashMap<>();
			fallback.put("failed", "error2");
			return Mono.just(fallback);
		});
	}
}
