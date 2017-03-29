package se.atrosys.config;

import com.datastax.driver.core.Cluster;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.Server;
import com.mongodb.selector.ServerSelector;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.repository.support.CassandraRepositoryFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO write documentation
 */
@Configuration
public class JpaConfig {
//	@Bean
//	@Primary
//	@ConfigurationProperties(prefix="spring.datasource")
//	public DataSource firstDataSource() {
//		DataSource ds =  DataSourceBuilder.create().build();
//		return ds;
//	}
//	@Bean
//	public Cluster noOpCassandraCluster() {
//		return Cluster.builder()
//				.addContactPoint(null)
//				.build();
//	}

//	@Bean
//	public com.mongodb.connection.Cluster noOpMongoCluster() {
//		return new com.mongodb.connection.Cluster() {
//			@Override
//			public ClusterSettings getSettings() {
//				return null;
//			}
//
//			@Override
//			public ClusterDescription getDescription() {
//				return null;
//			}
//
//			@Override
//			public Server selectServer(ServerSelector serverSelector) {
//				return null;
//			}
//
//			@Override
//			public void selectServerAsync(ServerSelector serverSelector, SingleResultCallback<Server> callback) {
//
//			}
//
//			@Override
//			public void close() {
//
//			}
//
//			@Override
//			public boolean isClosed() {
//				return false;
//			}
//		};
//	}

//	@Bean
//	public CassandraTemplate cassandraTemplate() {
//		return new CassandraTemplate();
//	}
//	@Bean
//	public CassandraRepositoryFactoryBean cassandraRepositoryFactoryBean(CassandraRepositoryFactoryBean cassandraRepositoryFactoryBean) {
//		cassandraRepositoryFactoryBean.
//		return cassandraRepositoryFactoryBean;
//	}

//	@Bean
//	public DataSource dataSource()  {
//		return DataSourceBuilder.create()
//				.build();
//
//	}
//	@Bean(name = "entityManager")
//	public LocalContainerEntityManagerFactoryBean mySqlEntityManagerFactory(EntityManagerFactoryBuilder builder,
//	                                                                        DataSource dataSource) {
//		return builder.dataSource(dataSource)
//				.persistenceUnit("default")
//				.build();
//	}

//	private Map<String, Object> jpaProperties() {
//		Map<String, Object> props = new HashMap<>();
//		props.put("hibernate.ejb.naming_strategyw SpringNamingStrategy());
//		return props;
//	}
}
