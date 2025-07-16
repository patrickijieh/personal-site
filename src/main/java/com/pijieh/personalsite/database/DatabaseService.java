package com.pijieh.personalsite.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper class for the ComboPooledDataSource class for logging purposes.
 *
 * @author patrickijieh
 */
public final class DatabaseService {
    private static Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    ComboPooledDataSource dataSource;

    /**
     * Constructs the new DatabaseService class.
     *
     * @param driverClass the JDBC driver class
     * @param dbUrl       the database url and port
     * @param username    the psql user
     * @param password    the psql password
     * @param minPoolSize the minimum connection pool size
     * @param maxPoolSize the maximum connection pool size
     */
    public DatabaseService(String driverClass, String dbUrl, String username, String password,
            int minPoolSize, int maxPoolSize) throws PropertyVetoException {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass(driverClass);
        cpds.setJdbcUrl(dbUrl);
        cpds.setUser(username);
        cpds.setPassword(password);
        cpds.setMinPoolSize(minPoolSize);
        cpds.setMaxPoolSize(maxPoolSize);
        logger.info("Successfully created database datasource.");
        dataSource = cpds;
        testConnection();
    }

    /**
     * Creates a new database connection.
     *
     * @returns a new connection
     * @throws SQLException if connection cannot be established
     */
    public Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        return conn;
    }

    private void testConnection() {
        try (Connection conn = getConnection()) {
            DSL.using(conn, SQLDialect.POSTGRES).query(
                    "select {0} from {1} limit 0",
                    DSL.name("id"), DSL.name("posts"))
                    .execute();
        } catch (SQLException ex) {
            logger.error("""
                    DB test connection failed.
                        Make sure that the database environment variables are set correctly.
                        \nError:
                        """,
                    ex);
        }
    }
}
