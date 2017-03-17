package se.atrosys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
@EnableCaching
public class HystrixTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(HystrixTestApplication.class, args);
	}
}
