package se.atrosys.config.gemfire;

import com.gemstone.gemfire.cache.GemFireCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.LocalRegionFactoryBean;
import se.atrosys.model.Model;
import se.atrosys.repository.ModelRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.Properties;

/**
 * TODO write documentation
 */
//@Configuration
//@EnableGemfireRepositories
public class GemfireConfig {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private ModelRepository modelRepository;
	private final Environment environment;

	@Autowired
	public GemfireConfig(Environment environment) {
		this.environment = environment;
	}

	@Autowired
	public void setModelRepository(ModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	//	@Autowired
//	public GemfireConfig(ModelRepository modelRepository) {
//		this.modelRepository = modelRepository;
//	}

//	@PostConstruct
//	public void createModels() {
//		Model model = modelRepository.save(Model.builder().id(1).value("1").build());
//		logger.info("Model saved, {}", model);
//	}

	@Bean
	public Properties gemfireProperties() {
		Properties gemfireProperties = new Properties();
		final String name = "hystrix-test-" + environment.getProperty("server.port", String.class, "8080");
		gemfireProperties.setProperty("name", name);
		logger.info("Set gemfire name to {}", name);
//		gemfireProperties.setProperty("mcast-port", "");
		gemfireProperties.setProperty("log-level", "config");
		return gemfireProperties;
	}

	@Bean
	public CacheFactoryBean gemfireCache() {
		CacheFactoryBean gemfireCache = new CacheFactoryBean();
		gemfireCache.setClose(true);
		gemfireCache.setProperties(gemfireProperties());
		return gemfireCache;
	}

	@Bean
	public LocalRegionFactoryBean<Integer, Integer> yodelRegion(final GemFireCache cache) {
		LocalRegionFactoryBean<Integer, Integer> modelRegion = new LocalRegionFactoryBean<>();
		modelRegion.setCache(cache);
		modelRegion.setClose(false);
		modelRegion.setName("yodel");
		modelRegion.setPersistent(false);
		return modelRegion;
	}

	@Bean
	public LocalRegionFactoryBean<Integer, Model> modelRegion(final GemFireCache cache) {
		LocalRegionFactoryBean<Integer, Model> modelRegion = new LocalRegionFactoryBean<>();
		modelRegion.setCache(cache);
		modelRegion.setClose(false);
		modelRegion.setName("model");
		modelRegion.setPersistent(false);
		return modelRegion;
	}
}
