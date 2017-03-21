package se.atrosys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@SpringBootApplication
@EnableCircuitBreaker
@EnableCaching
@EnableGemfireRepositories
public class HystrixTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(HystrixTestApplication.class, args);
	}
}
