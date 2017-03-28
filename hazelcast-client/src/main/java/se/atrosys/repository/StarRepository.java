package se.atrosys.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import se.atrosys.model.Star;
import se.atrosys.model.StarId;

/**
 * TODO write documentation
 */
public interface StarRepository extends PagingAndSortingRepository<Star, Long> {
	Page<StarId> readAllBy(Pageable pageable);
}
