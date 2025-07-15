package com.pijieh.personalsite.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DatabaseService {
    private static Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    ComboPooledDataSource dataSource;

    public DatabaseService(String driverClass, String dbUrl, String username, String password,
            int minPoolSize, int maxPoolSize) throws PropertyVetoException {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("org.postgresql.Driver");
        cpds.setJdbcUrl(dbUrl);
        cpds.setUser(username);
        cpds.setPassword(password);
        cpds.setMinPoolSize(minPoolSize);
        cpds.setMaxPoolSize(maxPoolSize);
        logger.info("Successfully created database datasource.");
        dataSource = cpds;
        testConnection();
    }

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
            logger.error("DB test connection failed:", ex);
        }
    }
}
