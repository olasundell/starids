package se.atrosys.resource;

import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.atrosys.model.Model;
import se.atrosys.service.Service;

/**
 * TODO write documentation
 */
@RestController
public class ModelResource {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Service service;

	@Autowired
	public void setService(Service service) {
		this.service = service;
	}

	@RequestMapping("/model/{id}")
	public Observable<Model> resourceModel(@PathVariable Integer id) {
//		return service.getModel(id); //.single(Model.builder().build());
		logger.info("Getting model {}", id);
		final Model model = service.actuallyGetModel(id);
		return Observable.just(model);
	}

	@RequestMapping(value = "/modeltimes/{id}")
	public Integer times(@PathVariable Integer id) {
		logger.info("Resource getting times for {}", id);
		return service.modelTimes(id);
	}

	@RequestMapping(value = "/purge/{id}")
	@CacheEvict(cacheNames = {"yodel"}, key = "#id")
	public void purge(@PathVariable Integer id) {
		logger.warn("purging cache entry {}", id);
	}

	@RequestMapping(value = "/purgeall")
	@CacheEvict(cacheNames = {"yodel"}, allEntries = true)
	public void purgeAll() {
		logger.warn("purging all cache entries");
	}

}
