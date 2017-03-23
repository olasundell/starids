package se.atrosys.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * TODO write documentation
 */
@Configuration
public class ContainerConfig {
	private final Environment environment;

	@Autowired
	public ContainerConfig(Environment environment) {
		this.environment = environment;
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return (container -> {
			container.setPort(environment.getProperty("SERVER_PORT", Integer.class, environment.getProperty("server.port", Integer.class, 8080)));
		});
	}
}
