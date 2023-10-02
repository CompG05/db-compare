package structure;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Table {
    String name;
    Set<Column> columns;
    Set<ForeignKey> foreignKeys;
    Set<Trigger> triggers;

    public Table(String name, Set<Column> columns, Set<ForeignKey> foreignKeys, Set<Trigger> triggers) {
        this.name = name;
        this.columns = columns;
        this.foreignKeys = foreignKeys;
        this.triggers = triggers;
    }

    public Table(String name) {
        this(name, new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public String getName() {
        return name;
    }

    public Set<Column> getColumns() {
        return columns;
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public Set<ForeignKey> getForeignKeys() {
        return foreignKeys;
    }

    public void addForeignKey(ForeignKey foreignKey) {
        foreignKeys.add(foreignKey);
    }

    public Set<Trigger> getTriggers() {
        return triggers;
    }

    public void addTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return name.equals(table.name) && Objects.equals(columns, table.columns) && Objects.equals(foreignKeys, table.foreignKeys) && Objects.equals(triggers, table.triggers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, foreignKeys, triggers);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Table ").append(name);

        for (Column column : columns)
            str.append("\t" + column.toString() + "\n");

        for (Column column : columns)
            str.append("\t" + column.toString() + "\n");

        return str.toString();
    }
}