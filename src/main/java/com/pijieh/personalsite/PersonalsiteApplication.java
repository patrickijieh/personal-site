package com.pijieh.personalsite;

import com.pijieh.personalsite.database.DatabaseService;
import com.pijieh.personalsite.helpers.ResourceFinder;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * The main entrypoint of the application.
 *
 * @author patrickijieh
 */
@SpringBootApplication
public class PersonalsiteApplication {
    private static final Logger logger = LoggerFactory.getLogger(PersonalsiteApplication.class);

    @Value("${database.min-pool-size}")
    private int MIN_POOL_SIZE;

    @Value("${database.max-pool-size}")
    private int MAX_POOL_SIZE;

    @Value("${database.url}")
    private String DATABASE_URL;

    @Value("${database.username}")
    private String DATABASE_USERNAME;

    @Value("${database.password}")
    private String DATABASE_PASSWORD;

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
    DatabaseService dataSource() throws PropertyVetoException, IOException, SQLException {
        return new DatabaseService("org.postgresql.Driver", DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD,
                MIN_POOL_SIZE, MAX_POOL_SIZE);
    }
}
