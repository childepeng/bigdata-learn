package cc.laop.redis;

import cc.laop.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.types.Expiration;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/8/11 15:14
 * @Description:
 */
public class RedisTest extends BaseTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void standaloneTest() {
        int i = 1;
        while (true) {
            try {
                Thread.sleep(1000);
                ValueOperations valueOperations = redisTemplate.opsForValue();
                //System.out.println(valueOperations.increment("test"));
                valueOperations.set(String.valueOf(++i), i, 60, TimeUnit.SECONDS);
                System.out.println(valueOperations.get(String.valueOf(i)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    @Test
    public void setnx() {
        String key = "test0001";
        String value = "1";
        long expire = 10;
        System.out.println(setnx(key, value, expire));
    }


    public Boolean setnx(String key, String value, long expire) {
        return (Boolean) redisTemplate.execute(connection -> {
            Expiration expiration = Expiration.seconds(expire);
            return connection.set(key.getBytes(), value.getBytes(), expiration,
                    RedisStringCommands.SetOption.SET_IF_ABSENT);
        }, true);
    }

}
