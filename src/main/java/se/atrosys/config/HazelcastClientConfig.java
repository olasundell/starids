package se.atrosys.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO write documentation
 */
@Configuration
public class HazelcastClientConfig {
	// client
//	@Bean
//	HazelcastInstance hazelcastInstance() {
//		return HazelcastClient.getHazelcastClientByName("hcast");    // (2)
//	}
//	@Bean
//	public CacheManager cacheManager() {
//		return new HazelcastCacheManager(hazelcastInstance());
//	}
}
