package org.example.zoomlion.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/zoomlion_data";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

//public class MySQLDatabaseConnector {
//    private static final String CONFIG_FILE = "C:\\Users\\bka32\\GitHub-repos\\Zoomlion\\src\\main\\resources\\org\\example\\zoomlion\\settings\\config.properties";
//    private static final String url;
//    private static final String user;
//    private static final String password;
//
//    static {
//        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
//            Properties properties = new Properties();
//            properties.load(fis);
//            url = properties.getProperty("db.url");
//            user = properties.getProperty("db.user");
//            password = properties.getProperty("db.password");
//        } catch (IOException e) {
//            throw new RuntimeException("Ошибка загрузки конфигурации базы данных", e);
//        }
//    }
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(url, user, password);
//    }
//}

//public class MySQLDatabaseConnector {
//    private static final String CONFIG_FILE = Constants.PROJECT_PATH + "settings/config.properties";
////    private static final String CONFIG_FILE = "settings/config.properties";
//    private static final String url;
//    private static final String user;
//    private static final String password;
//
//    static {
//        Properties properties = new Properties();
//        try (InputStream input = DatabaseConnector.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
//            if (input == null) {
//                throw new IOException("Файл конфигурации не найден: " + CONFIG_FILE);
//            }
//            properties.load(input);
//            url = properties.getProperty("db.url");
//            user = properties.getProperty("db.user");
//            password = properties.getProperty("db.password");
//        } catch (IOException e) {
//            throw new RuntimeException("Ошибка загрузки конфигурации базы данных", e);
//        }
//    }
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(url, user, password);
//    }
//}
