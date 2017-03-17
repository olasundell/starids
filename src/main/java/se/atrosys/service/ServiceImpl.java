package se.atrosys.service;

import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import se.atrosys.repository.Repository;

import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO write documentation
 */
@org.springframework.stereotype.Service
public class ServiceImpl implements Service {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Repository repository;

	@Autowired
	public ServiceImpl(Repository repository) {
		this.repository = repository;
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
}
