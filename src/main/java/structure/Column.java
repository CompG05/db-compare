package structure;

import java.util.Objects;

/**
 * Column class
 */
public class Column {
    String name;
    String typeName;
    String typeSize;
    boolean nullable;

    public Column(String name, String typeName, String typeSize, boolean nullable) {
        this.name = name;
        this.typeName = typeName;
        this.typeSize = typeSize;
        this.nullable = nullable;
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

    public boolean isNullable() {
        return nullable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return Objects.equals(name, column.name) && Objects.equals(typeName, column.typeName) && Objects.equals(typeSize, column.typeSize) && Objects.equals(nullable, column.nullable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, typeName, typeSize, nullable);
    }

    @Override
    public String toString() {
        return name + " " + typeName + "(" + typeSize + ")" + (nullable ? "" : " NOT NULL");
    }
}
