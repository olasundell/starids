package se.atrosys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * TODO write documentation
 */
@SpringBootApplication
@EnableCaching
public class EhCacheApplication {
	public static void main(String[] args) {
		SpringApplication.run(EhCacheApplication.class, args);
	}
}
