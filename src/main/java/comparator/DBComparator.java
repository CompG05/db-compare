package comparator;

import utils.Pair;
import structure.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DBComparator class
 * Compare two schemas and store the differences in two Schema objects
 * The differences are:
 * - Tables that are in one schema but not in the other
 * - Tables that are in both schemas but have different elements
 * - Procedures that are in one schema but not in the other
 * - Procedures that are in both schemas but have different elements
 */
public class DBComparator {
    Schema schema1;
    Schema schema2;
    Set<String> tableNames1;
    Set<String> tableNames2;
    Set<Pair<Table, Table>> commonTablesDiffs;
    Set<Procedure> procedures1;
    Set<Procedure> procedures2;
    Set<Pair<Procedure, Procedure>> commonProceduresDiffs;

    private DBComparator(Schema schema1, Schema schema2,
                         Set<String> tableNames1, Set<String> tableNames2,
                         Set<Pair<Table, Table>> commonTablesDiffs,
                         Set<Procedure> procedures1, Set<Procedure> procedures2,
                         Set<Pair<Procedure, Procedure>> commonProceduresDiffs) {
        this.schema1 = schema1;
        this.schema2 = schema2;
        this.tableNames1 = tableNames1;
        this.tableNames2 = tableNames2;
        this.commonTablesDiffs = commonTablesDiffs;
        this.procedures1 = procedures1;
        this.procedures2 = procedures2;
        this.commonProceduresDiffs = commonProceduresDiffs;
    }

    /**
     * Compare two schemas and return a DBComparator object
     * @param s1 first schema
     * @param s2 second schema
     * @return a DBComparator object with the differences between the two schemas
     */
    public static DBComparator of(Schema s1, Schema s2) {
        Set<String> tableNames1 = getUniqueTables(s1, s2);
        Set<String> tableNames2 = getUniqueTables(s2, s1);
        Set<Pair<Table, Table>> commonTablesDiffs = getCommonTablesDiffs(s1, s2);
        Set<Procedure> procedures1 = getUniqueProcedures(s1, s2);
        Set<Procedure> procedures2 = getUniqueProcedures(s2, s1);
        Set<Pair<Procedure, Procedure>> commonProceduresDiffs = getCommonProceduresDiffs(s1, s2);

        return new DBComparator(
                s1, s2,
                tableNames1, tableNames2,
                commonTablesDiffs,
                procedures1, procedures2,
                commonProceduresDiffs
        );
    }

    public Schema getSchema1() {
        return schema1;
    }

    public Schema getSchema2() {
        return schema2;
    }

    public Pair<Set<String>, Set<String>> getUniqueTables() {
        return new Pair<>(new HashSet<>(tableNames1), new HashSet<>(tableNames2));
    }

    public Set<Pair<Table, Table>> getCommonTablesDiffs() {
        return new HashSet<>(commonTablesDiffs);
    }

    public Pair<Set<Procedure>, Set<Procedure>> getUniqueProcedures() {
        return new Pair<>(new HashSet<>(procedures1), new HashSet<>(procedures2));
    }

    public Set<Pair<Procedure, Procedure>> getCommonProceduresDiffs() {
        return new HashSet<>(commonProceduresDiffs);
    }

    /**
     * Remove from the first schema the tables that are also in the second one.
     * @param s1 first schema
     * @param s2 second schema
     * @return returns a set of unique table names from schema s1
     */
    private static Set<String> getUniqueTables(Schema s1, Schema s2) {
        Set<String> s1TableNames = s1.getTables().stream().map(Table::getName).collect(Collectors.toSet());
        Set<String> s2TableNames = s2.getTables().stream().map(Table::getName).collect(Collectors.toSet());

        s1TableNames.removeAll(s2TableNames);
        return s1TableNames;
    }

    /**
      * Compare the tables that are in both schemas and return a set of pairs of tables that have differences
     * @param s1 first schema
     * @param s2 second schema
     * @return pairs of tables with their differences
     */
    private static Set<Pair<Table, Table>> getCommonTablesDiffs(Schema s1, Schema s2) {
        Set<Pair<Table, Table>> commonTablesDiffs = new HashSet<>();

        for (Table table : s1.getTables()) {
            Optional<Table> other = s2.getTable(table.getName());

            other.ifPresent(otherTable -> {
                Optional<Pair<Table, Table>> pair = compareTables(table, otherTable);
                pair.ifPresent(commonTablesDiffs::add);
            });
        }

        return commonTablesDiffs;
    }

