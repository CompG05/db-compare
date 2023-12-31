package loader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import structure.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static structure.ArgumentType.*;
import static utils.Utils.*;

public class LoaderTest {
    static String catalog;
    static Loader loader;
    static Schema schema;
    static Set<Table> tables;
    static Connection connection;

    @BeforeAll
    public static void setUp() {
        String configFileName = "src/test/java/loader/config.json";
        try {
            // Read config file and set up connection
            JsonNode config = loadConfig(configFileName);
            catalog = config.get("catalog").asText();

            connection = getConnection(configFileName);
            initializeDatabase(connection, catalog);
            connection = getConnection(configFileName, catalog);

            // Run initialization script
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.runScript(new FileReader(System.getProperty("user.dir") + "/src/test/java/loader/test-script.sql"));

            scriptRunner.setSendFullScript(true);
            scriptRunner.runScript(new FileReader(System.getProperty("user.dir") + "/src/test/java/loader/trigger.sql"));
            scriptRunner.runScript(new FileReader(System.getProperty("user.dir") + "/src/test/java/loader/procedure.sql"));
            scriptRunner.runScript(new FileReader(System.getProperty("user.dir") + "/src/test/java/loader/function.sql"));

            loader = new MyLoader(connection, catalog);
            schema = loader.loadSchema();
            tables = schema.getTables();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void tearDown() {
        try {
            finalizeDatabase(connection, "db_compare_test");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // @Test
    public void test01() {
        System.out.println(schema);
    }

    @Test
    public void tablesTest() {
        Set<String> actualTableNames = tables.stream().map(Table::getName).collect(Collectors.toSet());
        Set<String> expectedTableNames = new HashSet<>(Arrays.asList(
                "example", "foreign_example", "table_with_indices", "imported_table", "table_with_foreign_keys"
        ));
        assertEquals(expectedTableNames, actualTableNames);
    }

    @Test
    public void columnsTest() {
        Table exampleTable = findTable("example");
        Set<Column> actualColumns = exampleTable.getColumns();
        Set<Column> expectedColumns = new HashSet<>(Arrays.asList(
                new Column("attr1", "INT", "10", false),
                new Column("attr6", "INT", "10", false),
                new Column("attr2", "VARCHAR", "20", false),
                new Column("attr3", "VARCHAR", "20", true)
        ));
        assertEquals(expectedColumns, actualColumns);
    }

    @Test
    public void primaryKeysTest() {
        Table exampleTable = findTable("example");
        Set<OrderedColumn> actualPks = exampleTable.getPrimaryKey();
        Set<OrderedColumn> expectedPks = new HashSet<>(Arrays.asList(
                new OrderedColumn("attr1", 1),
                new OrderedColumn("attr6", 2)
        ));
        
        assertEquals(expectedPks, actualPks);
    }

    @Test
    public void indicesTest() {
        Table tableWithIndices = findTable("table_with_indices");
        Set<Set<OrderedColumn>> actualIndices = tableWithIndices.getIndices();
        Set<Set<OrderedColumn>> expectedIndices = new HashSet<>(Arrays.asList(
                new HashSet<>(Arrays.asList(
                        new OrderedColumn("attr1", 1),
                        new OrderedColumn("attr2", 2)
                )),
                new HashSet<>(Arrays.asList(
                        new OrderedColumn("attr3", 2),
                        new OrderedColumn("attr4", 1)
                ))
        ));
        
        assertEquals(expectedIndices, actualIndices);
    }
    
    @Test
    public void foreignKeysTest() {
        Table tableWithForeignKeys = findTable("table_with_foreign_keys");
        Set<ForeignKey> actualFks = tableWithForeignKeys.getForeignKeys();
        Set<ForeignKey> expectedFks = new HashSet<>(Arrays.asList(
                new ForeignKey(
                        new HashSet<>(Arrays.asList(
                                new OrderedColumn("attr3", 1),
                                new OrderedColumn("attr4", 2)
                        )),
                        "imported_table",
                        new HashSet<>(Arrays.asList(
                                new OrderedColumn("attr1", 1),
                                new OrderedColumn("attr2", 2)
                        ))
                )
        ));
        
        assertEquals(expectedFks, actualFks);
    }

    @Test
    public void triggersTest() {
        Table exampleTable = findTable("example");
        Set<Trigger> actualTriggers = exampleTable.getTriggers();
        Set<Trigger> expectedTriggers = new HashSet<>(Arrays.asList(
                new Trigger("example_trigger", "BEFORE", "INSERT")
        ));

        assertEquals(expectedTriggers, actualTriggers);
    }

    @Test
    public void proceduresTest() {
        Set<Procedure> actualProcedures = schema.getProcedures();
        Set<Procedure> expectedProcedure = new HashSet<>(Arrays.asList(
           new Procedure("example_procedure",
                   new HashSet<>(Arrays.asList(
                           new Argument("arg1", 1, IN, "INT"),
                           new Argument("arg2", 2, INOUT, "INT"),
                           new Argument("arg3", 3, OUT, "INT")
                           ))
           ),
            new Procedure("example_function",
                    new HashSet<>(Arrays.asList(
                            new Argument("arg1", 1, IN, "INT"),
                            new Argument("arg2", 2, IN, "INT"),
                            new Argument("", 0, RETURN, "INT")
                    ))
            )
        ));

        assertEquals(expectedProcedure, actualProcedures);
    }

    private Table findTable(String tableName) {
        Optional<Table> table = tables.stream().filter(t -> t.getName().equals(tableName)).findFirst();
        if (table.isPresent()) {
            return table.get();
        } else {
            throw new RuntimeException("Table not found");
        }
    }
}