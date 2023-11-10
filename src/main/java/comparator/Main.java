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
    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("--help") || args[0].equals("-h")) {
            System.out.println("Usage: java -jar <this jar> <config file> [-o <report file>]");
            return;
        }

        if (args.length != 1) {
            if (args.length != 3 || args.length == 3 && !args[1].equals("-o")) {
                System.err.println("Usage: java -jar <this jar> <config file> [-o <report file>]");
                return;
            }
        }
        String configFilename = args[0];
        String outputFilename = null;
        if (args.length == 3)
            outputFilename = args[2];

        DBComparator comparator;
        try {
            JsonNode json = Utils.loadConfig(configFilename);
            Connection connection1 = Utils.getConnection(configFilename, json.get("catalog1").asText());
            Connection connection2 = Utils.getConnection(configFilename, json.get("catalog2").asText());
            Loader loader1 = new MyLoader(connection1, connection1.getCatalog());
            Loader loader2 = new MyLoader(connection2, connection2.getCatalog());

            Schema schema1 = loader1.loadSchema();
            Schema schema2 = loader2.loadSchema();

            comparator = DBComparator.of(schema1, schema2);
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Reporter reporter = new Reporter(comparator);
        if (outputFilename != null)
            reporter.report(outputFilename);
        else
            reporter.report();
    }
}
