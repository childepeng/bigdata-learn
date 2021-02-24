package cc.laop.hbase.core;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/9/21 18:19
 * @Description:
 */
public class HbaseTemplate implements HbaseOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(HbaseTemplate.class);

    private Configuration configuration;

    private volatile Connection connection;

    public HbaseTemplate(Configuration configuration) {
        this.setConfiguration(configuration);
        Assert.notNull(configuration, " a valid configuration is required");
    }

    @Override
    public <T> T executeWithBuffered(String tableName, TableCallback<T> action) {
        Assert.notNull(action, "Callback object must not be null");
        Assert.notNull(tableName, "No table specified");

        StopWatch sw = new StopWatch();
        sw.start();
        Table table = null;
        try {
            table = this.getConnection().getTable(TableName.valueOf(tableName));
            return action.doInTable(table);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            if (null != table) {
                try {
                    table.close();
                    sw.stop();
                } catch (IOException e) {
                    LOGGER.error("hbase资源释放失败");
                }
            }
        }
    }

    @Override
    public <T> List<T> find(String tableName, String family, final RowMapper<T> action) {
        Scan scan = new Scan();
        scan.setCaching(10000);
        scan.addFamily(Bytes.toBytes(family));
        return this.find(tableName, scan, action);
    }

    @Override
    public <T> List<T> find(String tableName, String family, String qualifier, final RowMapper<T> action) {
        Scan scan = new Scan();
        scan.setCaching(10000);
        scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
        return this.find(tableName, scan, action);
    }

    @Override
    public <T> List<T> find(String tableName, final Scan scan, final RowMapper<T> action) {
        return this.executeWithBuffered(tableName, table -> {
            int caching = scan.getCaching();
            // 如果caching未设置(默认是1)，将默认配置成5000
            if (caching == 1) {
                scan.setCaching(10000);
            }
            ResultScanner scanner = table.getScanner(scan);
            try {
                List<T> rs = new ArrayList<T>();
                int rowNum = 0;
                for (Result result : scanner) {
                    rs.add(action.mapRow(result, rowNum++));
                }
                return rs;
            } finally {
                scanner.close();
            }
        });
    }


    public void delete(String tableName, byte[] rowkey) {
        this.executeWithBuffered(tableName, table -> {
            Delete del = new Delete(rowkey);
            table.delete(del);
            return null;
        });
    }


    public long count(String tableName, final Scan scan) {
        return this.executeWithBuffered(tableName, table -> {
            int caching = scan.getCaching();
            // 如果caching未设置(默认是1)，将默认配置成5000
            if (caching == 1) {
                scan.setCaching(10000);
            }
            ResultScanner scanner = table.getScanner(scan);
            try {
                long rowNum = 0;
                for (Result result : scanner) {
                    rowNum++;
                }
                return rowNum;
            } finally {
                scanner.close();
            }
        });
    }


    @Override
    public <T> T get(String tableName, String rowkey, final RowMapper<T> mapper) {
        return this.get(tableName, rowkey, null, null, mapper);
    }

    @Override
    public <T> T get(String tableName, String rowkey, String familyName, final RowMapper<T> mapper) {
        return this.get(tableName, rowkey, familyName, null, mapper);
    }

    @Override
    public <T> T get(String tableName, final String rowkey, final String familyName, final String qualifier,
                     final RowMapper<T> mapper) {
        return this.executeWithBuffered(tableName, table -> {
            Get get = new Get(Bytes.toBytes(rowkey));
            if (StringUtils.isNotBlank(familyName)) {
                byte[] family = Bytes.toBytes(familyName);
                if (StringUtils.isNotBlank(qualifier)) {
                    get.addColumn(family, Bytes.toBytes(qualifier));
                } else {
                    get.addFamily(family);
                }
            }
            Result result = table.get(get);
            return mapper.mapRow(result, 0);
        });
    }

    @Override
    public <T> T get(String tablename, Get get, RowMapper<T> rowMapper) {
        return this.executeWithBuffered(tablename, table -> {
            Result result = table.get(get);
            return rowMapper.mapRow(result, 0);
        });
    }

    @Override
    public void executeWithBuffered(String tableName, MutatorCallback action) {
        Assert.notNull(action, "Callback object must not be null");
        Assert.notNull(tableName, "No table specified");

        StopWatch sw = new StopWatch();
        sw.start();
        BufferedMutator mutator = null;
        try {
            BufferedMutatorParams mutatorParams = new BufferedMutatorParams(TableName.valueOf(tableName));
            mutator = this.getConnection().getBufferedMutator(mutatorParams.writeBufferSize(3 * 1024 * 1024));
            action.doInMutator(mutator);
        } catch (Throwable throwable) {
            sw.stop();
            throw new RuntimeException(throwable);
        } finally {
            if (null != mutator) {
                try {
                    mutator.flush();
                    mutator.close();
                    sw.stop();
                } catch (IOException e) {
                    LOGGER.error("hbase mutator资源释放失败");
                }
            }
        }
    }

    @Override
    public void saveOrUpdate(String tableName, final Mutation mutation) {
        this.executeWithBuffered(tableName, mutator -> {
            mutator.mutate(mutation);
        });
    }

    @Override
    public void saveOrUpdates(String tableName, final List<Mutation> mutations) {
        this.executeWithBuffered(tableName, mutator -> {
            mutator.mutate(mutations);
        });
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        if (null == this.connection) {
            synchronized (this) {
                if (null == this.connection) {
                    try {
                        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(200, Integer.MAX_VALUE, 60L,
                                TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
                        // init pool
                        poolExecutor.prestartCoreThread();
                        this.connection = ConnectionFactory.createConnection(configuration, poolExecutor);
                    } catch (IOException e) {
                        LOGGER.error("hbase connection资源池创建失败");
                    }
                }
            }
        }
        return this.connection;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}