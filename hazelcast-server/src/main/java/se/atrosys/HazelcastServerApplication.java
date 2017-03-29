package se.atrosys;

/**
 * TODO write documentation
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
//@EnableEurekaClient
@EnableMongoRepositories(basePackages = "se.atrosys.repository.mongo")
@EnableCassandraRepositories(basePackages = "se.atrosys.repository.cassandra")
public class HazelcastServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(HazelcastServerApplication.class, args);
	}
}
