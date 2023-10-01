package structure;

import java.util.HashSet;

public class Table {
    HashSet<Column> columns = null;
    HashSet<ForeignKey> foreignKeys = null;
    HashSet<Trigger> triggers = null;

    public HashSet<Column> getColumns() {
        return columns;
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }

    public HashSet<ForeignKey> getForeignKeys() {
        return foreignKeys;
    }

    public void addForeignKey(ForeignKey foreignKey) {
        this.foreignKeys.add(foreignKey);
    }

    public HashSet<Trigger> getTriggers() {
        return triggers;
    }

    public void addTrigger(Trigger trigger) {
        this.triggers.add(trigger);
    }
}
