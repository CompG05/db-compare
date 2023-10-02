package structure;

import java.util.Objects;

public class ForeignKey {
    String columnName;
    String referencedTable;
    String referencedColumn;

    public ForeignKey(String columnName, String referencedTable, String referencedColumn) {
        this.columnName = columnName;
        this.referencedTable = referencedTable;
        this.referencedColumn = referencedColumn;
    }

// Preguntar:
//  db A
//    tabla a
//        x string
//        y string references b (z)
//
//    tabla b
//        z string
// ------------------------------------
//  db B
//    tabla a
//        x string references b (z)
//        y string
//
//    tabla b
//        z string

    public String getColumnName() {
        return columnName;
    }

    public String getReferencedTable() {
        return referencedTable;
    }

    public String getReferencedColumn() {
        return referencedColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForeignKey that = (ForeignKey) o;
        return Objects.equals(columnName, that.columnName) && Objects.equals(referencedTable, that.referencedTable) && Objects.equals(referencedColumn, that.referencedColumn);
    }

    public boolean hasSameStructure(ForeignKey key) {
        return Objects.equals(referencedTable, key.referencedTable) && Objects.equals(referencedColumn, key.referencedColumn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, referencedTable, referencedColumn);
    }

    @Override
    public String toString() {
        return "FOREIGN KEY (" + columnName + ") REFERENCES " + referencedTable + "(" + referencedColumn + ")";
    }
}
