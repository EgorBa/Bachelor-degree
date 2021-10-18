package ru.bazhenov.sd.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.bazhenov.sd.mvc.dao.ListJdbcDao;

import javax.sql.DataSource;

@Configuration
public class JdbcDaoContextConfiguration {
    @Bean
    public ListJdbcDao listJdbcDao(DataSource dataSource) {
        return new ListJdbcDao(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:tasks.db");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }
}
