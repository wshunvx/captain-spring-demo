package com.gen.demo.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import com.netflix.eureka.webflux.exception.BlockExceptionHandler;

@Configuration
public class GlobalExceptionHandler {

    @Bean
    @Order(-1)
    public BlockExceptionHandler otherBlockExceptionHandler(ObjectProvider<List<ViewResolver>> viewResolversProvider,
            ServerCodecConfigurer serverCodecConfigurer) {
    	BlockExceptionHandler exceptionHandler = new BlockExceptionHandler(
    			viewResolversProvider.getIfAvailable(Collections::emptyList), 
    			serverCodecConfigurer);
    	exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
    	return exceptionHandler;
    }

}