    /**
     * Compare two tables and return a pair of tables with their differences if they have any
     * @param table first table
     * @param otherTable second table
     * @precondition: table.name = otherTable.name
     * @return a pair of tables with their differences if they have any, empty otherwise
     */
    private static Optional<Pair<Table, Table>> compareTables(Table table, Table otherTable) {
        Table table1 = new Table(table.getName());
        Table table2 = new Table(table.getName());
        Pair<Table, Table> pair = new Pair<>(table1, table2);
        boolean change = false;


        //Columns
        Set<Column> columns1 = table.getColumns();
        Set<Column> columns2 = otherTable.getColumns();

        if (!columns1.equals(columns2)) {
            change = true;
            columns1.removeAll(columns2);
            table1.addColumns(columns1);

            columns1 = table.getColumns();
            columns2.removeAll(columns1);
            table2.addColumns(columns2);
        }

        // Primary keys
        Set<OrderedColumn> primaryKey1 = table.getPrimaryKey();
        Set<OrderedColumn> primaryKey2 = otherTable.getPrimaryKey();

        if (!primaryKey1.equals(primaryKey2)) {
            change = true;
            table1.addPrimaryKey(primaryKey1);
            table2.addPrimaryKey(primaryKey2);
        }

        //Indices
        Set<Set<OrderedColumn>> indices1 = table.getIndices();
        Set<Set<OrderedColumn>> indices2 = otherTable.getIndices();
        if (!indices1.equals(indices2)) {
            change = true;
            indices1.removeAll(indices2);
            table1.addIndices(indices1);

            indices1 = table.getIndices();
            indices2.removeAll(indices1);
            table2.addIndices(indices2);
        }

        //Foreign keys
        Set<ForeignKey> fk1 = table.getForeignKeys();
        Set<ForeignKey> fk2 = otherTable.getForeignKeys();
        if (!fk1.equals(fk2)) {
            change = true;
            fk1.removeAll(fk2);
            table1.addForeignKeys(fk1);

            fk1 = table.getForeignKeys();
            fk2.removeAll(fk1);
            table2.addForeignKeys(fk2);
        }


        //Triggers
        Set<Trigger> triggers1 = table.getTriggers();
        Set<Trigger> triggers2 = otherTable.getTriggers();
        if (!triggers1.equals(triggers2)) {
            change = true;
            triggers1.removeAll(triggers2);
            table1.addTriggers(triggers1);

            triggers1 = table.getTriggers();
            triggers2.removeAll(triggers1);
            table2.addTriggers(triggers2);
        }

        if (change) return Optional.of(pair);
        else return Optional.empty();
    }

    /**
     * Remove from the first schema the procedures that are also in the second one.
     * @param s1 first schema
     * @param s2 second schema
     * @return returns a set of unique procedures from schema s1
     */
    private static Set<Procedure> getUniqueProcedures(Schema s1, Schema s2) {
        Set<String> s2ProcedureNames = s2.getProcedures().stream().map(Procedure::getName).collect(Collectors.toSet());

        return s1.getProcedures().stream().filter(p -> !s2ProcedureNames.contains(p.getName())).collect(Collectors.toSet());
    }

    /**
     * Compare the procedures that are in both schemas and return a set of pairs of procedures that have differences
     * @param s1 first schema
     * @param s2 second schema
     * @return pairs of procedures with their differences
     */
    private static Set<Pair<Procedure, Procedure>> getCommonProceduresDiffs(Schema s1, Schema s2) {
        Set<Pair<Procedure, Procedure>> commonProceduresDiffs = new HashSet<>();

        for (Procedure proc : s1.getProcedures()) {
            Optional<Procedure> otherProc = s2.getProcedure(proc.getName());

            otherProc.ifPresent(otherProcedure -> {
                Optional<Pair<Procedure, Procedure>> pair = compareProcedures(proc, otherProcedure);
                pair.ifPresent(commonProceduresDiffs::add);
            });
        }

        return commonProceduresDiffs;
    }

    /**
     * Compare two procedures and return a pair of procedures with their differences if they have any
     * @param proc first procedure
     * @param otherProc second procedure
     * @precondition: proc.name = otherProc.name
     * @return a pair of procedures with their differences if they have any, empty otherwise
     */
    private static Optional<Pair<Procedure, Procedure>> compareProcedures(Procedure proc, Procedure otherProc) {
        Procedure proc1 = new Procedure(proc.getName());
        Procedure proc2 = new Procedure(proc.getName());
        Pair<Procedure, Procedure> pair = new Pair<>(proc1, proc2);
        boolean change = false;

        //Arguments
        Set<Argument> args1 = proc.getArguments();
        Set<Argument> args2 = otherProc.getArguments();

        if (!args1.equals(args2)) {
            change = true;
            proc1.addArguments(args1);
            proc2.addArguments(args2);
        }

        if (change) return Optional.of(pair);
        else return Optional.empty();
    }

    /**
     * Check if the two schemas are equal
     * @return true if the schemas are equal, false otherwise
     */
    public boolean equal() {
        return tableNames1.isEmpty() && tableNames2.isEmpty()
                && commonTablesDiffs.isEmpty()
                && procedures1.isEmpty() && procedures2.isEmpty()
                && commonProceduresDiffs.isEmpty();
    }
}
