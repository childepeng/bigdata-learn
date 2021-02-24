package cc.laop.hbase.core;

import org.apache.hadoop.hbase.client.Table;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/9/21 18:18
 * @Description:
 */
@FunctionalInterface
public interface TableCallback<T> {

    /**
     * Gets called by {@link HbaseTemplate} execute with an active Hbase table. Does need to care about activating or
     * closing down the table.
     *
     * @param table active Hbase table
     * @return a result object, or null if none
     * @throws Throwable thrown by the Hbase API
     */
    T doInTable(Table table) throws Throwable;
}
