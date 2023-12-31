package comparator.equalDataBases;

import comparator.DBComparator;
import loader.Loader;
import loader.MyLoader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import structure.Schema;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static utils.Utils.*;

public class EqualDatabasesTest {

    static DBComparator comparator;
    static Connection connection1;
    static Connection connection2;


    @BeforeAll
    public static void setUp() {
        try {
            String configFilename = "src/test/java/comparator/config.json";
            Connection connection = getConnection(configFilename);
            initializeDatabase(connection, "schema1");
            initializeDatabase(connection, "schema2");

            connection1 = getConnection(configFilename, "schema1");
            connection2 = getConnection(configFilename, "schema2");

            String path = "src/test/java/comparator/equalDataBases";

            runScript(connection1, path + "/schema.sql");
            runScript(connection1, path + "/trigger.sql", true);
            runScript(connection1, path + "/proc.sql", true);

            runScript(connection2, path + "/schema.sql");
            runScript(connection2, path + "/trigger.sql", true);
            runScript(connection2, path + "/proc.sql", true);

            Loader loader1 = new MyLoader(connection1, "schema1");
            Loader loader2 = new MyLoader(connection2, "schema2");

            Schema schema1 = loader1.loadSchema();
            Schema schema2 = loader2.loadSchema();
            comparator = DBComparator.of(schema1, schema2);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void tearDown() {
        try {
            finalizeDatabase(connection1, "schema1");
            finalizeDatabase(connection1, "schema2");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testEqualDatabases() {
        assert comparator.equal();
    }
}
