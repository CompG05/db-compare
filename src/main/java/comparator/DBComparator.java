package comparator;

import javafx.util.Pair;
import structure.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DBComparator {
    Set<String> tableNames1;
    Set<String> tableNames2;
    Set<Pair<Table, Table>> commonTablesDiffs;
    Set<Procedure> procedures1;
    Set<Procedure> procedures2;

    private DBComparator(Set<String> tableNames1, Set<String> tableNames2,
                         Set<Pair<Table, Table>> commonTablesDiffs,
                         Set<Procedure> procedures1, Set<Procedure> procedures2) {
        this.tableNames1 = tableNames1;
        this.tableNames2 = tableNames2;
        this.commonTablesDiffs = commonTablesDiffs;
        this.procedures1 = procedures1;
        this.procedures2 = procedures2;
    }

    public static DBComparator of(Schema s1, Schema s2) {
        Set<String> tableNames1 = getUniqueTables(s1, s2);
        Set<String> tableNames2 = getUniqueTables(s2, s1);
        Set<Pair<Table, Table>> commonTablesDiffs = getCommonTablesDiffs(s1, s2);
        Set<Procedure> procedures1 = getDifferentProcedures(s1, s2);
        Set<Procedure> procedures2 = getDifferentProcedures(s2, s1);

        return new DBComparator(
                tableNames1, tableNames2,
                commonTablesDiffs,
                procedures1, procedures2);
    }

    private static Set<String> getUniqueTables(Schema s1, Schema s2) {
        Set<String> s1TableNames = s1.getTables().stream().map(Table::getName).collect(Collectors.toSet());
        Set<String> s2TableNames = s2.getTables().stream().map(Table::getName).collect(Collectors.toSet());

        s1TableNames.removeAll(s2TableNames);
        return s1TableNames;
    }

    private static Set<String> getCommonTables(Schema s1, Schema s2) {
        Set<String> s1TableNames = s1.getTables().stream().map(Table::getName).collect(Collectors.toSet());
        Set<String> s2TableNames = s2.getTables().stream().map(Table::getName).collect(Collectors.toSet());

        s1TableNames.retainAll(s2TableNames);
        return s1TableNames;
    }

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
        Set<OrderedColumn> primaryKey2 = table.getPrimaryKey();

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

    private static Set<Procedure> getDifferentProcedures(Schema s1, Schema s2) {
        Set<Procedure> s1Procedures = s1.getProcedures();
        Set<Procedure> s2Procedures = s2.getProcedures();

        s1Procedures.retainAll(s2Procedures);
        return s1Procedures;
    }
}
