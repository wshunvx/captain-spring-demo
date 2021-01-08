package com.spring.demo.spring.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.netflix.eureka.http.NettyHttpServer;
import com.netflix.eureka.webflux.WebfluxServer;

@WebfluxServer
@NettyHttpServer
@SpringBootApplication
public class WebFluxDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebFluxDemoApplication.class, args);
    }
}
