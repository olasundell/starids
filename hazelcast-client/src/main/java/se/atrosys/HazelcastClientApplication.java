package se.atrosys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * TODO write documentation
 */
@SpringBootApplication(
		exclude = {
				CassandraAutoConfiguration.class,
				CassandraDataAutoConfiguration.class,
				CassandraRepositoriesAutoConfiguration.class,
				MongoAutoConfiguration.class,
				MongoDataAutoConfiguration.class,
				MongoRepositoriesAutoConfiguration.class
		}
//		excludeName = {
//		"org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration",
//		"org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration",
//		}
 )
@EnableCaching
@EnableJpaRepositories(basePackages = "se.atrosys.repository.jpa")
//@EnableMongoRepositories(basePackages = "se.atrosys.repository.mongo")
//@EnableCassandraRepositories(basePackages = "se.atrosys.repository.cassandra")
//@EnableEurekaClient
public class HazelcastClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(HazelcastClientApplication.class, args);
	}
}
