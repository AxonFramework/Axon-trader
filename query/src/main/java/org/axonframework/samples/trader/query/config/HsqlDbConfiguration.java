package org.axonframework.samples.trader.query.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Profile("hsqldb")
@EnableJpaRepositories(basePackages = "org.axonframework.samples.trader.query.*.repositories",
        transactionManagerRef = "jpaTransactionManager",
        entityManagerFactoryRef = "entityManagerFactoryBean")
public class HsqlDbConfiguration {

    @Bean(name = "entityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(DataSource dataSource) {
        final LocalContainerEntityManagerFactoryBean container = new LocalContainerEntityManagerFactoryBean();
        container.setDataSource(dataSource);
        container.setPersistenceUnitName("trader");

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(true);
        container.setJpaVendorAdapter(adapter);

        container.setJpaProperties(jpaProps());
        return container;
    }

    @Bean(name = "jpaTransactionManager")
    public PlatformTransactionManager jpaTransactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    private Properties jpaProps() {
        final Properties p = new Properties();
        p.setProperty("hibernate.show_sql", "true");
        p.setProperty("hibernate.generate_statistics", "false");
        p.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        return p;
    }
}
