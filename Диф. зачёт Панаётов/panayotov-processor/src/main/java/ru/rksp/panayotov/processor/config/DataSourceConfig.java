package ru.rksp.panayotov.processor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    // Основной DataSource для PostgreSQL (используется JPA)
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.postgres")
    public DataSource postgresDataSource() {
        return DataSourceBuilder.create().build();
    }

    // DataSource для ClickHouse
    @Bean("clickHouseDataSource")
    @ConfigurationProperties(prefix = "clickhouse")
    public DataSource clickHouseDataSource() {
        return DataSourceBuilder.create().build();
    }

    // JdbcTemplate для ClickHouse
    @Bean("clickHouseJdbcTemplate")
    public JdbcTemplate clickHouseJdbcTemplate() {
        return new JdbcTemplate(clickHouseDataSource());
    }
}