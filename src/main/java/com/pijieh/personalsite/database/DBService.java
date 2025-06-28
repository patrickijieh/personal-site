package com.pijieh.personalsite.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBService {
    private static Logger logger = LoggerFactory.getLogger(DBService.class);
    ComboPooledDataSource dataSource;

    public DBService(ComboPooledDataSource cpds) {
        this.dataSource = cpds;
        testConnection();
    }

    public Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        return conn;
    }

    private void testConnection() {
        try (Connection conn = dataSource.getConnection()) {
            DSL.using(conn, SQLDialect.POSTGRES).query("select {0} from {1} limit 0", DSL.name("id"), DSL.name("posts"))
                    .execute();
        } catch (SQLException ex) {
            logger.error("DB test connection failed:", ex);
        }
    }
}
