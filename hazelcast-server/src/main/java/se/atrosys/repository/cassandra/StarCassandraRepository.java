package se.atrosys.repository.cassandra;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import se.atrosys.model.Star;

/**
 * TODO write documentation
 */
public interface StarCassandraRepository extends CrudRepository<Star, Long> {
}
