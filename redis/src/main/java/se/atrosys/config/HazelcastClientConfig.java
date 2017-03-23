package se.atrosys.config;

import com.hazelcast.cache.ICache;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

/**
 * TODO write documentation
 */
@Configuration
@Profile("hazelcast")
public class HazelcastClientConfig {
	// client
	@Bean
	@DependsOn("hazelcastConfig")
	public HazelcastInstance hazelcastInstance() {
		final HazelcastInstance hcast = HazelcastClient.getHazelcastClientByName("hcast");
//		ICache<Object, Object> cache = hcast.getCacheManager().getCache("yodel");
		return hcast;    // (2)
	}
	@Bean
	public CacheManager cacheManager() {
		return new HazelcastCacheManager(hazelcastInstance());
	}
}
