package se.atrosys.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import se.atrosys.model.Star;
import se.atrosys.model.StarId;
import se.atrosys.repository.jpa.StarRepository;

import java.util.List;

/**
 * TODO write documentation
 */
@org.springframework.stereotype.Service
public class CacheableStarLoader {
	private final StarRepository repository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public CacheableStarLoader(StarRepository repository) {
		this.repository = repository;
	}

	@Cacheable(cacheNames = "stars", sync = true)
	public Star getStar(Long id) {
		logger.debug("Getting star {}", id);
		return repository.findOne(id);
	}

	List<Star> getStars(Integer count) {
		return repository.findAll(new PageRequest(0, count)).getContent();
	}

//	@Cacheable(cacheNames = "starids", sync = true)
	public Page<Long> getStarIds(Pageable pageable) {
		Pageable p = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC, "sourceId");
		return repository.findAll(p).map(Star::getSourceId);
	}
}
