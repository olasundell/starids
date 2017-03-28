package se.atrosys.service;

import io.reactivex.Observable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se.atrosys.model.Star;

import java.util.List;

/**
 * TODO write documentation
 */
public interface StarService {
	Observable<Star> getStar(Long id);
	Observable<List<Star>> getStars(Integer count);

	Observable<Page<Long>> getStarIds(Pageable pageable);
}
