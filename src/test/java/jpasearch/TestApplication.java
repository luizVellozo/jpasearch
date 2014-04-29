package jpasearch;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author speralta
 */
@Configuration
@ComponentScan
public class TestApplication {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(HibernateJpaVendorAdapter jpaVendorAdapter, DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("jpasearch.domain");
        entityManagerFactoryBean.getJpaPropertyMap().put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        entityManagerFactoryBean.getJpaPropertyMap().put("hibernate.search.analyzer", "org.apache.lucene.analysis.fr.FrenchAnalyzer");
        entityManagerFactoryBean.getJpaPropertyMap().put("hibernate.search.default.directory_provider", "ram");
        entityManagerFactoryBean.getJpaPropertyMap().put("hibernate.search.default.worker.execution", "async");
        entityManagerFactoryBean.getJpaPropertyMap().put("hibernate.search.default.worker.execution", "3");
        return entityManagerFactoryBean;
    }

    @Bean
    public HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setDatabase(Database.H2);
        adapter.setGenerateDdl(true);
        adapter.getJpaPropertyMap().put("hibernate.ddl-auto", "none");
        adapter.getJpaPropertyMap().put("hibernate.naming-strategy", "org.hibernate.cfg.DefaultNamingStrategy");
        return adapter;
    }
}
