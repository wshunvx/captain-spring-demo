package com.spring.demo.spring.webflux.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.netflix.eureka.common.ParamFlowItem;
import com.netflix.eureka.common.ParamFlowRule;
import com.netflix.eureka.http.slots.block.ParamFlowRuleManager;
import com.netflix.eureka.webflux.reactor.ReactorTransformer;
import com.spring.demo.spring.webflux.service.FooService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/foo")
public class FooController {

    @Autowired
    private FooService fooService;

    public FooController() {
        ParamFlowRule rule = new ParamFlowRule("freqParam")
                .setParamIdx(0)
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
                .setCount(5);
        ParamFlowItem item = new ParamFlowItem().setObject("slow")
        	    .setClassType(String.class.getName())
        	    .setCount(10);
        rule.setParamFlowItemList(Collections.singletonList(item));
        ParamFlowRuleManager.loadRules(Collections.singletonList(rule));
        
        DegradeRule drule = new DegradeRule("/foo/random")
                .setGrade(CircuitBreakerStrategy.ERROR_RATIO.getType())
                .setCount(0.4)
                .setTimeWindow(5)
                .setMinRequestAmount(30)
                .setStatIntervalMs(10000);

        DegradeRuleManager.loadRules(Collections.singletonList(drule));
    }
    
    @GetMapping("/single")
    public Mono<String> apiNormalSingle() {
        return fooService.emitSingle()
            // transform the publisher here.
            .transform(new ReactorTransformer<>("demo_foo_normal_single"));
    }

    @GetMapping("/sleep")
    public Mono<String> apiSleepMono() {
        return fooService.doSomethingSlow()
            .transform(new ReactorTransformer<>("demo_foo_normal_flux"));
    }
    
    @GetMapping("/flux")
    public Flux<Integer> apiNormalFlux() {
        return fooService.emitMultiple()
            .transform(new ReactorTransformer<>("demo_foo_normal_flux"));
    }
    
    @GetMapping("/random")
    public Mono<Map<String, String>> apiExceptionMono() {
        return fooService.randomExc()
            .transform(new ReactorTransformer<>("demo_foo_normal_flux"));
    }
    
    @GetMapping("/slow")
    public Mono<String> apiDoSomethingSlow(@RequestParam(value = "name", defaultValue = "slow") String name) {
    	Entry entry = null;
    	try {
    		entry = SphU.entry("freqParam", EntryType.IN, 1, name);  
    	} catch (BlockException e) {
			return Mono.just(e.getMessage());
		}finally {
			if(entry != null) {
				entry.exit();
			}
		}
    	return fooService.doSomethingSlow();
    }
}
