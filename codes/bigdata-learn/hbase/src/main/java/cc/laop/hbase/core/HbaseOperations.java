package cc.laop.hbase.core;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Scan;

import java.util.List;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/9/21 17:24
 * @Description:
 */
public interface HbaseOperations {

    /**
     * Executes the given action against the specified table handling resource management.
     * <p>
     * Application exceptions thrown by the action object get propagated to the caller (can only be unchecked).
     * Allows for returning a result object (typically a domain object or collection of domain objects).
     *
     * @param tableName the target table
     * @param <T>       action type
     * @return the result object of the callback action, or null
     */
    <T> T executeWithBuffered(String tableName, TableCallback<T> mapper);

    /**
     * Scans the target table, using the given column family.
     * The content is processed row by row by the given action, returning a list of domain objects.
     *
     * @param tableName target table
     * @param family    column family
     * @param <T>       action type
     * @return a list of objects mapping the scanned rows
     */
    <T> List<T> find(String tableName, String family, final RowMapper<T> mapper);

    /**
     * Scans the target table, using the given column family.
     * The content is processed row by row by the given action, returning a list of domain objects.
     *
     * @param tableName target table
     * @param family    column family
     * @param qualifier column qualifier
     * @param <T>       action type
     * @return a list of objects mapping the scanned rows
     */
    <T> List<T> find(String tableName, String family, String qualifier, final RowMapper<T> mapper);

    /**
     * Scans the target table using the given {@link Scan} object. Suitable for maximum control over the scanning
     * process.
     * The content is processed row by row by the given action, returning a list of domain objects.
     *
     * @param tableName target table
     * @param scan      table scanner
     * @param <T>       action type
     * @return a list of objects mapping the scanned rows
     */
    <T> List<T> find(String tableName, final Scan scan, final RowMapper<T> mapper);

    /**
     * Gets an individual row from the given table. The content is mapped by the given action.
     *
     * @param tableName target table
     * @param rowkey    row name
     * @param mapper    row mapper
     * @param <T>       mapper type
     * @return object mapping the target row
     */
    <T> T get(String tableName, String rowkey, final RowMapper<T> mapper);

    /**
     * Gets an individual row from the given table. The content is mapped by the given action.
     *
     * @param tableName  target table
     * @param rowkey     row name
     * @param familyName column family
     * @param mapper     row mapper
     * @param <T>        mapper type
     * @return object mapping the target row
     */
    <T> T get(String tableName, String rowkey, String familyName, final RowMapper<T> mapper);

    /**
     * Gets an individual row from the given table. The content is mapped by the given action.
     *
     * @param tableName  target table
     * @param rowkey     row name
     * @param familyName family
     * @param qualifier  column qualifier
     * @param mapper     row mapper
     * @param <T>        mapper type
     * @return object mapping the target row
     */
    <T> T get(String tableName, final String rowkey, final String familyName, final String qualifier,
              final RowMapper<T> mapper);


    <T> T get(String tablename, Get get, final RowMapper<T> rowMapper);

    /**
     * 执行put update or delete
     *
     * @param tableName
     * @param action
     */
    void executeWithBuffered(String tableName, MutatorCallback action);

    /**
     * @param tableName
     * @param mutation
     */
    void saveOrUpdate(String tableName, Mutation mutation);

    /**
     * @param tableName
     * @param mutations
     */
    void saveOrUpdates(String tableName, List<Mutation> mutations);
}