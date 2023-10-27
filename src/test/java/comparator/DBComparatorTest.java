package comparator;

import com.fasterxml.jackson.databind.JsonNode;
import loader.Loader;
import loader.MyLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static utils.Utils.getConnection;
import static utils.Utils.loadConfig;

public class DBComparatorTest {
    @Test
    public void testEqualDatabases() {
        try {
            String configFilename = "src/test/java/comparator/config.json";
            JsonNode config = loadConfig(configFilename);
            Connection connection = getConnection(configFilename);
            Loader loader1 = new MyLoader(connection, "schema1");
            Loader loader2 = new MyLoader(connection, "schema2");

            DBComparator comparator = DBComparator.of(loader1.loadSchema(), loader2.loadSchema());
            System.out.println("Chau");


        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
