package structure;

import java.util.Objects;

public class Column {
    String name;
    String typeName;
    String typeSize;
    boolean primaryKey;

    public Column(String name, String typeName, String typeSize, boolean primaryKey) {
        this.name = name;
        this.typeName = typeName;
        this.typeSize = typeSize;
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTypeSize() {
        return typeSize;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return primaryKey == column.primaryKey && Objects.equals(name, column.name) && Objects.equals(typeName, column.typeName) && Objects.equals(typeSize, column.typeSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, typeName, typeSize, primaryKey);
    }

    @Override
    public String toString() {
        return name + " " + typeName + "(" + typeSize + ")";
    }
}
