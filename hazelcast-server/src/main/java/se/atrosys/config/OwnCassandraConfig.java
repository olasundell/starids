package se.atrosys.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.cassandra.config.CassandraCqlClusterFactoryBean;
import org.springframework.cassandra.config.DataCenterReplication;
import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification;
import org.springframework.cassandra.core.keyspace.KeyspaceActionSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.mapping.PrimaryKey;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * TODO write documentation
 */
@Configuration
@AutoConfigureAfter(CassandraDataAutoConfiguration.class)
public class OwnCassandraConfig extends AbstractCassandraConfiguration {
	private final CassandraProperties cassandraProperties;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public OwnCassandraConfig(CassandraProperties cassandraProperties) {
		this.cassandraProperties = cassandraProperties;
	}

	@Bean
	@Override
	public CassandraCqlClusterFactoryBean cluster() {
		CassandraCqlClusterFactoryBean cluster = super.cluster();
		final HashSet<KeyspaceActionSpecification<?>> keyspaceSpecifications = new HashSet<>();
		keyspaceSpecifications.add(getKeySpaceSpecification());
		cluster.setKeyspaceSpecifications(keyspaceSpecifications);

		return cluster;
	}

	@Override
	@Bean
	public CassandraSessionFactoryBean session() throws ClassNotFoundException {
		return super.session();
	}

	@Override
	protected String getContactPoints() {
		final String contactPoints = cassandraProperties.getContactPoints();
		logger.info("Returning contact points {}", contactPoints);
		return contactPoints;
	}

	@Override
	protected String getKeyspaceName() {
		return cassandraProperties.getKeyspaceName();
	}

	protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
		List<CreateKeyspaceSpecification> createKeyspaceSpecifications = new ArrayList<>();
		createKeyspaceSpecifications.add(getKeySpaceSpecification());
		return createKeyspaceSpecifications;
	}

	// Below method creates "my_keyspace" if it doesnt exist.
	private CreateKeyspaceSpecification getKeySpaceSpecification() {
		CreateKeyspaceSpecification pandaCoopKeyspace = new CreateKeyspaceSpecification();
		DataCenterReplication dcr = new DataCenterReplication("dc1", 3L);
		pandaCoopKeyspace.name(getKeyspaceName());
		pandaCoopKeyspace.ifNotExists(true).createKeyspace().withNetworkReplication(dcr);
		return pandaCoopKeyspace;
	}
}
