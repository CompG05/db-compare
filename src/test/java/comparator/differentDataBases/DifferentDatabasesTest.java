package comparator.differentDataBases;

import comparator.DBComparator;
import javafx.util.Pair;
import loader.Loader;
import loader.MyLoader;
import structure.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Utils.*;
import static utils.Utils.getConnection;

public class DifferentDatabasesTest {
    static DBComparator comparator;
    static Connection connection1;
    static Connection connection2;

    @BeforeAll
    public static void setUp() {
        try {
            String configFilename = "src/test/java/comparator/config.json";
            Connection connection = getConnection(configFilename);
            initializeDatabase(connection, "schema1");
            runScript(connection, "src/test/java/comparator/differentDataBases/schema1.sql");
            initializeDatabase(connection, "schema2");
            runScript(connection, "src/test/java/comparator/differentDataBases/schema2.sql");

            connection1 = getConnection(configFilename, "schema1");
            connection2 = getConnection(configFilename, "schema2");

            Loader loader1 = new MyLoader(connection1, "schema1");
            Loader loader2 = new MyLoader(connection2, "schema2");

            Schema schema1 = loader1.loadSchema();
            Schema schema2 = loader2.loadSchema();
            comparator = DBComparator.of(schema1, schema2);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUniqueTables() {
        Pair<Set<String>, Set<String>> expected = new Pair<>(
                new HashSet<>(Collections.singletonList("uniquetable1")),
                new HashSet<>(Arrays.asList("uniquetable2", "uniquetable3"))
        );

        assertEquals(expected, comparator.getUniqueTables());
    }

    @Test
    public void testColumns() {
        String tableName = "differentcolumns";
        Table table1 = new Table(tableName);
        Table table2 = new Table(tableName);

        table1.addColumns(new HashSet<>(Arrays.asList(
                new Column("attr2", "INT", "10", true),
                new Column("attr3", "INT", "10", true),
                new Column("attr4", "VARCHAR", "20", true),
                new Column("attr5", "INT", "10", false))));
        ;

        table2.addColumns(new HashSet<>(Arrays.asList(
                new Column("attrX", "INT", "10", true),
                new Column("attr3", "DECIMAL", "10", true),
                new Column("attr4", "VARCHAR", "30", true),
                new Column("attr5", "INT", "10", true))));

        Pair<Table, Table> expected = new Pair<>(table1, table2);
        Pair<Table, Table> actual = comparator.getCommonTablesDiffs().stream()
                .filter(p -> p.getKey().getName().equals(tableName)).findFirst().get();

        assertEquals(expected, actual);
    }

    @Test
    public void testPrimaryKeys() {
        Table table1 = new Table("differentpks");
        Table table2 = new Table("differentpks");
        table1.addPrimaryKey(Arrays.asList(
                new OrderedColumn("attr1",1),
                new OrderedColumn("attr2",2)
                ));
        table1.addIndex(new HashSet<>(Arrays.asList(
                new OrderedColumn("attr1",1),
                new OrderedColumn("attr2",2)
        )));

        table2.addPrimaryKey(Arrays.asList(
                new OrderedColumn("attr1",1),
                new OrderedColumn("attr3",2)
        ));
        table2.addIndex(new HashSet<>(Arrays.asList(
                new OrderedColumn("attr1",1),
                new OrderedColumn("attr3",2)
        )));

        Pair<Table, Table> expected = new Pair<>(table1, table2);

        Pair<Table, Table> actual = comparator.getCommonTablesDiffs().stream().filter((p) -> p.getKey().getName().equals("differentpks")).findFirst().get();

        assertEquals(expected, actual);
    }

    @Test
    public void testForeignKeys() {
        String tableName = "differentfks";
        Table table1 = new Table(tableName);
        Table table2 = new Table(tableName);

        table1.addForeignKey(new ForeignKey(
                new HashSet<>(Arrays.asList(new OrderedColumn("a", 1), new OrderedColumn("b", 2))),
                "importedtable",
                new HashSet<>(Arrays.asList(new OrderedColumn("a", 1), new OrderedColumn("b", 2)))));
        table1.addIndex(new HashSet<>(Arrays.asList(new OrderedColumn("a", 1), new OrderedColumn("b", 2))));

        table2.addForeignKey(new ForeignKey(
                new HashSet<>(Arrays.asList(new OrderedColumn("b", 1), new OrderedColumn("c", 2))),
                "importedtable",
                new HashSet<>(Arrays.asList(new OrderedColumn("b", 1), new OrderedColumn("c", 2)))));
        table2.addIndex(new HashSet<>(Arrays.asList(new OrderedColumn("b", 1), new OrderedColumn("c", 2))));

        Pair<Table, Table> expected = new Pair<>(table1, table2);
        Pair<Table, Table> actual = comparator.getCommonTablesDiffs().stream()
                .filter(p -> p.getKey().getName().equals(tableName)).findFirst().get();

        assertEquals(expected, actual);
    }
}
