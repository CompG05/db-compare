package loader;

import structure.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class Loader {

    String driver;
    Connection connection;
    String catalog;

    /**
     * Load a schema from the database into a Schema object
     *
     * @return a Schema object with the elements of the database
     */
    public Schema loadSchema() {
        try {
            DatabaseMetaData metaData;

            metaData = connection.getMetaData();

            Set<Table> tables = loadTables(metaData);
            Set<Procedure> procedures = loadProcedures(metaData);

            return new Schema(catalog, tables, procedures);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load the tables of the database into Table objects
     * @param metaData the metadata of the database
     * @return a set of Table objects
     * @throws SQLException if a database access error occurs
     */
    private Set<Table> loadTables(DatabaseMetaData metaData) throws SQLException {
        ResultSet tables = metaData.getTables(catalog, null, null, new String[]{"TABLE"});
        String tableName;

        Set<Table> tableSet = new HashSet<>();
        Table table;
        while (tables.next()) {
            tableName = tables.getString("TABLE_NAME");
            table = new Table(
                    tableName,
                    loadColumns(metaData, tableName),
                    loadPrimaryKeys(metaData, tableName),
                    loadIndices(metaData, tableName),
                    loadForeignKeys(metaData, tableName),
                    loadTriggers(metaData, tableName)
            );
            tableSet.add(table);
        }

        return tableSet;
    }

    /**
     * Load the columns of the given table into Column objects
     * @param metaData the metadata of the database
     * @param tableName the name of the table whose columns are to be retrieved
     * @return a set of Column objects of the given table
     * @throws SQLException if an error occurs while accessing the database or reading a column
     */
    private Set<Column> loadColumns(DatabaseMetaData metaData, String tableName) throws SQLException {
        Set<Column> columnSet = new HashSet<>();
        ResultSet columns = metaData.getColumns(catalog, null, tableName, null);

        while (columns.next())
            columnSet.add(new Column(
                    columns.getString("COLUMN_NAME"),
                    columns.getString("TYPE_NAME"),
                    columns.getString("COLUMN_SIZE"),
                    columns.getBoolean("NULLABLE")));

        return columnSet;
    }

    /**
     * Load the primary key of the given table into a set of OrderedColumn objects
     * @param metaData the metadata object of the database
     * @param tableName the name of the table whose primary key is to be loaded
     * @return a primary key in the form of a set of OrderedColumn objects
     * @throws SQLException if an error occurs while accessing the database of reading a primary key
     */
    private Set<OrderedColumn> loadPrimaryKeys(DatabaseMetaData metaData, String tableName) throws SQLException {
        Set<OrderedColumn> primaryKeysSet = new HashSet<>();
        ResultSet primaryKeys = metaData.getPrimaryKeys(catalog, null, tableName);

        while (primaryKeys.next()) {
            primaryKeysSet.add(new OrderedColumn(
                    primaryKeys.getString("COLUMN_NAME"),
                    primaryKeys.getInt("KEY_SEQ")));
        }

        return primaryKeysSet;
    }

    /**
     * Load the indices of the given table into an inner representation
     * @param metaData the metadata object of the database
     * @param tableName the name of the table whose indices are to be retrieved
     * @return a set of indices in the form of sets of OrderedColumn objects
     * @throws SQLException if an error occurs while accessing the database or reading an index
     */
    private Set<Set<OrderedColumn>> loadIndices(DatabaseMetaData metaData, String tableName) throws SQLException {
        ResultSet indices = metaData.getIndexInfo(catalog, null, tableName, false, false);

        Set<OrderedColumn> index;
        String indexName;
        HashMap<String, Set<OrderedColumn>> indexMap = new HashMap<>();
        while (indices.next()) {
            indexName = indices.getString("INDEX_NAME");
            index = indexMap.getOrDefault(indexName, new HashSet<>());
            indexMap.putIfAbsent(indexName, index);

            index.add(new OrderedColumn(
                    indices.getString("COLUMN_NAME"),
                    indices.getInt("ORDINAL_POSITION")));
        }

        return new HashSet<>(indexMap.values());
    }

    /**
     * Load the foreign keys of the given table into ForeignKey objects
     * @param metaData the metadata object of the database
     * @param tableName the name of the table whose foreign keys are to be retrieved
     * @return a set of ForeignKey objects
     * @throws SQLException if an error occurs while accessing the database or reading a foreign key
     */
    private Set<ForeignKey> loadForeignKeys(DatabaseMetaData metaData, String tableName) throws SQLException {
        ResultSet fks = metaData.getImportedKeys(catalog, null, tableName);

        HashMap<String, ForeignKey> fkMap = new HashMap<>();

        ForeignKey fk;
        String referencedTableName, fkName;
        int keySeq;
        while (fks.next()) {
            referencedTableName = fks.getString("PKTABLE_NAME");
            fkName = fks.getString("FK_NAME");
            fk = fkMap.getOrDefault(fkName, new ForeignKey(referencedTableName));
            fkMap.putIfAbsent(fkName, fk);

            keySeq = fks.getInt("KEY_SEQ");
            fk.addColumn(new OrderedColumn(fks.getString("FKCOLUMN_NAME"), keySeq));
            fk.addReferencedColumn((new OrderedColumn(fks.getString("PKCOLUMN_NAME"), keySeq)));
        }

        return new HashSet<>(fkMap.values());
    }

    /**
     * Load the triggers of the given table into Trigger objects
     * @param metaData the metadata object of the database
     * @param tableName the name of the table whose triggers are to be retrieved
     * @return a set of Trigger objects
     * @throws SQLException if an error occurs while accessing the database or reading a trigger
     */
    abstract Set<Trigger> loadTriggers(DatabaseMetaData metaData, String tableName) throws SQLException;

    /**
     * Load the procedures of the database into Procedure objects
     * @param metaData the metadata object of the database
     * @return a set of Procedure objects
     * @throws SQLException if an error occurs while accessing the database or reading a procedure
     */
    private Set<Procedure> loadProcedures(DatabaseMetaData metaData) throws SQLException {
        Set<Procedure> procedureSet = new HashSet<>();
        ResultSet procedures = metaData.getProcedures(catalog, null, null);

        while (procedures.next()) {
            String procedureName = procedures.getString("PROCEDURE_NAME");

            Set<Argument> argSet = new HashSet<>();
            ResultSet args = metaData.getProcedureColumns(catalog, null, procedureName, null);
            while (args.next()) {
                argSet.add(new Argument(
                   args.getString("COLUMN_NAME"),
                   args.getInt("ORDINAL_POSITION"),
                   ArgumentType.from(args.getInt("COLUMN_TYPE")),
                   args.getString("TYPE_NAME")
                ));
            }

            procedureSet.add(new Procedure(procedureName, argSet));
        }

        return procedureSet;
    }
}