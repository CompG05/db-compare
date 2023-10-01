package structure;

import java.util.HashSet;

public class DataBase {
    public HashSet<Schema> schemas = null;

    public HashSet<Schema> getSchemas() {
        return schemas;
    }

    public void addSchema(Schema schema) {
        this.schemas.add(schema);
    }
}
