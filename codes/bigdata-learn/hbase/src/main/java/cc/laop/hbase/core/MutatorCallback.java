package cc.laop.hbase.core;

import org.apache.hadoop.hbase.client.BufferedMutator;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/9/21 18:20
 * @Description:
 */
public interface MutatorCallback {

    /**
     * 使用mutator api to update put and delete
     *
     * @param mutator
     * @throws Throwable
     */
    void doInMutator(BufferedMutator mutator) throws Throwable;
}
