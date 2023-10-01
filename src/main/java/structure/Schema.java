package structure;


import java.util.HashSet;

public class Schema {
    public HashSet<Table> tables = null;
    public HashSet<Procedure> procedures = null;

    public void addTable(Table table) {
        this.tables.add(table);
    }

    public HashSet<Table> getTables() {
        return tables;
    }

    public void addProcedure(Procedure procedure) {
        this.procedures.add(procedure);
    }

    public HashSet<Procedure> getProcedures() {
        return procedures;
    }
}
