package se.atrosys.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO write documentation
 */
@Service
public class Repository {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ConcurrentHashMap<Integer, AtomicInteger> times = new ConcurrentHashMap<>();

	@Cacheable(cacheNames = {"string"}, sync = true, key="#id")
	public String getString(Integer id) {
		logger.info("Constructing str for id {}", id);
		try {
			Thread.sleep(750);
		} catch (InterruptedException e) {
			logger.error("Error {}", e);
		}

		if (id == -1) {
			throw new InvalidParameterException("-1 is not valid!");
		}

		logger.info("Returning str for id {}", id);

		// is this really thread safe?
		times.putIfAbsent(id, new AtomicInteger(0));
		times.get(id).incrementAndGet();

		return "myNiceStr" + id;
	}

	public Integer times(Integer id) {
		final int i = times.getOrDefault(id, new AtomicInteger(0)).get();
		logger.debug("Called {} times", i);
		return i;
	}
}
