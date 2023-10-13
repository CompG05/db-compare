package structure;

import java.util.Objects;

public class Column {
    String name;
    String typeName;
    String typeSize;

    public Column(String name, String typeName, String typeSize) {
        this.name = name;
        this.typeName = typeName;
        this.typeSize = typeSize;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return Objects.equals(name, column.name) && Objects.equals(typeName, column.typeName) && Objects.equals(typeSize, column.typeSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, typeName, typeSize);
    }

    @Override
    public String toString() {
        return name + " " + typeName + "(" + typeSize + ")";
    }
}
