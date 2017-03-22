package se.atrosys.config;

import org.ehcache.Cache;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import se.atrosys.model.Model;

import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO write documentation
 */
//@Configuration
public class EhCacheConfig {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
/*	@Bean
	public org.ehcache.CacheManager ehCacheManager() {
		logger.info("Creating ehcache cache manager");
		org.ehcache.CacheManager cacheManager
				= CacheManagerBuilder.newCacheManagerBuilder()
				.withCache("preConfigured",
						CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, Model.class, ResourcePoolsBuilder.heap(10)))
				.build();
		cacheManager.init();

		Cache<Integer, Model> preConfigured =
				cacheManager.getCache("preConfigured", Integer.class, Model.class);

		Cache<Integer, Model> myCache = cacheManager.createCache("yodel",
				CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, Model.class, ResourcePoolsBuilder.heap(10)).build());

//		myCache.put(1, "da one!");
//		String value = myCache.get(1L);

		cacheManager.removeCache("preConfigured");

		cacheManager.close();

		return cacheManager;
	}*/

//	@Bean
//	@DependsOn("eh107CacheManager")
	public CacheManager cacheManager(javax.cache.CacheManager cacheManager) {
		logger.info("Creating spring cache manager");
		final JCacheCacheManager jcacheManager = new JCacheCacheManager();

		jcacheManager.setCacheManager(cacheManager);

		return jcacheManager;
	}

	@Bean
//	@DependsOn("ehCacheManager")
	public javax.cache.CacheManager eh107CacheManager() {
		logger.info("Creating JSR107 cache manager");
		Iterable<CachingProvider> iterable = Caching.getCachingProviders();
		List<CachingProvider> cachingProviders = new ArrayList<>();
		iterable.forEach(cachingProviders::add);
		CachingProvider provider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
		javax.cache.CacheManager cacheManager = provider.getCacheManager();
		MutableConfiguration<Object, Object> configuration =
				new MutableConfiguration<Object, Object>()
						.setTypes(Object.class, Object.class)
						.setStoreByValue(false)
						.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));
		javax.cache.Cache<Object, Object> cache = cacheManager.createCache("yodel", configuration);
//		cache.put(1L, "one");
//		String value = cache.get(1L);
//		Iterable<CachingProvider> iterable = Caching.getCachingProviders();
//		List<CachingProvider> cachingProviders = new ArrayList<>();
//		iterable.forEach(cachingProviders::add);
//
//		CachingProvider provider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
//		javax.cache.CacheManager cacheManager = provider.getCacheManager();
//		CacheConfiguration<Integer, Model> cacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, Model.class,
//				ResourcePoolsBuilder.heap(10)).build();
////
//		final javax.cache.configuration.Configuration kvConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration);
//		javax.cache.Cache<Integer, Model> cache = cacheManager.createCache("yodel", kvConfiguration);
//
//		Eh107Configuration<Long, String> configuration = cache.getConfiguration(Eh107Configuration.class);
//		configuration.unwrap(CacheConfiguration.class);
//
//		configuration.unwrap(CacheRuntimeConfiguration.class);
//
//		try {
//			cache.getConfiguration(CompleteConfiguration.class);
//			throw new AssertionError("IllegalArgumentException expected");
//		} catch (IllegalArgumentException iaex) {
//			 Expected
//		}

		return cacheManager;
	}

//	@Bean
//	public CacheManager cacheManager(org.ehcache.CacheManager ehCacheManager) {
//		return new EhCacheCacheManager(ehCacheManager);
//	}
}
