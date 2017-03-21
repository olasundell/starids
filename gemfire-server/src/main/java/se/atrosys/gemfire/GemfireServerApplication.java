package se.atrosys.gemfire;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheLoader;
import com.gemstone.gemfire.cache.CacheLoaderException;
import com.gemstone.gemfire.cache.LoaderHelper;
import com.gemstone.gemfire.cache.RegionAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.LocalRegionFactoryBean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.server.CacheServerFactoryBean;
import se.atrosys.model.Model;

import java.util.Properties;

/**
 * TODO write documentation
 */
@SpringBootApplication
public class GemfireServerApplication {

	static final boolean DEFAULT_AUTO_STARTUP = true;

	public static void main(String[] args) {
		SpringApplication.run(GemfireServerApplication.class, args);
	}

	@Bean
	static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertyPlaceholderConfigurer();
	}

	String applicationName() {
		return GemfireServerApplication.class.getSimpleName();
	}

	@Bean
	Properties gemfireProperties(
			@Value("${gemfire.log.level:config}") String logLevel,
			@Value("${gemfire.locator.host-port:localhost[10334]}") String locatorHostPort,
			@Value("${gemfire.manager.port:1099}") String managerPort) {

		Properties gemfireProperties = new Properties();

		gemfireProperties.setProperty("name", applicationName());
		gemfireProperties.setProperty("mcast-port", "0");
		gemfireProperties.setProperty("log-level", logLevel);
		gemfireProperties.setProperty("start-locator", locatorHostPort);
		gemfireProperties.setProperty("jmx-manager", "true");
		gemfireProperties.setProperty("jmx-manager-port", managerPort);
		gemfireProperties.setProperty("jmx-manager-start", "true");

		return gemfireProperties;
	}

	@Bean
	CacheFactoryBean gemfireCache(@Qualifier("gemfireProperties") Properties gemfireProperties) {
		CacheFactoryBean gemfireCache = new CacheFactoryBean();

		gemfireCache.setClose(true);
		gemfireCache.setProperties(gemfireProperties);

		return gemfireCache;
	}

	@Bean
	CacheServerFactoryBean gemfireCacheServer(Cache gemfireCache,
	                                          @Value("${gemfire.cache.server.bind-address:localhost}") String bindAddress,
	                                          @Value("${gemfire.cache.server.hostname-for-clients:localhost}") String hostNameForClients,
	                                          @Value("${gemfire.cache.server.port:40404}") int port) {

		CacheServerFactoryBean gemfireCacheServer = new CacheServerFactoryBean();

		gemfireCacheServer.setCache(gemfireCache);
		gemfireCacheServer.setAutoStartup(DEFAULT_AUTO_STARTUP);
		gemfireCacheServer.setBindAddress(bindAddress);
		gemfireCacheServer.setHostNameForClients(hostNameForClients);
		gemfireCacheServer.setPort(port);

		return gemfireCacheServer;
	}
/*

	@Bean
	PartitionedRegionFactoryBean<Long, Long> factorialsRegion(Cache gemfireCache,
	                                                          @Qualifier("factorialsRegionAttributes") RegionAttributes<Long, Long> factorialsRegionAttributes) {

		PartitionedRegionFactoryBean<Long, Long> factorialsRegion = new PartitionedRegionFactoryBean<>();

		factorialsRegion.setAttributes(factorialsRegionAttributes);
		factorialsRegion.setCache(gemfireCache);
		factorialsRegion.setClose(false);
		factorialsRegion.setName("Factorials");
		factorialsRegion.setPersistent(false);

		return factorialsRegion;
	}

	@Bean
	@SuppressWarnings("unchecked")
	RegionAttributesFactoryBean factorialsRegionAttributes() {
		RegionAttributesFactoryBean factorialsRegionAttributes = new RegionAttributesFactoryBean();

		factorialsRegionAttributes.setCacheLoader(factorialsCacheLoader());
		factorialsRegionAttributes.setKeyConstraint(Long.class);
		factorialsRegionAttributes.setValueConstraint(Long.class);

		return factorialsRegionAttributes;
	}
*/

	@Bean
	PartitionedRegionFactoryBean<Integer, Model> yodelRegion(Cache gemfireCache,
		@Qualifier("modelsRegionAttributes") RegionAttributes<Integer, Model> modelsRegionAttributes) {
//	LocalRegionFactoryBean<Integer, Model> yodelRegion(Cache gemfireCache) {

		PartitionedRegionFactoryBean<Integer, Model> cacheRegion = new PartitionedRegionFactoryBean<>();
//		LocalRegionFactoryBean<Integer, Integer> cacheRegion = new LocalRegionFactoryBean<>();

		cacheRegion.setAttributes(modelsRegionAttributes);
		cacheRegion.setCache(gemfireCache);
		cacheRegion.setClose(false);
		cacheRegion.setName("yodel");
		cacheRegion.setPersistent(false);

		return cacheRegion;
	}

	@Bean
	PartitionedRegionFactoryBean<Integer, Model> modelsRegion(Cache gemfireCache,
	                                                          @Qualifier("modelsRegionAttributes") RegionAttributes<Integer, Model> modelsRegionAttributes) {

		PartitionedRegionFactoryBean<Integer, Model> modelsRegion = new PartitionedRegionFactoryBean<>();

		modelsRegion.setAttributes(modelsRegionAttributes);
		modelsRegion.setCache(gemfireCache);
		modelsRegion.setClose(false);
		modelsRegion.setName("model");
		modelsRegion.setPersistent(false);

		return modelsRegion;
	}

	@Bean
	@SuppressWarnings("unchecked")
	RegionAttributesFactoryBean modelsRegionAttributes() {
		RegionAttributesFactoryBean modelsRegionAttributes = new RegionAttributesFactoryBean();

//		modelsRegionAttributes.setCacheLoader(modelsCacheLoader());
		modelsRegionAttributes.setKeyConstraint(Integer.class);
		modelsRegionAttributes.setValueConstraint(Model.class);

		return modelsRegionAttributes;
	}

	private ModelCacheLoader modelsCacheLoader() {
		return new ModelCacheLoader();
	}

	class ModelCacheLoader implements CacheLoader<Integer, Model> {
		private final Logger logger = LoggerFactory.getLogger(this.getClass());

		@Override
		public Model load(LoaderHelper<Integer, Model> loaderHelper) throws CacheLoaderException {
			logger.debug("Loading model with id {}", loaderHelper.getKey());

			return Model.builder()
					.id(loaderHelper.getKey())
					.value(String.valueOf(loaderHelper.getKey()))
					.build();
		}

		@Override
		public void close() {

		}
	}
/*

	FactorialsCacheLoader factorialsCacheLoader() {
		return new FactorialsCacheLoader();
	}

	class FactorialsCacheLoader implements CacheLoader<Long, Long> {

		// stupid, naive implementation of Factorial!
		@Override
		public Long load(LoaderHelper<Long, Long> loaderHelper) throws CacheLoaderException {
			long number = loaderHelper.getKey();

			assert number >= 0 : String.format("Number [%1$d] must be greater than equal to 0", number);

			if (number <= 2L) {
				return (number < 2L ? 1L : 2L);
			}

			long result = number;

			while (number-- > 1L) {
				result *= number;
			}

			return result;
		}

		@Override
		public void close() {
		}
	}
*/

}
