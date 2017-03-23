package se.atrosys.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;

/**
 * TODO write documentation
 */
@Configuration
@Profile("redis")
public class RedisCacheConfig {
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		final LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
		return lettuceConnectionFactory;
	}

	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate redisTemplate = new RedisTemplate();

		redisTemplate.setConnectionFactory(redisConnectionFactory);

		return redisTemplate;
	}
	@Bean
	public CacheManager cacheManager(RedisTemplate redisTemplate) {
		final RedisCacheManager manager = new RedisCacheManager(redisTemplate);
		manager.setCacheNames(Collections.singletonList("yodel"));
//		manager.setTransactionAware(true);
		manager.setDefaultExpiration(5L);
		return manager;
	}
}

