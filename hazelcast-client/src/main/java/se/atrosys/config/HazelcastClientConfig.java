package se.atrosys.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientSecurityConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.security.UsernamePasswordCredentials;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

import java.util.Collection;

/**
 * TODO write documentation
 */
@Configuration
public class HazelcastClientConfig {
	// client
	@Bean
	public HazelcastInstance hazelcastInstance() {
		ClientConfig clientConfig = new ClientConfig();
//		clientConfig.setCredentials(new UsernamePasswordCredentials("principal2", "foobar2"));
		clientConfig.getGroupConfig().setName("hcast").setPassword("foobar");
		clientConfig.getNetworkConfig().addAddress("localhost");
		clientConfig.setSecurityConfig(new ClientSecurityConfig().setCredentials(new UsernamePasswordCredentials("hcast", "foobar")));
//		clientConfig.setSecurityConfig(null);

		final HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
		hazelcastInstance.getCluster();
		return hazelcastInstance;

//		Collection<HazelcastInstance> instances = HazelcastClient.getAllHazelcastClients();
//
//		final HazelcastInstance hcast = HazelcastClient.getHazelcastClientByName("hcast");
//		ICache<Object, Object> cache = hcast.getCacheManager().getCache("yodel");
//		return hcast;    // (2)
	}
	@Bean
	public CacheManager cacheManager() {
		return new HazelcastCacheManager(hazelcastInstance());
	}
}
