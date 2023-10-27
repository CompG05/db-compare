package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Utils {
    public static Connection getConnection(String filename) throws IOException, ClassNotFoundException, SQLException {
        JsonNode jsonNode = loadConfig(filename);

        String url = jsonNode.get("url").asText();
        String username = jsonNode.get("username").asText();
        String password = jsonNode.get("password").asText();

        String fullUrl = "jdbc:mysql://" + url + "/";

        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(fullUrl, username, password);
    }

    public static JsonNode loadConfig(String filename) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(new FileReader(filename));
    }

    public static void initializeDatabase(Connection connection, String databaseName) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("drop database if exists %s", databaseName));
        statement.execute(String.format("create database %s", databaseName));
        statement.execute(String.format("use %s", databaseName));
    }

    public static void finalizeDatabase(Connection connection, String databaseName) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("drop database if exists %s", databaseName));
    }
}
