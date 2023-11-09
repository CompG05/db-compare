package comparator;

import com.fasterxml.jackson.databind.JsonNode;
import comparator.reporter.Reporter;
import loader.Loader;
import loader.MyLoader;
import structure.Schema;
import utils.Utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        DBComparator comparator;
        try {
            JsonNode json = Utils.loadConfig("config.json");
            Connection connection1 = Utils.getConnection("config.json", json.get("catalog1").asText());
            Connection connection2 = Utils.getConnection("config.json", json.get("catalog2").asText());
            Loader loader1 = new MyLoader(connection1, connection1.getCatalog());
            Loader loader2 = new MyLoader(connection2, connection2.getCatalog());

            Schema schema1 = loader1.loadSchema();
            Schema schema2 = loader2.loadSchema();

            comparator = DBComparator.of(schema1, schema2);
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Reporter reporter = new Reporter(comparator);
        reporter.report();
    }
}
