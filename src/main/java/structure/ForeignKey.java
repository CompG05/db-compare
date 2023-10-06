package structure;

import java.util.List;
import java.util.Objects;

public class ForeignKey {
    List<String> columnNames;
    String referencedTable;
    List<String> referencedColumns;

    public ForeignKey(List<String> columnNames, String referencedTable, List<String> referencedColumns) {
        this.columnNames = columnNames;
        this.referencedTable = referencedTable;
        this.referencedColumns = referencedColumns;
    }

    public ForeignKey(String referencedTable) {
        this.referencedTable = referencedTable;
    }

    public void addColumnName(String columnName) {
        columnNames.add(columnName);
    }

    public void addReferencedColumn(String referencedColumn) {
        referencedColumns.add(referencedColumn);
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public String getReferencedTable() {
        return referencedTable;
    }

    public List<String> getReferencedColumns() {
        return referencedColumns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForeignKey that = (ForeignKey) o;
        return Objects.equals(columnNames, that.columnNames) && Objects.equals(referencedTable, that.referencedTable) && Objects.equals(referencedColumns, that.referencedColumns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnNames, referencedTable, referencedColumns);
    }

    @Override
    public String toString() {
        return "FOREIGN KEY (" + columnNames + ") REFERENCES " + referencedTable + "(" + referencedColumns + ")";
    }
}
