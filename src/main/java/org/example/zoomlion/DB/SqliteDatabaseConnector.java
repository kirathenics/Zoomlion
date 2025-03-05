package org.example.zoomlion.DB;

import org.example.zoomlion.Utils.Constants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

//public class SqliteDatabaseConnector {
//    private static String URL = "jdbc:sqlite:";
//
//    static {
//        URL += Objects.requireNonNull(SqliteDatabaseConnector.class.getResource(Constants.PROJECT_PATH + "zoomlion_data.db")).toExternalForm();
//    }
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(URL);
//    }
//}

public class SqliteDatabaseConnector {
    private static String DB_FILE_NAME = "zoomlion_data.db"; // Название файла БД
    private static String DB_PATH = System.getProperty("user.home") + File.separator + DB_FILE_NAME; // Где будет храниться база
    private static String URL = "jdbc:sqlite:" + DB_PATH;

    static {
        extractDatabase(); // Копируем базу, если её нет
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

