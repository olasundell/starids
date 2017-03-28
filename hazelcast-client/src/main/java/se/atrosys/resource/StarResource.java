package se.atrosys.resource;

import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.atrosys.model.Star;
import se.atrosys.service.StarService;

import java.util.List;

/**
 * TODO write documentation
 */
@RestController
public class StarResource {
	private final StarService starService;

	@Autowired
	public StarResource(StarService starService) {
		this.starService = starService;
	}

	@RequestMapping("/star/{id}")
	public Observable<Star> getStar(@PathVariable Long id) {
		return starService.getStar(id);
	}

	@RequestMapping("/stars/")
	public Observable<List<Star>> getStars() {
		return starService.getStars(20);
	}

	@RequestMapping("/starids")
	public Observable<Page<Long>> getStarIds(Pageable pageable) {
		return starService.getStarIds(pageable);
	}
}
