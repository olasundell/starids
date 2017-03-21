package se.atrosys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

/**
 * TODO write documentation
 */
@SpringBootApplication
@EnableGemfireRepositories
@EnableCaching
public class GemfireClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(GemfireClientApplication.class);
	}
}
