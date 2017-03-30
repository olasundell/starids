package se.atrosys.repository.mongo;

import org.springframework.data.repository.PagingAndSortingRepository;
import se.atrosys.model.Star;

/**
 * TODO write documentation
 */
public interface StarMongoRepository extends PagingAndSortingRepository<Star, Long> {
}
