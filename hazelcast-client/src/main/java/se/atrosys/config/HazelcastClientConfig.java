package se.atrosys.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientSecurityConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.security.UsernamePasswordCredentials;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * TODO write documentation
 */
@Configuration
public class HazelcastClientConfig {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// client
	@Bean
	public HazelcastInstance hazelcastInstance(List<String> clusterAddress) {
		ClientConfig clientConfig = new ClientConfig();
//		clientConfig.setCredentials(new UsernamePasswordCredentials("principal2", "foobar2"));
		clientConfig.getGroupConfig().setName("hcast").setPassword("foobar");
		clusterAddress.forEach(address -> clientConfig.getNetworkConfig().addAddress(address));
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

	@Bean(name = "clusterAddress")
	@Profile("!docker")
	public List<String> clusterAddressLocal() {
		logger.warn("Using local cluster address");
		return Collections.singletonList("localhost");
	}

	@Bean(name = "clusterAddress")
	@Profile("docker")
	public List<String> clusterAddressDocker() {
		logger.warn("Using docker cluster address");
		return Collections.singletonList("hcastserver");
//		return Arrays.asList("172.18.0.1", "172.18.0.2", "172.18.0.3", "172.18.0.4", "172.18.0.5");
	}

	@Bean
	public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
		return new HazelcastCacheManager(hazelcastInstance);
	}
}
