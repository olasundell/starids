package se.atrosys;

/**
 * TODO write documentation
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
//@EnableEurekaClient
@EnableCassandraRepositories(basePackages = "se.atrosys.repository.cassandra")
public class HazelcastServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(HazelcastServerApplication.class, args);
	}
}
