package cc.laop.hbase.example;

import cc.laop.hbase.core.HbaseTemplate;
import cc.laop.hbase.core.RowMapper;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: create in 2021/1/4 11:33
 * @Description:
 */
public class UserMapper implements RowMapper<UserPO> {

    private final String TABLE_NAME = "user";
    private final byte[] CF_USER = "cf_userinfo".getBytes();
    private final byte[] CF_ROLE = "cf_roleinfo".getBytes();

    private final byte[] Q_USERNAME = "username".getBytes();
    private final byte[] Q_ROLES = "roles".getBytes();

    private HbaseTemplate hbaseTemplate;

    public UserPO get(byte[] rowkey) {
        Get get = new Get(rowkey);
        get.addFamily(CF_USER);
        get.addFamily(CF_ROLE);
        return hbaseTemplate.get(TABLE_NAME, get, this);
    }

    private byte[] generateRowkey(UserPO user) {
        return user.getUsername().getBytes();
    }

    public void put(List<UserPO> userlist) {
        List<Mutation> puts = new ArrayList<>();
        for (UserPO userPO : userlist) {
            Put put = new Put(generateRowkey(userPO));
            put.addColumn(CF_USER, Q_USERNAME, userPO.getUsername().getBytes());
            put.addColumn(CF_ROLE, Q_ROLES, userPO.getRoles().getBytes());
            puts.add(put);
        }
        hbaseTemplate.saveOrUpdates(TABLE_NAME, puts);
    }

    @Override
    public UserPO mapRow(Result result, int rowNum) throws Exception {
        UserPO user = new UserPO();
        user.setUsername(Bytes.toString(result.getValue(CF_USER, Q_USERNAME)));
        user.setRoles(Bytes.toString(result.getValue(CF_ROLE, Q_ROLES)));
        return user;
    }
}
