package se.atrosys.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import se.atrosys.model.Model;

import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO write documentation
 */
@Component
public class CacheableModelService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ConcurrentHashMap<Integer, Integer> modelTimes = new ConcurrentHashMap<>();

	@Cacheable(cacheNames = {"yodel"}, key = "#id", sync=true)
//	@Cacheable(cacheNames = {"yodel"}, key = "#id")
	public Model actuallyGetModel(final Integer id) {
		logger.info("Constructing/reading model {}", id);
		try {
			Thread.sleep(750);
		} catch (InterruptedException e) {
			logger.error("Interupted", e);
		}
		final Model one = Model.builder()
				.id(id)
				.value(String.valueOf(id))
				.build();
		logger.info("Got {}", one);

		synchronized (modelTimes) {
			modelTimes.put(id, modelTimes.getOrDefault(id, 0) + 1);
		}

		return one;
	}

	public Integer getModelTimes(Integer id) {
		logger.info("Getting times for {}, which does {}exist in the map", id, modelTimes.containsKey(id) ? "" : "NOT ");
		return modelTimes.getOrDefault(id, 0);
	}
}
