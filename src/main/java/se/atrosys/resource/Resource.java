package se.atrosys.resource;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.atrosys.service.Service;

/**
 * TODO write documentation
 */
@RestController
public class Resource {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Service service;

	@Autowired
	public Resource(Service service) {
		HystrixRequestContext context = HystrixRequestContext.initializeContext();

		this.service = service;
	}

	@RequestMapping("/resource/{id}")
//	@HystrixCollapser(batchMethod = "getString")
	@HystrixCommand(commandKey = "getString", groupKey = "resource")
//	@CacheResult
//	public Observable<String> resource(@PathVariable @CacheKey Integer id) {
	public Observable<String> resource(@PathVariable Integer id) {
		return service.getString(id);
	}

	@RequestMapping("/times/{id}")
	public Integer times(@PathVariable Integer id) {
		return service.times(id);
	}

//	@HystrixCommand
//	public List<String> getString(List<Integer> id) {
//		logger.info("Request received");
//		return id.stream().map(service::getString).collect(Collectors.toList());
////		return service.getString(id);
//	}
}
