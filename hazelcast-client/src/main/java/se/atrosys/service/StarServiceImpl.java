package se.atrosys.service;

import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.*;
import se.atrosys.model.Star;

import java.util.List;

/**
 * TODO write documentation
 */
@org.springframework.stereotype.Service
public class StarServiceImpl implements StarService {
	private final CacheableStarLoader cacheableStarLoader;

	@Autowired
	public StarServiceImpl(CacheableStarLoader cacheableStarLoader) {
		this.cacheableStarLoader = cacheableStarLoader;
	}

	@Override
	public Observable<Star> getStar(Long id) {
		return Observable.just(cacheableStarLoader.getStar(id));
	}

	@Override
	public Observable<List<Star>> getStars(Integer count) {
		return Observable.just(cacheableStarLoader.getStars(count));
	}

	@Override
	public Observable<Page<Long>> getStarIds(Pageable pageable) {
		return Observable.just(cacheableStarLoader.getStarIds(pageable));
	}
}
