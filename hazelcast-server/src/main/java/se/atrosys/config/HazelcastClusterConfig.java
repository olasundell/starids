package se.atrosys.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import se.atrosys.store.StarMapStore;

import java.util.Collections;
import java.util.List;

/**
 * TODO write documentation
 */
@Configuration
public class HazelcastClusterConfig {
	private final Environment environment;
//	private final DiscoveryClient discoveryClient;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public HazelcastClusterConfig(Environment environment) {
//	public HazelcastClusterConfig(Environment environment,
//	                              DiscoveryClient discoveryClient) {
		this.environment = environment;
//		this.discoveryClient = discoveryClient;
	}

	@Bean
//	@Bean(name = "cluster")
//	public Config hazelcastConfig() {
	public HazelcastInstance hazelcastInstance(List<String> members,
	                                           MapConfig starMap) {
//		return new Config("hcast"); // Set up any non-default config here
		logger.info("Creating hazelcast config bean");

		final Config config = new Config("hcast")
//				.addListenerConfig(new ListenerConfig())
		;
		config.getGroupConfig().setName("hcast").setPassword("foobar");
//		config.getManagementCenterConfig().setUrl("http://localhost:8099/mancenter").setEnabled(true);
//		config.setSecurityConfig(new SecurityConfig().setEnabled(false));
//		config.setSecurityConfig(new SecurityConfig().)
		config.getMapConfigs().put("stars", starMap);
		config.getMapConfigs().put("starids",
				new MapConfig()
						.setName("starids")
						.setInMemoryFormat(InMemoryFormat.OBJECT)
						.setEvictionPolicy(EvictionPolicy.LRU)
						.setTimeToLiveSeconds(2_400));
		config.getMapConfigs().put("yodel",
				new MapConfig()
						.setName("yodel")
						.setEvictionPolicy(EvictionPolicy.LRU)
						.setTimeToLiveSeconds(2_400));
		JoinConfig joinConfig = config.getNetworkConfig().getJoin();

		joinConfig.getMulticastConfig().setEnabled(false);
		joinConfig.getTcpIpConfig()
				.setEnabled(true)
				.setMembers(members);
//		final String dockercloudServiceHostname = environment.getProperty("DOCKERCLOUD_SERVICE_HOSTNAME");
//		logger.info("Setting hazelcast cluster members to {}", u);
//		joinConfig.getTcpIpConfig().setEnabled(true).setMembers(discoveryClient.getInstances("HCAST").stream().map(ServiceInstance::getHost).collect(Collectors.toList()));
//		joinConfig.getTcpIpConfig().setEnabled(true).setMembers(Collections.singletonList("172.17.0.1-5"));


		config.setProperty("hazelcast.logging.type","slf4j");

		return Hazelcast.getOrCreateHazelcastInstance(config);
	}

	@Bean
	MapConfig starMap(StarMapStore starMapStore){
		final MapConfig stars = new MapConfig()
				.setName("stars")
				.setInMemoryFormat(InMemoryFormat.OBJECT)
				.setMaxSizeConfig(new MaxSizeConfig(90, MaxSizeConfig.MaxSizePolicy.USED_HEAP_PERCENTAGE))
				.setEvictionPolicy(EvictionPolicy.LRU)
				.setTimeToLiveSeconds(2_400);

		stars.getMapStoreConfig()
				.setWriteDelaySeconds(1)
				.setImplementation(starMapStore)
				.setWriteBatchSize(10)
				.setEnabled(true);

		return stars;
	}

	@Bean(name = "members")
	@Profile("docker")
	@Primary
	List<String> membersDocker() {
//		return Collections.singletonList("172.18.0.1-5");
		return Collections.singletonList("hcastserver");
	}

	@Bean(name = "members")
	List<String> membersLocal() {
		return Collections.singletonList("127.0.0.1");
	}

}
