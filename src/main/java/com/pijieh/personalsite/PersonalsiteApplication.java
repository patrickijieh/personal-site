package com.pijieh.personalsite;

import com.pijieh.personalsite.database.DatabaseService;
import com.pijieh.personalsite.helpers.ResourceFinder;
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

/**
 * The main entrypoint of the application.
 *
 * @author patrickijieh
 */
@SpringBootApplication
public class PersonalsiteApplication {
    private static final Logger logger = LoggerFactory.getLogger(PersonalsiteApplication.class);

    // TODO: Make these application properties
    private static final int MIN_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 10;

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
        return new DatabaseService("org.postgresql.Driver", dbUrl(), dbUsername(), dbPassword(),
                MIN_POOL_SIZE, MAX_POOL_SIZE);
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
