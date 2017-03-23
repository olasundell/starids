package se.atrosys.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * TODO write documentation
 */
@Configuration
@Profile("hazelcast")
public class HazelcastClusterConfig {
	private final Environment environment;
	private final DiscoveryClient discoveryClient;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public HazelcastClusterConfig(Environment environment,
	                              DiscoveryClient discoveryClient) {
		this.environment = environment;
		this.discoveryClient = discoveryClient;
	}

	@Bean
//	@Bean(name = "cluster")
//	public Config hazelcastConfig() {
	public HazelcastInstance hazelcastInstance() {
//		return new Config("hcast"); // Set up any non-default config here
		logger.info("Creating hazelcast config bean");

		final Config config = new Config("hcast")
//				.addListenerConfig(new ListenerConfig())
		;
		config.getMapConfigs().put("yodel",
				new MapConfig()
						.setName("yodel")
						.setEvictionPolicy(EvictionPolicy.LRU)
						.setTimeToLiveSeconds(2_400));
		JoinConfig joinConfig = config.getNetworkConfig().getJoin();

		joinConfig.getMulticastConfig().setEnabled(false);
//		joinConfig.getTcpIpConfig().setEnabled(true).setMembers(Collections.singletonList(environment.getProperty("spring.cloud.client.ipAddress", "127.0.0.1")));
//		final String dockercloudServiceHostname = environment.getProperty("DOCKERCLOUD_SERVICE_HOSTNAME");
//		logger.info("Setting hazelcast cluster members to {}", u);
//		joinConfig.getTcpIpConfig().setEnabled(true).setMembers(discoveryClient.getInstances("HCAST").stream().map(ServiceInstance::getHost).collect(Collectors.toList()));
		joinConfig.getTcpIpConfig().setEnabled(true).setMembers(Collections.singletonList("172.17.0.1-5"));


		config.setProperty("hazelcast.logging.type","slf4j");

		return Hazelcast.getOrCreateHazelcastInstance(config);
	}
}
