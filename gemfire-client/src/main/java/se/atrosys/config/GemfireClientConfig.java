package se.atrosys.config;

import com.gemstone.gemfire.cache.GemFireCache;
import com.gemstone.gemfire.cache.RegionAttributes;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.client.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.client.PoolFactoryBean;
import org.springframework.data.gemfire.config.xml.GemfireConstants;
import org.springframework.data.gemfire.support.ConnectionEndpoint;
import org.springframework.data.gemfire.support.GemfireCacheManager;
import se.atrosys.model.Model;
import se.atrosys.repository.ModelRepository;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * TODO write documentation
 */
@Configuration
//@DependsOn("modelRepository")
public class GemfireClientConfig {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private ModelRepository modelRepository;

	@Autowired
	public void setModelRepository(ModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	private int intValue(Number value) {
		return value.intValue();
	}

	@PostConstruct
	public void createModels() {
		Model model = modelRepository.save(Model.builder().id(1).value("1").build());
		logger.info("Model saved, {}", model);
	}

	@Bean
	static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	Properties gemfireProperties(@Value("${gemfire.log.level:fine}") String logLevel) {
		Properties gemfireProperties = new Properties();

		gemfireProperties.setProperty("log-level", logLevel);

		return gemfireProperties;
	}

	@Bean
	@DependsOn({"yodelRegion", "modelRegion"})
	GemfireCacheManager cacheManager(GemFireCache gemfireCache) {
		logger.info("Creating spring cache manager");
		final GemfireCacheManager gemfireCacheManager = new GemfireCacheManager();
		gemfireCacheManager.setCache(gemfireCache);
		gemfireCacheManager.setCacheNames(new HashSet<String>() {{
			add("yodel");
			add("model");
		}});
		return gemfireCacheManager;
	}

	@Bean
	ClientCacheFactoryBean gemfireCache(@Qualifier("gemfireProperties") Properties gemfireProperties) {
		ClientCacheFactoryBean gemfireCache = new ClientCacheFactoryBean();

		gemfireCache.setClose(true);
		gemfireCache.setProperties(gemfireProperties);

		return gemfireCache;
	}

	@Bean(name = GemfireConstants.DEFAULT_GEMFIRE_POOL_NAME)
	PoolFactoryBean gemfirePool(
			@Value("${gemfire.cache.server.host:localhost}") String host,
			@Value("${gemfire.cache.server.port:40404}") int port) {

		PoolFactoryBean gemfirePool = new PoolFactoryBean();

		gemfirePool.setName(GemfireConstants.DEFAULT_GEMFIRE_POOL_NAME);
		gemfirePool.setFreeConnectionTimeout(intValue(TimeUnit.SECONDS.toMillis(5)));
		gemfirePool.setKeepAlive(false);
		gemfirePool.setPingInterval(TimeUnit.SECONDS.toMillis(5));
		gemfirePool.setReadTimeout(intValue(TimeUnit.SECONDS.toMillis(5)));
		gemfirePool.setRetryAttempts(1);
		gemfirePool.setSubscriptionEnabled(true);
		gemfirePool.setThreadLocalConnections(false);

		gemfirePool.setServers(Collections.singletonList(new ConnectionEndpoint(host, port)));

		return gemfirePool;
	}

	@Bean
	@DependsOn("gemfireCache")
	public ClientRegionFactoryBean<Integer, Model> yodelRegion(ClientCache gemfireCache, Pool gemfirePool,
	                                                           RegionAttributes<Integer, Model> regionAttributes
	                                                           ) {
//	public ClientRegionFactoryBean<Integer, Model> yodelRegion(ClientCache gemfireCache) {
		ClientRegionFactoryBean<Integer, Model> region = new ClientRegionFactoryBean<>();

		region.setCache(gemfireCache);
		region.setName("yodel");
		region.setPool(gemfirePool);
		region.setShortcut(ClientRegionShortcut.PROXY);
		region.setAttributes(regionAttributes);

		return region;
	}

	@Bean
	public RegionAttributesFactoryBean regionAttributesFactoryBean() {
		final RegionAttributesFactoryBean regionAttributesFactoryBean = new RegionAttributesFactoryBean();

		regionAttributesFactoryBean.setConcurrencyChecksEnabled(true);
		regionAttributesFactoryBean.setConcurrencyLevel(32);

		return regionAttributesFactoryBean;
	}

	@Bean
	@DependsOn("gemfireCache")
	public ClientRegionFactoryBean<Integer, Model> modelRegion(ClientCache gemfireCache, Pool gemfirePool,
	                                                           RegionAttributes<Integer, Model> regionAttributes
	                                                           ) {
//	public ClientRegionFactoryBean<Integer, Model> modelRegion(ClientCache gemfireCache) {
		ClientRegionFactoryBean<Integer, Model> region = new ClientRegionFactoryBean<>();

		region.setCache(gemfireCache);
		region.setName("model");
		region.setPool(gemfirePool);
		region.setAttributes(regionAttributes);
		region.setShortcut(ClientRegionShortcut.PROXY);

		return region;
	}
}
