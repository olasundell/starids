package se.atrosys.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import se.atrosys.model.Model;
import se.atrosys.repository.ModelRepository;

import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO write documentation
 */
@Component
public class CacheableModelService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ModelRepository modelRepository;
	private final ConcurrentHashMap<Integer, Integer> modelTimes = new ConcurrentHashMap<>();

	@Autowired
	public CacheableModelService(ModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	@Cacheable(cacheNames = {"yodel"}, key = "#id", sync=true)
	public Model actuallyGetModel(final Integer id) {
		logger.info("Getting model {}", id);
		try {
			Thread.sleep(750);
		} catch (InterruptedException e) {
			logger.error("Interupted", e);
		}
		final Model one = modelRepository.findOne(id);
		logger.info("Got {}", one);
		modelTimes.put(id, modelTimes.getOrDefault(id, 0) + 1);
		return one;
	}

	public Integer getModelTimes(Integer id) {
		return modelTimes.getOrDefault(id, 0);
	}
}
