package com.example.gatewayservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class GatewayServiceConfig {

    @Bean
    public RouteLocator getWayRoute(RouteLocatorBuilder builder, CircuitBreakerRegistry circuitBreakerRegistry) {
        // Custom circuit breaker configuration
        CircuitBreakerConfig customCircuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // 50% failure rate triggers the breaker
                .slidingWindowSize(10)    // 10 requests in the sliding window
                .minimumNumberOfCalls(5)  // Minimum 5 calls before evaluating failure rate
                .waitDurationInOpenState(Duration.ofSeconds(10)) // 10-second wait in open state
                .permittedNumberOfCallsInHalfOpenState(3) // Allow 3 calls in half-open state
                .build();

        // Register the custom circuit breaker config
        circuitBreakerRegistry.circuitBreaker("productServiceCircuitBreaker", customCircuitBreakerConfig);

        return builder.routes()
                .route("productservice", r -> r.path("/api/v1/products/**")
                        .filters(f -> f
                                .addRequestHeader("X-Request-Header", "Product-Service Header")
                                .addResponseHeader("X-Response-Header", "Product-Service Header")
                                .circuitBreaker(config -> config
                                        .setName("productServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/product-service"))) // Set fallback URI
                        .uri("lb://product-service")) // Load-balanced call to product-service
                .build();
    }
}
