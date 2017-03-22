package se.atrosys.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.atrosys.model.Model;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

/**
 * TODO write documentation
 */
@Configuration
public class InfinispanCacheConfig {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

//		iterable.forEach(cachingProviders::add);
	@Bean
	public CacheManager cacheManager(javax.cache.CacheManager cacheManager) {
		logger.info("Creating spring cache manager");
		final JCacheCacheManager jcacheManager = new JCacheCacheManager();

		jcacheManager.setCacheManager(cacheManager);

		return jcacheManager;
	}

	@Bean
	public javax.cache.CacheManager jsr107Manager() {
		// Construct a simple local cache manager with default configuration
//		CachingProvider jcacheProvider = Caching.getCachingProvider("org.infinispan.jcache.embedded.JCachingProvider");
		CachingProvider jcacheProvider = Caching.getCachingProvider("org.infinispan.jcache.embedded.JCachingProvider");
		javax.cache.CacheManager cacheManager = jcacheProvider.getCacheManager();
//		MutableConfiguration<Object, Object> configuration = new MutableConfiguration<>();
//		configuration.setTypes(Object.class, Object.class);
		MutableConfiguration<Object, Object> configuration =
				new MutableConfiguration<Object, Object>()
						.setTypes(Object.class, Object.class)
						.setStoreByValue(false)
						.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));
		// create a cache using the supplied configuration
		Cache<Object, Object> cache = cacheManager.createCache("yodel", configuration);
		// Store a value
		// Stop the cache manager and release all resources
//		cacheManager.close();
		return cacheManager;
	}
}
