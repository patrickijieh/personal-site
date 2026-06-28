package com.pijieh.personalsite;

import com.pijieh.personalsite.database.DatabaseService;
import com.pijieh.personalsite.helpers.ResourceFinder;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import redis.clients.jedis.RedisClient;

/**
 * The main entrypoint of the application.
 *
 * @author patrickijieh
 */
@SpringBootApplication
public class PersonalsiteApplication {
    private static final Logger logger = LoggerFactory.getLogger(PersonalsiteApplication.class);

    @Value("${database.min-pool-size}")
    private int minPoolSize;

    @Value("${database.max-pool-size}")
    private int maxPoolSize;

    @Value("${database.url}")
    private String databaseUrl;

    @Value("${database.username}")
    private String databaseUsername;

    @Value("${database.password}")
    private String databasePassword;

    /**
     * Main entrypoint.
     */
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
    Git gitRepository() throws IOException {
        return Git.open(new File("./"));
    }

    @Bean
    RedisClient redisClient() {
        return RedisClient.create("localhost", 6379);
    }

    @Bean
    DatabaseService dataSource() throws PropertyVetoException, IOException, SQLException {
        return new DatabaseService("org.postgresql.Driver", databaseUrl, databaseUsername,
                databasePassword, minPoolSize, maxPoolSize);
    }
}
