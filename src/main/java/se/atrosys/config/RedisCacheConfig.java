package se.atrosys.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;

/**
 * TODO write documentation
 */
//@Configuration
public class RedisCacheConfig {
//	@Bean
	public CacheManager cacheManager() {
		final RedisTemplate redisOperations = new RedisTemplate();
		final RedisCacheManager manager = new RedisCacheManager(redisOperations);
		manager.setCacheNames(Collections.singletonList("string"));
		manager.setDefaultExpiration(5L);
		return manager;
	}
}
