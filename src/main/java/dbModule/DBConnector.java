package dbModule;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class DBConnector {

    private static final String CONFIG_FILE = "dbModule/config.properties";

    public static Connection getConnection() throws SQLException {
        Properties properties = loadConfig();

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    private static Properties loadConfig() {
        Properties properties = new Properties();

        try (InputStream input = DBConnector.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Configuration file not found in classpath: " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration file", e);
        }

        return properties;
    }
}
