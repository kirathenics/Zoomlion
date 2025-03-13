package org.example.zoomlion.DB.DatabaseConnectors;

import org.example.zoomlion.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteDatabaseConnector {
    private static final String DB_FILE_NAME = "zoomlion_data.db";
    private static final String DB_PATH = System.getProperty("user.home") +
        File.separator +
        "zoomlion" +
        File.separator +
        DB_FILE_NAME;
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    static {
        extractDatabase();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private static void extractDatabase() {
        File dbFile = new File(DB_PATH);
        if (!dbFile.exists()) {
            try (InputStream is = SqliteDatabaseConnector.class.getResourceAsStream( Constants.PROJECT_PATH + "zoomlion_data.db")) {
                if (is == null) {
                    throw new RuntimeException("Файл базы данных не найден в ресурсах!");
                }
                Files.copy(is, dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("База данных скопирована в " + DB_PATH);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка копирования базы данных", e);
            }
        }
    }
}
