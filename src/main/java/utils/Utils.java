package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Utils {
    public static Connection getConnection(String filename, String catalog) throws IOException, ClassNotFoundException, SQLException {
        JsonNode jsonNode = loadConfig(filename);

        String url = jsonNode.get("url").asText();
        String username = jsonNode.get("username").asText();
        String password = jsonNode.get("password").asText();

        String fullUrl = "jdbc:mysql://" + url + "/"  + catalog;

        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(fullUrl, username, password);
    }

    public static Connection getConnection(String filename) throws IOException, ClassNotFoundException, SQLException {
        return getConnection(filename, "");
    }

    public static void runScript(Connection connection, String filename) throws FileNotFoundException {
        runScript(connection, filename, false);
    }

    public static void runScript(Connection connection, String filename, boolean sendFullScript) throws FileNotFoundException {
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setSendFullScript(sendFullScript);
        scriptRunner.runScript(new FileReader(filename));
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
