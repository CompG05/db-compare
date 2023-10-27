package structure;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Schema {
    public String name;
    public Set<Table> tables;
    public Set<Procedure> procedures;

    public Schema(String name) {
        this(name, new HashSet<>(), new HashSet<>());
    }

    public Schema(String name, Set<Table> tables, Set<Procedure> procedures) {
        this.name = name;
        this.tables = tables;
        this.procedures = procedures;
    }

    public void addTable(Table table) {
        tables.add(table);
    }

    public Set<Table> getTables() {
        return tables;
    }

    public void addProcedure(Procedure procedure) {
        procedures.add(procedure);
    }

    public Set<Procedure> getProcedures() {
        return new HashSet<>(procedures);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schema schema = (Schema) o;
        return Objects.equals(name, schema.name) && Objects.equals(tables, schema.tables) && Objects.equals(procedures, schema.procedures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tables, procedures);
    }

    @Override
    public String toString() {
        return "Schema " + name + "\n" +
                "\tTables:\n" +
                "\t\t" + tables.stream().map(Table::toString).collect(Collectors.joining("\n\t\t")) +
                "\n" +
                "\nProcedures:\n" +
                "\t\t" + procedures.stream().map(Procedure::toString).collect(Collectors.joining("\n\t\t")) +
                "\n";
    }
}