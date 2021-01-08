package com.gen.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.netflix.eureka.http.NettyHttpServer;
import com.netflix.eureka.webmvc.WebmvcServer;

@WebmvcServer
@NettyHttpServer
@SpringBootApplication
public class HystrixApplication {

	public static void main(String[] args) {
		SpringApplication.run(HystrixApplication.class, args);
	}

}
