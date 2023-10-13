package loader;

import structure.*;

import java.sql.*;
import java.util.*;

public class MyLoader {
    static String driver = "com.mysql.cj.jdbc.Driver";
    private Connection connection;
    private String catalog;

    public MyLoader(String url, String catalog, String username, String password) throws SQLException {
        this.catalog = catalog;
        String fullURL = "jdbc:mysql://" + url + "/" + catalog;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.connection = DriverManager.getConnection(fullURL, username, password);
    }

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

    private Set<Column> loadColumns(DatabaseMetaData metaData, String tableName) throws SQLException {
        Set<Column> columnSet = new HashSet<>();
        ResultSet columns = metaData.getColumns(catalog, null, tableName, null);

        while (columns.next())
            columnSet.add(new Column(
                    columns.getString("COLUMN_NAME"),
                    columns.getString("TYPE_NAME"),
                    columns.getString("TYPE_SIZE")));

        return columnSet;
    }

    private Set<String> loadPrimaryKeys(DatabaseMetaData metaData, String tableName) throws SQLException {
        Set<String> primaryKeysSet = new HashSet<>();
        ResultSet primaryKeys = metaData.getPrimaryKeys(catalog, null, tableName);

        while (primaryKeys.next()) {
            primaryKeysSet.add(primaryKeys.getString("COLUMN_NAME"));
        }

        return primaryKeysSet;
    }

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

    private Set<ForeignKey> loadForeignKeys(DatabaseMetaData metaData, String tableName) throws SQLException {
        Set<ForeignKey> fkSet = new HashSet<>();
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

    private Set<Trigger> loadTriggers(DatabaseMetaData metaData, String tableName) {
        return new HashSet<>();
    }

    private Set<Procedure> loadProcedures(DatabaseMetaData metaData) {
        return new HashSet<>();
    }
}