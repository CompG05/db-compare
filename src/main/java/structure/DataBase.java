package structure;

import java.util.HashSet;
import java.util.Set;

public class DataBase {
    public Set<Schema> schemas;

    public DataBase() {
        schemas = new HashSet<>();
    }

    public DataBase(Set<Schema> schemas) {
        this.schemas = schemas;
    }

    public Set<Schema> getSchemas() {
        return schemas;
    }

    public void addSchema(Schema schema) {
        this.schemas.add(schema);
    }
}
