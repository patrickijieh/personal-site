package com.pijieh.personalsite.database;

import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBService {
    private static Logger logger = LoggerFactory.getLogger(DBService.class);
    ComboPooledDataSource dataSource;

    public DBService(ComboPooledDataSource cpds) {
        this.dataSource = cpds;
    }

    public Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        logger.info(String.format("Spawned new database connection. Total connections: %d, Idle connections: %d",
                dataSource.getNumConnectionsAllUsers(),
                dataSource.getNumIdleConnectionsAllUsers()));
        return conn;
    }
}
