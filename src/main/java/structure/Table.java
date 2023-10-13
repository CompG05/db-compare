package structure;

import java.util.*;

public class Table {
    String name;
    Set<Column> columns;
    Set<String> primaryKeys;
    Set<Set<OrderedColumn>> indices;
    Set<ForeignKey> foreignKeys;
    Set<Trigger> triggers;

    public Table(String name) {
        this(name, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public Table(String name, Set<Column> columns, Set<String> primaryKeys, Set<Set<OrderedColumn>> indices, Set<ForeignKey> foreignKeys, Set<Trigger> triggers) {
        this.name = name;
        this.columns = columns;
        this.primaryKeys = primaryKeys;
        this.indices = indices;
        this.foreignKeys = foreignKeys;
        this.triggers = triggers;
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

    public Set<String> getPrimaryKeys() {
        return primaryKeys;
    }

    public void addPrimaryKeys(String primaryKey) {
        this.primaryKeys.add(primaryKey);
    }

    public Set<Set<OrderedColumn>> getIndices() {
        return indices;
    }

    public void addIndex(Set<OrderedColumn> index) {
        this.indices.add(index);
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
        return Objects.equals(name, table.name) && Objects.equals(columns, table.columns) && Objects.equals(primaryKeys, table.primaryKeys) && Objects.equals(indices, table.indices) && Objects.equals(foreignKeys, table.foreignKeys) && Objects.equals(triggers, table.triggers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, columns, primaryKeys, indices, foreignKeys, triggers);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Table ").append(name);

        for (Column column : columns)
            str.append("\t" + column.toString() + "\n");

        str.append("\tPRIMARY KEY (")
           .append(String.join(", ", primaryKeys))
           .append(")");

        for (Set<OrderedColumn> index : indices)
            str.append("\tINDEX(")
                    .append(String.join(", ", OrderedColumn.getSorted(index)))
                    .append(")\n");

        for (ForeignKey foreignKey : foreignKeys)
            str.append("\t" + foreignKey.toString() + "\n");

        for (Trigger trigger : triggers)
            str.append("\t" + trigger.toString() + "\n");

        return str.toString();
    }
}