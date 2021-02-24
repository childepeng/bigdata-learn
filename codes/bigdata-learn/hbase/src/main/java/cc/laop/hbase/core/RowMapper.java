package cc.laop.hbase.core;

import org.apache.hadoop.hbase.client.Result;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/9/21 18:18
 * @Description:
 */
public interface RowMapper<T> {

    T mapRow(Result result, int rowNum) throws Exception;

}