package se.atrosys;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import se.atrosys.model.Star;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
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
	public static final int MAX = 1_000;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final List<String> servers = Arrays.asList("http://localhost:8020", "http://localhost:8021", "http://localhost:8022");

	@Test
	public void shouldGetStars() throws InterruptedException, IOException {
		final int pageSize = 20;
		final List<Star> stars = new CopyOnWriteArrayList<>();
		final BlockingDeque<Long> idQueue = new LinkedBlockingDeque<>(pageSize * 5);

		ForkJoinPool idFetchPool  = new ForkJoinPool(3);
		final AtomicInteger pageNum = new AtomicInteger(0);
		final AtomicLong fetchedStars = new AtomicLong(0);

		Map<Integer, List<Long>> previousRun = readPreviousRun();
		Map<Integer, List<Long>> currentRun = new ConcurrentHashMap<>();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		for (int i = 0 ; i < 3 ; i++) {
			idFetchPool.execute(() -> {
				RestTemplate restTemplate = new RestTemplate();
				for (;;) {
					final int j = pageNum.getAndIncrement();
					if (j * pageSize > MAX) {
						return;
					}
					final String url = pageUrl(j, pageSize, servers.get(j % 3));
					RequestEntity<String> requestEntity = new RequestEntity<>(HttpMethod.GET, URI.create(url));
					ResponseEntity<RestResponsePage<Long>> pageResponse = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<RestResponsePage<Long>>() {
					});
					final RestResponsePage<Long> body = pageResponse.getBody();
//					final Page<Long> body = pageResponse.getBody();
					logger.info("Fetched ids for page {} (which we think is {}), got {}", body.getNumber(), j, body.getNumberOfElements());
					assertPrevious(previousRun, body);
					currentRun.put(j, body.getContent());
					body.forEach(idQueue::offerLast);
				}
			});
		}

		ForkJoinPool pool = new ForkJoinPool(5);
		for (;pageNum.get() * pageSize > MAX ;) {
			final long id = idQueue.takeFirst();
			pool.execute(() -> {
				RestTemplate restTemplate = new RestTemplate();
				final long l1 = fetchedStars.get();

				if (l1 % 100 == 0) {
					logger.info("Current count {}", l1);
				}

				final Star star = restTemplate.getForObject(servers.get(Math.toIntExact(l1 % servers.size())) + "/star/" + id, Star.class);
				stars.add(star);
//					logger.info("Got star {}", id);
			});
		}

		pool.shutdown();
		idFetchPool.shutdown();
		pool.awaitTermination(10, TimeUnit.SECONDS);
		idFetchPool.awaitTermination(10, TimeUnit.SECONDS);

		stopWatch.stop();
		logger.info("It took {} seconds, total fetched {}", stopWatch.getTotalTimeSeconds(), fetchedStars.get());
		writeCurrentRunToFileIfApplicable(currentRun);
	}

	private void assertPrevious(Map<Integer, List<Long>> previousRun, Page<Long> body) {
		if (!previousRun.isEmpty()) {
			Assert.assertTrue("Previous run does not contain page " + body.getNumber(),
					previousRun.containsKey(body.getNumber()));

			Assert.assertEquals("Previous run (" + body.getNumber() + ")and current run do not match",
					previousRun.get(body.getNumber()),
					body.getContent());
		}
	}

	@Test
	public void shouldReadPreviousRun() throws IOException {
		Map<Integer, List<Long>> map = readPreviousRun();
		Assert.assertNotNull(map);
	}

	private Map<Integer, List<Long>> readPreviousRun() throws IOException {
		final ClassPathResource file = new ClassPathResource("previousrun.json");
		if (file.exists()) {
			final ObjectMapper mapper = new ObjectMapper();
			final TypeFactory typeFactory = mapper.getTypeFactory();
			Map<Integer, List<Long>> map = mapper.readValue(file.getInputStream(),
					new TypeReference<Map<Integer, List<Long>>>() { });
			logger.info("Previous run data loaded, contains {} pages", map.size());
			return map;
		}
		logger.warn("No previous run data exists");
		return Collections.emptyMap();
	}

	private void writeCurrentRunToFileIfApplicable(Map<Integer, List<Long>> map) throws IOException {
		final ClassPathResource resource = new ClassPathResource("previousrun.json");
		if (!resource.exists()) {
			File file = new File("src/test/resources/" + resource.getFilename());
			new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(file, map);
			logger.info("Wrote {} pages to file {}", map.size(), file.getPath());
		}
	}

	@Test
	public void checkPaging() {
		logger.info("About to get star pages");
		final RestResponsePage<Long> body = getBody(servers.get(0));
		final RestResponsePage<Long> body1 = getBody(servers.get(1));
		final RestResponsePage<Long> body2 = getBody(servers.get(2));

		logger.warn("Got:\n{}\n{}\n{}", body.getContent(), body1.getContent(), body2.getContent());

		Assert.assertEquals(body.getContent(), body1.getContent());
		Assert.assertEquals(body.getContent(), body2.getContent());
		Assert.assertEquals(body1.getContent(), body2.getContent());
	}

	private RestResponsePage<Long> getBody(String server) {
		RestTemplate restTemplate = new RestTemplate();
		final String url = pageUrl(0, 20, server);
		RequestEntity<String> requestEntity = new RequestEntity<>(HttpMethod.GET, URI.create(url));
		ResponseEntity<RestResponsePage<Long>> pageResponse = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<RestResponsePage<Long>>() {
		});
		final RestResponsePage<Long> body = pageResponse.getBody();

		Assert.assertNotNull(body);
		Assert.assertEquals(20, body.getNumberOfElements());

		return body;
	}

	private String pageUrl(int pageNum, int pageSize, String server) {
		return server + "/starids" + "?page=" + pageNum + "&size=" + pageSize;
	}

	public static class RestResponsePage<T> extends PageImpl<T> {
		private final Logger logger = LoggerFactory.getLogger(this.getClass());

		@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
		public RestResponsePage(@JsonProperty("content") List<T> content,
		                    @JsonProperty("number") int page,
		                    @JsonProperty("size") int size,
		                    @JsonProperty("totalElements") long total) {
			super(content, new PageRequest(page, size), total);
//			logger.info("Creating rest response page from json");
		}
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
}
