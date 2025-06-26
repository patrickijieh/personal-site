package com.pijieh.personalsite;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.pijieh.personalsite.database.DBService;
import com.pijieh.personalsite.helpers.ResourceFinder;

@SpringBootApplication
public class PersonalsiteApplication {
    private static final Logger logger = LoggerFactory.getLogger(PersonalsiteApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PersonalsiteApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder bcrypt() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ResourceFinder rsFinder() {
        return new ResourceFinder();
    }

    @Bean
    DBService dataSource() throws PropertyVetoException, IOException, SQLException {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("org.postgresql.Driver");
        cpds.setJdbcUrl(dbUrl());
        cpds.setUser(dbUsername());
        cpds.setPassword(dbPassword());
        cpds.setMinPoolSize(3);
        cpds.setMaxPoolSize(10);
        logger.info("Successfully created database datasource.");
        return new DBService(cpds);
    }

    String dbUrl() throws IOException {
        return getRsFinder().getEnvironmentVariable("DB_URL");
    }

    String dbUsername() throws IOException {
        return getRsFinder().getEnvironmentVariable("DB_USERNAME");
    }

    String dbPassword() throws IOException {
        return getRsFinder().getEnvironmentVariable("DB_PASSWORD");
    }

    @Lookup
    ResourceFinder getRsFinder() {
        return null;
    }
}
