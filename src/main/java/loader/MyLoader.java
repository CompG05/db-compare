package loader;

import structure.*;

import java.sql.*;
import java.util.*;

/**
 * MyLoader class implementing a Loader for MySQL databases
 */
public class MyLoader extends Loader {
    public MyLoader(String url, String catalog, String username, String password) throws SQLException {
        driver = "com.mysql.cj.jdbc.Driver";
        this.catalog = catalog;
        String fullURL = "jdbc:mysql://" + url + "/" + catalog;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.connection = DriverManager.getConnection(fullURL, username, password);
    }

    public MyLoader(Connection connection, String schema) {
        this.connection = connection;
        this.catalog = schema;
    }

    @Override
    Set<Trigger> loadTriggers(DatabaseMetaData metaData, String tableName) throws SQLException {
        Set<Trigger> triggerSet = new HashSet<>();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT TRIGGER_NAME, ACTION_TIMING, EVENT_MANIPULATION " +
                        "FROM information_schema.TRIGGERS " +
                        "WHERE TRIGGER_SCHEMA=? AND EVENT_OBJECT_TABLE=?"
        );
        statement.setString(1, catalog);
        statement.setString(2, tableName);
        ResultSet triggers = statement.executeQuery();

        while(triggers.next()) {
            triggerSet.add(new Trigger(
                    triggers.getString("TRIGGER_NAME"),
                    triggers.getString("ACTION_TIMING"),
                    triggers.getString("EVENT_MANIPULATION")
            ));
        }

        return triggerSet;
    }
}
