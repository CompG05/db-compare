package structure;


import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Schema class
 * Contains a set of tables and procedures
 */
public class Schema {
    String name;
    Set<Table> tables;
    Set<Procedure> procedures;

    public Schema(String name) {
        this(name, new HashSet<>(), new HashSet<>());
    }

    public Schema(String name, Set<Table> tables, Set<Procedure> procedures) {
        this.name = name;
        this.tables = tables;
        this.procedures = procedures;
    }

    public String getName(){
        return name;
    }

    public Set<Table> getTables() {
        return tables;
    }

    public Set<Procedure> getProcedures() {
        return new HashSet<>(procedures);
    }

    public void addTable(Table table) {
        tables.add(table);
    }

    public void addProcedure(Procedure procedure) {
        procedures.add(procedure);
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

    /**
     * Search for a table with a given name
     * @param name of a table
     * @return a table if it exists, empty otherwise
     */
    public Optional<Table> getTable(String name) {
        for (Table table : tables) {
            if (table.getName().equals(name))
                return Optional.of(table);
        }
        return Optional.empty();
    }

    /**
     * Search for a procedure with a given name
     * @param name of a procedure
     * @return a procedure if it exists, empty otherwise
     */
    public Optional<Procedure> getProcedure(String name) {
        for (Procedure proc: procedures) {
            if (proc.getName().equals(name))
                return Optional.of(proc);
        }
        return Optional.empty();
    }
}