package com.gen.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.netflix.eureka.http.NettyHttpServer;
import com.netflix.eureka.webflux.WebfluxServer;

@WebfluxServer
@NettyHttpServer
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
