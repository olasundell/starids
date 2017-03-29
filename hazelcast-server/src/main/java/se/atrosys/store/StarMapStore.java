package se.atrosys.store;

import com.hazelcast.core.MapStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.atrosys.model.Star;
import se.atrosys.repository.cassandra.StarCassandraRepository;
import se.atrosys.repository.mongo.StarMongoRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * TODO write documentation
 */
@Component
public class StarMapStore implements MapStore<Long, Star> {
//	private final StarMongoRepository starRepository;
	private final StarCassandraRepository starRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public StarMapStore(StarCassandraRepository starRepository) {
		this.starRepository = starRepository;
	}

//	@Autowired
//	public StarMapStore(StarMongoRepository starRepository) {
//		this.starRepository = starRepository;
//	}
//

	@Override
	public void store(Long key, Star value) {
		logger.trace("Storing {}", value);
		starRepository.save(value);
	}

	@Override
	public void storeAll(Map<Long, Star> map) {
		logger.trace("Storing all in {}", map);
		starRepository.save(map.values());
	}

	@Override
	public void delete(Long key) {
		logger.trace("Deleting {}", key);
		starRepository.delete(key);
	}

	@Override
	public void deleteAll(Collection<Long> keys) {
		logger.trace("Deleting all {}", keys);
		keys.forEach(starRepository::delete);
	}

	@Override
	public Star load(Long key) {
		logger.trace("Loading {}", key);
		return starRepository.findOne(key);
	}

	@Override
	public Map<Long, Star> loadAll(Collection<Long> keys) {
		logger.trace("Loading all");
		final Iterable<Star> all = starRepository.findAll(keys);
		return StreamSupport.stream(all.spliterator(), false)
				.collect(Collectors.toMap(Star::getSourceId, Function.identity()));
	}

	@Override
	public Iterable<Long> loadAllKeys() {
		logger.trace("Loading all keys");
		final Iterable<Star> all = starRepository.findAll();
		return StreamSupport.stream(all.spliterator(), false)
				.map(Star::getSourceId)
				.collect(Collectors.toList());
//		final List<Star> all = starRepository.findAll();
//		return all.stream()
//				.map(Star::getSourceId)
//				.collect(Collectors.toList());
	}
}
