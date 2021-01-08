package com.spring.demo.spring.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.netflix.eureka.http.NettyHttpServer;
import com.netflix.eureka.webmvc.WebmvcServer;

@WebmvcServer
@NettyHttpServer
@SpringBootApplication
public class WebmvcDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebmvcDemoApplication.class, args);
    }
}
