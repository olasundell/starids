package se.atrosys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * TODO write documentation
 */
@SpringBootApplication
@EnableCaching
@EnableJpaRepositories
//@EnableEurekaClient
public class HazelcastClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(HazelcastClientApplication.class, args);
	}
}
