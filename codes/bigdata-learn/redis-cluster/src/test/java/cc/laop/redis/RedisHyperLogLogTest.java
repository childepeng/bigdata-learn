package cc.laop.redis;

import cc.laop.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Random;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/9/1 17:44
 * @Description:
 */
public class RedisHyperLogLogTest extends BaseTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test() {
        HyperLogLogOperations hyperLogLogOperations = redisTemplate.opsForHyperLogLog();
        int size = 1000000;
        Random random = new Random();
        String key = "hypertest";
        for (int i = 0; i < size; i++) {
            hyperLogLogOperations.add(key, random.nextInt(size));
        }
        System.out.println(hyperLogLogOperations.size(key));
    }

}
