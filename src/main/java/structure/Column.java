package structure;

public class Column {
    String name;
    String typeName;
    boolean primaryKey;

    public Column(String name, String typeName, boolean primaryKey) {
        this.name = name;
        this.typeName = typeName;
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey() {
        this.primaryKey = true;
    }
}
