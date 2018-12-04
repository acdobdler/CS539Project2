package com.class539.class539project2.Configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean(name = "MSDataSource")
    @Qualifier("MSDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource msDataSource() {
        return DataSourceBuilder.create().build();

    }

    @Bean(name = "msSqlConnector")
    public JdbcTemplate getMsSqlConnector(@Qualifier("MSDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
