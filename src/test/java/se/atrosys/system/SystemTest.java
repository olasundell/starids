package se.atrosys.system;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import se.atrosys.model.Model;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * TODO write documentation
 */
public class SystemTest {
//	private final List<String> servers = Arrays.asList("http://localhost:8080", "http://localhost:8081", "http://localhost:8082");
	private final List<String> servers = Arrays.asList("http://localhost:8090");
	@Test
	public void shouldGet() throws InterruptedException {
		RestTemplate restTemplate = new RestTemplate();

		int oldSum = servers.stream().mapToInt(s -> restTemplate.getForObject(s + "/times/1", Integer.class)).sum();

		ForkJoinPool pool = new ForkJoinPool(5);
//		pool.submit(() -> {
//			String result = restTemplate.getForObject("http://localhost:8080/resource/1", String.class);
//		});
		for (int i = 0 ; i < 15 ; i++) {
			final int i2 = i;

			pool.submit(() -> {
				String result = restTemplate.getForObject(servers.get(i2 % 3) + "/resource/1", String.class);
			});
		}

//		pool.submit(() -> {
//			String result = restTemplate.getForObject("http://localhost:8080/resource/-1", String.class);
//		});

		pool.shutdown();
		pool.awaitTermination(2, TimeUnit.SECONDS);

		int sum = servers.stream().mapToInt(s -> restTemplate.getForObject(s + "/times/1", Integer.class)).sum();

		Assert.assertEquals("Should only have called once", oldSum + 1, sum);
	}
}
