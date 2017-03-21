package se.atrosys.service;

import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import se.atrosys.model.Model;
import se.atrosys.repository.ModelRepository;
import se.atrosys.repository.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO write documentation
 */
@org.springframework.stereotype.Service
public class ServiceImpl implements Service {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Repository repository;
	private final CacheableModelService cacheableModelService;

	@Autowired
	public ServiceImpl(Repository repository, CacheableModelService cacheableModelService) {
		this.repository = repository;
		this.cacheableModelService = cacheableModelService;
	}

	@Override
	public Observable<String> getString(Integer id) {
		return Observable.just(repository.getString(id));

//		return Observable.just(repository.getString(id)).(() -> times.getOrDefault(id));
	}

	@Override
	public Integer times(Integer id) {
		return repository.times(id);
	}

	@Override
	public Observable<Model> getModel(final Integer id) {
		return Observable.just(cacheableModelService.actuallyGetModel(id));
//				.doOnComplete(() -> modelTimes.put(id, modelTimes.getOrDefault(id, 0) + 1));
	}

	@Override
	public Integer modelTimes(Integer id) {
		return cacheableModelService.getModelTimes(id);
	}
}
