package comparator;

import structure.Procedure;
import structure.Schema;
import structure.Table;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class DBComparator {
    Set<String> tableNames1;
    Set<String> tableNames2;
    Set<String> commonTableNames;
    HashMap<String, Table> commonTablesDiffs1;
    HashMap<String, Table> commonTablesDiffs2;
    Set<Procedure> procedures1;
    Set<Procedure> procedures2;

    private DBComparator(Set<String> tableNames1, Set<String> tableNames2, Set<String> commonTableNames, HashMap<String, Table> commonTablesDiffs1, HashMap<String, Table> commonTablesDiffs2, Set<Procedure> procedures1, Set<Procedure> procedures2) {
        this.tableNames1 = tableNames1;
        this.tableNames2 = tableNames2;
        this.commonTableNames = commonTableNames;
        this.commonTablesDiffs1 = commonTablesDiffs1;
        this.commonTablesDiffs2 = commonTablesDiffs2;
        this.procedures1 = procedures1;
        this.procedures2 = procedures2;
    }

    public static DBComparator of(Schema s1, Schema s2) {  
        Set<String> tableNames1 = getUniqueTables(s1, s2);
        Set<String> tableNames2 = getUniqueTables(s2, s1);
        Set<String> commonTableNames = getCommonTables(s1, s2);
        HashMap<String, Table> commonTablesDiffs1 = getCommonTablesDiffs(s1, s2, commonTableNames);
        HashMap<String, Table> commonTablesDiffs2 = getCommonTablesDiffs(s2, s1, commonTableNames);Set<Procedure> procedures1 = getDifferentProcedures(s1, s2);
        Set<Procedure> procedures2 = getDifferentProcedures(s2, s1);

        return new DBComparator(tableNames1, tableNames2, commonTableNames, commonTablesDiffs1, commonTablesDiffs2, procedures1, procedures2);
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

    private static HashMap<String, Table> getCommonTablesDiffs(Schema s1, Schema s2, Set<String> commonTableNames) {
        Set<Table> tables1 = s1.getTables().stream()
                .filter(t -> commonTableNames.contains(t.getName()))
                .collect(Collectors.toSet());
        Set<Table> tables2 = s2.getTables().stream()
                .filter(t -> commonTableNames.contains(t.getName()))
                .collect(Collectors.toSet());

        HashMap<String, Table> commonTablesDiff = new HashMap<>();
        for (String tableName: commonTableNames) {
            Table diffTable = new Table(tableName);
            Table t = s1.getTable(tableName);
        }
    }

    private static Set<Procedure> getDifferentProcedures(Schema s1, Schema s2) {
        Set<Procedure> s1Procedures = s1.getProcedures();
        Set<Procedure> s2Procedures = s2.getProcedures();

        s1Procedures.retainAll(s2Procedures);
        return s1Procedures;
    }
}
