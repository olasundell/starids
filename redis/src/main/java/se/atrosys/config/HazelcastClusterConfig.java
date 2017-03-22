package se.atrosys.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.ListenerConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static freemarker.template.utility.Collections12.singletonList;

/**
 * TODO write documentation
 */
//@Configuration
public class HazelcastClusterConfig {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
//	@Bean
	public Config hazelcastConfig() {
//		return new Config("hcast"); // Set up any non-default config here
		logger.info("Creating hazelcast config bean");

		final Config config = new Config("hcast")
//				.addListenerConfig(new ListenerConfig())
//				.addMapConfig(
//						new MapConfig()
//								.setName("string")
//								.setEvictionPolicy(EvictionPolicy.LRU)
//								.setTimeToLiveSeconds(2400));
		;
		JoinConfig joinConfig = config.getNetworkConfig().getJoin();

		joinConfig.getMulticastConfig().setEnabled(false);
		joinConfig.getTcpIpConfig().setEnabled(true).setMembers(singletonList("127.0.0.1"));

		return config
//				.setProperty("hazelcast.logging.type","slf4j")
		;
	}
}
