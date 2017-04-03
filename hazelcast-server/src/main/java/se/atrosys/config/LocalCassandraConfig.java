package se.atrosys.config;

import com.datastax.driver.core.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.cassandra.config.CassandraCqlClusterFactoryBean;
import org.springframework.cassandra.config.KeyspaceAction;
import org.springframework.cassandra.config.KeyspaceActionSpecificationFactoryBean;
import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification;
import org.springframework.cassandra.core.keyspace.KeyspaceActionSpecification;
import org.springframework.cassandra.core.keyspace.KeyspaceOption;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO write documentation
 */
//@Configuration
//@AutoConfigureBefore(org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration.class)
//@AutoConfigureOrder(0)
//@DependsOn("CassandraDataAutoConfiguration")
//@DependsOn("org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration")
//public abstract class CassandraConfig extends AbstractClusterConfiguration implements BeanClassLoaderAware {
//public class CassandraConfig extends AbstractCassandraConfiguration {
public class LocalCassandraConfig implements BeanPostProcessor {
//public class LocalCassandraConfig {
//public class LocalCassandraConfig extends AbstractCassandraConfiguration implements BeanPostProcessor {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
//	private static final Logger logger = LoggerFactory.getLogger(LocalCassandraConfig.class);
	private final String keyspaceName;

	@Autowired
	protected LocalCassandraConfig(Environment environment) {
		this.keyspaceName = environment.getRequiredProperty("spring.data.cassandra.keyspace-name");
	}

	@Bean
	public KeyspaceActionSpecification createKeyspace() {
//		return new CreateKeyspaceSpecification(keyspaceName).ifNotExists();
		CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(keyspaceName)
				.ifNotExists()
				.with(KeyspaceOption.DURABLE_WRITES);
//				.withNetworkReplication(DataCenterReplication.dcr("foo", 1))

		return specification;
	}

//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

//	@Override
	public void setServletContext(ServletContext servletContext) {
		logger.error("Adding keyspace creation spec to the cluster factory bean");
//		CassandraCqlClusterFactoryBean clusterFactoryBean = servletContext.get(CassandraCqlClusterFactoryBean.class);
//		Set<KeyspaceActionSpecification<?>> specs = new HashSet<>(clusterFactoryBean.getKeyspaceSpecifications());

//		specs.add(createKeyspace());

//		clusterFactoryBean.setKeyspaceSpecifications(specs);
	}

	private String createKeyspaceScript() {
		return "CREATE KEYSPACE IF NOT EXISTS " + keyspaceName + " "
				+ "WITH durable_writes = true "
				+ "AND replication = { 'replication_factor' : 1, 'class' : 'SimpleStrategy' };";
	}
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		logger.info("before {}", beanName);
		if (beanName.equals("cluster")) {
			Cluster cluster = (Cluster) bean;
		}
		if (beanName.equals("session")) {
			CassandraSessionFactoryBean session = (CassandraSessionFactoryBean) bean;
			session.setStartupScripts(Collections.singletonList(createKeyspaceScript()));
			logger.info("setting startup scripts with keyspace {}", keyspaceName);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		logger.trace("after  {}", beanName);
		return bean;
	}

//	@Bean
//	public static KeyspaceActionSpecificationFactoryBean keyspaceActionSpecificationFactoryBean() {
//		logger.info("Creating keyspace action spec bean");
//		final KeyspaceActionSpecificationFactoryBean bean = new KeyspaceActionSpecificationFactoryBean();
//
//		bean.setAction(KeyspaceAction.CREATE);
//		bean.setName(keyspaceName);
//		bean.setDurableWrites(true);
//		bean.setIfNotExists(true);
//
//		return bean;
//	}

/*	@Override
	protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
		logger.info("Creating keyspace creations specification");
		CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(keyspaceName)
				.ifNotExists()
				.with(KeyspaceOption.DURABLE_WRITES);
//				.withNetworkReplication(DataCenterReplication.dcr("foo", 1))

		return Collections.singletonList(specification);
	}

	@Override
	protected String getKeyspaceName() {
		logger.trace("Getting keyspace name {}", keyspaceName);
		return keyspaceName;
	}
	*/
}
