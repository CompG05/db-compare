package structure;

import java.util.*;

/**
 * Table class
 * contains one primary key and set of:
 *          Columns,
 *          ForeignKeys,
 *          Triggers,
 *          Indices
 * PrimaryKey and Indices are represented as a set of OrderedColumn
 */
public class Table {
    String name;
    Set<Column> columns;
    Set<OrderedColumn> primaryKey;
    Set<Set<OrderedColumn>> indices;
    Set<ForeignKey> foreignKeys;
    Set<Trigger> triggers;

    public Table(String name) {
        this(name, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public Table(String name, Set<Column> columns, Set<OrderedColumn> primaryKey, Set<Set<OrderedColumn>> indices, Set<ForeignKey> foreignKeys, Set<Trigger> triggers) {
        this.name = name;
        this.columns = columns;
        this.primaryKey = primaryKey;
        this.indices = indices;
        this.foreignKeys = foreignKeys;
        this.triggers = triggers;
    }

    public String getName() {
        return name;
    }

    public Set<Column> getColumns() {
        return new HashSet<>(columns);
    }

    public Set<OrderedColumn> getPrimaryKey() {
        return new HashSet<>(primaryKey);
    }

    public Set<Set<OrderedColumn>> getIndices() {
        return new HashSet<>(indices);
    }

    public Set<ForeignKey> getForeignKeys() {
        return foreignKeys;
    }

    public Set<Trigger> getTriggers() {
        return new HashSet<>(triggers);
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public void addColumns(Collection<Column> columns) {
        this.columns.addAll(columns);
    }

    public void addPrimaryKeyColumn(OrderedColumn primaryKey) {
        this.primaryKey.add(primaryKey);
    }

    public void addPrimaryKeyColumn(String primaryKey, int order) {
        this.primaryKey.add(new OrderedColumn(primaryKey, order));
    }

    public void addPrimaryKey(Collection<OrderedColumn> primaryKeys) {
        this.primaryKey.addAll(primaryKeys);
    }

    public void addIndex(Set<OrderedColumn> index) {
        this.indices.add(index);
    }

    public void addIndices(Collection<Set<OrderedColumn>> indices) {
        this.indices.addAll(indices);
    }

    public void addForeignKey(ForeignKey foreignKey) {
        foreignKeys.add(foreignKey);
    }

    public void addForeignKeys(Collection<ForeignKey> foreignKeys) {
        this.foreignKeys.addAll(foreignKeys);
    }

    public void addTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    public void addTriggers(Collection<Trigger> triggers){
        this.triggers.addAll(triggers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(name, table.name) && Objects.equals(columns, table.columns) && Objects.equals(primaryKey, table.primaryKey) && Objects.equals(indices, table.indices) && Objects.equals(foreignKeys, table.foreignKeys) && Objects.equals(triggers, table.triggers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, columns, primaryKey, indices, foreignKeys, triggers);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Table ").append(name).append('\n');

        //print columns
        for (Column column : columns)
            str.append("\t\t\t").append(column.toString()).append('\n');

        //print primary key
        if (!primaryKey.isEmpty()) {
            str.append("\t\t\tPRIMARY KEY (")
                    .append(String.join(", ", OrderedColumn.getSorted(primaryKey)))
                    .append(")\n");
        }

        //print indices
        if (!indices.isEmpty()) {
            for (Set<OrderedColumn> index : indices)
                str.append("\t\t\tINDEX (")
                        .append(String.join(", ", OrderedColumn.getSorted(index)))
                        .append(")\n");
        }

        //print foreign keys
        for (ForeignKey foreignKey : foreignKeys)
            str.append("\t\t\t").append(foreignKey.toString()).append('\n');

        //print triggers
        for (Trigger trigger : triggers)
            str.append("\t\t\t").append(trigger.toString()).append('\n');

        return str.toString();
    }
}