package loader;

import structure.Schema;

public interface Loader {
    /**
     * Load a schema from the database into a Schema object
     * @return a Schema object with the elements of the database
     */
    public Schema loadSchema();
}
