package com.gen.demo.rest;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gen.demo.service.HttpBinService;

@RestController
public class DemoController {

	Logger LOG = LoggerFactory.getLogger(DemoController.class);

	private HttpBinService httpBin;
	
	private CircuitBreakerFactory circuitBreakerFactory;

	@Inject
	public DemoController(CircuitBreakerFactory circuitBreakerFactory, HttpBinService httpBinService) {
		this.circuitBreakerFactory = circuitBreakerFactory;
		this.httpBin = httpBinService;
	}

	/**
	 * 原始方式
	 * @return
	 */
	@GetMapping("/get")
	public Map get() {
		Map<String, String> fallback = new HashMap<>();
		fallback.put("ts", "0");
		return fallback;
	}
	
	@GetMapping("/error")
	public Map error() {
		return httpBin.map();
	}
	
	@GetMapping("/timeout")
	public Map timeout() {
		try {
            Thread.sleep(2200);
        } catch (InterruptedException e) {
            // do nothing
        }
		Map<String, String> fallback = new HashMap<>();
		fallback.put("ts", "1");
		return fallback;
	}
	
	/**
	 * Circuit Breaker 方式
	 * @return
	 */
	@GetMapping("/delay")
	public Map delay() {
		return circuitBreakerFactory.create("delay").run(httpBin.delaySuppplier(), t -> {
			Map<String, String> fallback = new HashMap<>();
			fallback.put("failed", "error");
			return fallback;
		});
	}
	
	@GetMapping("/breaker")
	public Map breaker() {
		return circuitBreakerFactory.create("timeout").run(httpBin.delaySuppplier(), t -> {
			Map<String, String> fallback = new HashMap<>();
			fallback.put("failed", "error2");
			return fallback;
		});
	}
}
