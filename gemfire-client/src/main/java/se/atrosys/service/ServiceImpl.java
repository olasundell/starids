package se.atrosys.service;

import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import se.atrosys.model.Model;

import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO write documentation
 */
@org.springframework.stereotype.Service
public class ServiceImpl implements Service {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
//	private final ModelRepository modelRepository;
	private final ConcurrentHashMap<Integer, Integer> modelTimes = new ConcurrentHashMap<>();

//	@Autowired
//	public ServiceImpl(ModelRepository modelRepository) {
//		this.modelRepository = modelRepository;
//	}
//
	@Override
	public Observable<String> getString(Integer id) {
		return null;
	}

	@Override
	public Integer times(Integer id) {
		return null;
	}

	@Override
	public Observable<Model> getModel(Integer id) {
		return Observable.just(actuallyGetModel(id));
	}

	@Cacheable(cacheNames = "yodel", key = "#id", sync = true)
	public Model actuallyGetModel(final Integer id) {
		logger.info("Getting model {}", id);
		try {
			Thread.sleep(750);
		} catch (InterruptedException e) {
			logger.error("Interupted", e);
		}
//		final Model one = modelRepository.findOne(id);
		final Model one = Model.builder()
				.id(id)
				.value(String.valueOf(id))
				.build();
		logger.info("Got {}", one);
		modelTimes.put(id, modelTimes.getOrDefault(id, 0) + 1);
		return one;
	}

	@Override
	public Integer modelTimes(Integer id) {
		logger.info("Getting times for {}, which does {}exist in the map", id, modelTimes.containsKey(id) ? "" : "NOT ");
		final Integer integer = modelTimes.getOrDefault(id, 0);
		return integer;
	}
}
