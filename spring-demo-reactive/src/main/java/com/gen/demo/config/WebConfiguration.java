package com.gen.demo.config;

import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.netflix.hystrix.ReactiveHystrixCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;

/**
 * 来源于：netflix 
 * 版本 2.2.4.RELEASE
 * To provide a default configuration for all of your circuit breakers create a bean that is passed a or . 
 * The method can be used to provide a default configuration.
 * https://docs.spring.io/spring-cloud-netflix/docs/2.2.4.RELEASE/reference/html/#default-configuration
 * https://github.com/Netflix/Hystrix/wiki/Configuration
 * @author WX
 *
 */
@Configuration
public class WebConfiguration {

	/**
	 * 通用配置
	 * @return
	 */
	@Bean
	public Customizer<ReactiveHystrixCircuitBreakerFactory> defaultConfig() {
		return factory -> factory.configureDefault(id -> {
			return HystrixObservableCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(id))
					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
							.withExecutionTimeoutInMilliseconds(3000));

		});
	}
	
	/**
	 * 自定义配置
	 * @return
	 */
	@Bean
	public Customizer<ReactiveHystrixCircuitBreakerFactory> customizer() {
	    return factory -> factory.configure(builder -> builder.commandProperties(
	                    HystrixCommandProperties.Setter()
	                    //设置超时时间(以毫秒为单位), 在此时间之后, 调用方将观察超时并退出命令执行.
	                    .withExecutionTimeoutInMilliseconds(2000) //默认1000
	                    //在发生故障或拒绝时是否尝试调用
	                    .withFallbackEnabled(true)//默认True
	                    //是否启用超时设置
	                    .withExecutionTimeoutEnabled(true) //默认True
	                    //超时发生时是否应中断执行
	                    .withExecutionIsolationThreadInterruptOnTimeout(true) //默认True
	                    //当线程执行超时, 是否进行中断处理, 即异步的Future#cancel()
	                    .withExecutionIsolationThreadInterruptOnFutureCancel(true)//默认False
	                    //允许最大请求数, 如果达到此最大并发限制, 则后续请求将被拒绝.
	                    .withExecutionIsolationSemaphoreMaxConcurrentRequests(30)//默认10
	                    //允许调用线程发出的最大请求数, 如果达到最大并发限制, 则后续请求将被拒绝, 并引发异常.
	                    .withFallbackIsolationSemaphoreMaxConcurrentRequests(30)//默认10
	                    //采样请求数, 一个采样周期内必须进行至少多个请求才能进行采样统计
	                    .withCircuitBreakerRequestVolumeThreshold(20)//默认20
	                    //采样容错率(百分比), 失败率超过该配置, 则打开熔断开关
	                    .withCircuitBreakerErrorThresholdPercentage(50)//默认50
	                    //当断路器拒绝请求期间, 等待多久会再次执行请求
	                    .withCircuitBreakerSleepWindowInMilliseconds(5000)//默认5000
	                    //存储桶统计持续时间
	                    .withMetricsRollingStatisticalWindowInMilliseconds(10000)//默认10000
	                    //存储桶持续时间内分配桶的数量, 计算方式 10000(10second) / 10 = 1/second
	                    .withMetricsRollingStatisticalWindowBuckets(10)//默认10
	                    //存储桶保留执行时间, 用于计算响应统计
	                    .withMetricsRollingPercentileWindowInMilliseconds(60000)//默认60000
	                    //存储桶执行时间内分配桶的数量, 计算方式 60000(6second) / 6 = 1/second
	                    .withMetricsRollingPercentileWindowBuckets(6)//默认6
	                    //每个存储桶保留最大执行时间
	                    .withMetricsRollingPercentileBucketSize(100)//默认100
	                    //存储桶统计间隔
	                    .withMetricsHealthSnapshotIntervalInMilliseconds(500)//默认500
	                    ), "timeout");
	}
}
