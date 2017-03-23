package se.atrosys;

/**
 * TODO write documentation
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableEurekaClient
public class HazelcastServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(HazelcastServerApplication.class, args);
	}
}
