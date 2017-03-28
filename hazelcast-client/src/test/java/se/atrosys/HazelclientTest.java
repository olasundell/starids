package se.atrosys;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import se.atrosys.model.Star;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO write documentation
 */
public class HazelclientTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final List<String> servers = Arrays.asList("http://localhost:8020", "http://localhost:8021", "http://localhost:8022");

//	@Before
//	public void setUp() throws Exception {
//		restTemplate = new RestTemplate();
//	}
//
	@Test
	public void shouldGetStars() throws InterruptedException {
		final int pageSize = 20;
		final List<Star> stars = new CopyOnWriteArrayList<>();
		final BlockingDeque<Long> idQueue = new LinkedBlockingDeque<>(pageSize * 5);

		ForkJoinPool idFetchPool  = new ForkJoinPool(3);
		final AtomicInteger pageNum = new AtomicInteger(0);
		final AtomicLong fetchedStars = new AtomicLong(0);

		for (int i = 0 ; i < 3 ; i++) {
			idFetchPool.execute(() -> {
				RestTemplate restTemplate = new RestTemplate();
				for (;;) {
					logger.info("Fetching ids");
					final int j = pageNum.getAndIncrement();
					final String url = servers.get(j % 3) + "/starids" + "?page=" + j + "&size=" + pageSize;
					RequestEntity<String> requestEntity = new RequestEntity<>(HttpMethod.GET, URI.create(url));
					ResponseEntity<RestResponsePage<Long>> pageResponse = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<RestResponsePage<Long>>() {
					});
					pageResponse.getBody().forEach(idQueue::offerLast);
				}
			});
		}

		ForkJoinPool pool = new ForkJoinPool(5);
		for (int i = 0 ; i < 5 ; i++) {
//			final int i2 = i;

			pool.execute(() -> {
				RestTemplate restTemplate = new RestTemplate();
				for (;;) {
					try {
						final long id = idQueue.takeFirst();
						final Star star = restTemplate.getForObject(servers.get(Math.toIntExact(fetchedStars.get() % servers.size())) + "/star/" + id, Star.class);
						fetchedStars.getAndIncrement();
//					logger.info("Got star {}", id);
						stars.add(star);
					} catch (InterruptedException e) {
						logger.error("Interrupted!", e);
					}
				}
			});
		}

		for (long f = 0; f < 1_000_000; f = fetchedStars.get()) {
//			if (f % 100 == 0) {
			logger.info("Current count {}", f);
//			}
			Thread.sleep(1_000);
		}


		pool.shutdown();
		idFetchPool.shutdown();
		pool.awaitTermination(10, TimeUnit.SECONDS);
		idFetchPool.awaitTermination(10, TimeUnit.SECONDS);
	}

	public static class RestResponsePage<T> extends PageImpl<T> {

		public RestResponsePage(List<T> content, Pageable pageable, long total) {
			super(content, pageable, total);
		}

		public RestResponsePage(List<T> content) {
			super(content);
		}

		public RestResponsePage() {
			super(new ArrayList<T>());
		}
	}

//		restTemplate.getForObject(servers.get(0) + "/purge/" + ID_STR, Void.class);
//		restTemplate.getForObject(servers.get(0) + "/purgeall/", Void.class);

//		int oldSum = summate(restTemplate);
//
//		ForkJoinPool pool = new ForkJoinPool(5);
//		for (int i = 0 ; i < 15 ; i++) {
//			final int i2 = i;
//
//			pool.execute(() -> {
//				final model = restTemplate.getForObject(servers.get(i2 % servers.size()) + "/model/" + ID_STR, Model.class);
//				logger.info("Got model {}", model);
//				models.add(model);
//			});
//		}
//
//		pool.shutdown();
//		pool.awaitTermination(3, TimeUnit.SECONDS);
//
//		Assert.assertEquals("Should have gotten the correct amount of results", 15, models.size());
//
//		models.forEach(this::assertModel);
//
//
//		int sum = summate(restTemplate);
//
//		Assert.assertEquals("Should only have called once", oldSum + 1, sum);
//
//	}
//
//	private void assertModel(Model model) {
//		Assert.assertNotNull(model);
//		Assert.assertEquals(ID, model.getId().intValue());
//		Assert.assertEquals(ID_STR, model.getValue());
//	}
//
//	private int summate(RestTemplate restTemplate) {
//		return servers.stream().mapToInt(s -> restTemplate.getForObject(s + "/modeltimes/" + ID_STR, Integer.class)).sum();
//	}
}
