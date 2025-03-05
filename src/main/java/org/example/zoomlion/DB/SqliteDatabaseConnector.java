package org.example.zoomlion.DB;

import org.example.zoomlion.Utils.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class SqliteDatabaseConnector {
    private static String URL = "jdbc:sqlite:";

    static {
        URL += Objects.requireNonNull(SqliteDatabaseConnector.class.getResource(Constants.PROJECT_PATH + "zoomlion_data.db")).toExternalForm();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}


