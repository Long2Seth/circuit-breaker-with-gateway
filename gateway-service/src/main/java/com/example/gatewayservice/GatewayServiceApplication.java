package com.example.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

	// Fallback route handler
	@Bean
	public Mono<ServerResponse> productServiceFallback() {
		return ServerResponse.ok()
				.contentType(MediaType.TEXT_PLAIN)
				.bodyValue("Product service is currently unavailable. Please try again later.");
	}

	// Alternatively, if using @RequestMapping
	@RestController
	public class FallbackController {

		@RequestMapping("/fallback/product-service")
		public Mono<String> productServiceFallback() {
			return Mono.just("Product service is currently unavailable. Please try again later.");
		}
	}
}
