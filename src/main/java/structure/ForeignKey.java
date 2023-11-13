package structure;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *  Foreign Key class
 *  contains the name of the referenced table
 *  and a set of:
 *      columns of the current table
 *      columns of the referenced table
 *  both represented as a set of OrderedColumn
 */
public class ForeignKey {
    Set<OrderedColumn> columns;
    String referencedTable;
    Set<OrderedColumn> referencedColumns;

    public ForeignKey(String referencedTable) {
        this(new HashSet<>(), referencedTable, new HashSet<>());
    }

    public ForeignKey(Set<OrderedColumn> columns, String referencedTable, Set<OrderedColumn> referencedColumns) {
        this.columns = columns;
        this.referencedTable = referencedTable;
        this.referencedColumns = referencedColumns;
    }

    public Set<OrderedColumn> getColumns() {
        return columns;
    }

    public String getReferencedTable() {
        return referencedTable;
    }

    public Set<OrderedColumn> getReferencedColumns() {
        return referencedColumns;
    }

    public void addColumn(OrderedColumn columnName) {
        columns.add(columnName);
    }

    public void addReferencedColumn(OrderedColumn referencedColumn) {
        referencedColumns.add(referencedColumn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForeignKey that = (ForeignKey) o;
        return Objects.equals(columns, that.columns) && Objects.equals(referencedTable, that.referencedTable) && Objects.equals(referencedColumns, that.referencedColumns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, referencedTable, referencedColumns);
    }

    @Override
    public String toString() {
        return "FOREIGN KEY ("
                + String.join(", ", OrderedColumn.getSorted(columns))
                + ") REFERENCES " + referencedTable + "("
                + String.join(", ", OrderedColumn.getSorted(referencedColumns))
                + ")";
    }
}
