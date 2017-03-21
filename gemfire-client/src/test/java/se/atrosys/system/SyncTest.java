package se.atrosys.system;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import se.atrosys.model.Model;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * TODO write documentation
 */
public class SyncTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final int ID = 6;
	public static final String ID_STR = String.valueOf(ID);
	private final List<String> servers = Arrays.asList("http://localhost:8090", "http://localhost:8081", "http://localhost:8082");

	@Test
	public void shouldGetModel() throws InterruptedException {
		final List<Model> models = new CopyOnWriteArrayList<>();
		final List<Model> canaries = new CopyOnWriteArrayList<>();
		RestTemplate restTemplate = new RestTemplate();

		restTemplate.getForObject(servers.get(0) + "/purge/" + ID_STR, Void.class);

		int oldSum = summate(restTemplate);

		ForkJoinPool pool = new ForkJoinPool(5);
		for (int i = 0 ; i < 15 ; i++) {
			final int i2 = i;

			pool.execute(() -> {
				final Model model = restTemplate.getForObject(servers.get(i2 % servers.size()) + "/model/" + ID_STR, Model.class);
				logger.info("Got model {}", model);
				models.add(model);
			});
		}

		pool.shutdown();
		pool.awaitTermination(3, TimeUnit.SECONDS);

		Assert.assertEquals("Should have gotten the correct amount of results", 15, models.size());

		models.forEach(model -> {
			Assert.assertNotNull(model);
			Assert.assertEquals(ID, model.getId().intValue());
			Assert.assertEquals(ID_STR, model.getValue());
		});


		int sum = summate(restTemplate);

		Assert.assertEquals("Should only have called once", oldSum + 1, sum);

	}

	private int summate(RestTemplate restTemplate) {
		return servers.stream().mapToInt(s -> restTemplate.getForObject(s + "/modeltimes/" + ID_STR, Integer.class)).sum();
	}
}
