package cc.laop.redis;

import cc.laop.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/9/1 17:46
 * @Description:
 */
public class RedisStreamTest extends BaseTest {

    AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    RedisTemplate redisTemplate;

    ExecutorService pool = Executors.newFixedThreadPool(10);

    static final String key = "stream_test";

    @Test
    public void test() throws IOException {
        init();

        consumer();
        consumer();

        String g1 = "g1", g2 = "g2";
        producer(g1);
        producer(g1);
        producer(g2);
        producer(g2);
        producer(g2);

        System.in.read();
    }

    public void init() {
        StreamOperations streamOperations = redisTemplate.opsForStream();
        streamOperations.add(key, Collections.singletonMap("1", 1));
        streamOperations.createGroup(key, "g1");
        streamOperations.createGroup(key, "g2");
    }

    public void consumer() {
        pool.execute(() -> {
            StreamOperations streamOperations = redisTemplate.opsForStream();
            while (true) {
                try {
                    Map map = new HashMap();
                    map.put("s" + counter.addAndGet(1), counter.get());
                    streamOperations.add(key, map);
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void createGroup(String group) {
        StreamOperations streamOperations = redisTemplate.opsForStream();
        streamOperations.createGroup(key, group);
    }

    public void producer(String group) {
        pool.execute(() -> {
            while (true) {
                StreamOperations streamOperations = redisTemplate.opsForStream();

                StreamReadOptions readOptions = StreamReadOptions.empty();
                readOptions.block(Duration.ofSeconds(5000));
                readOptions.count(1);
                readOptions.autoAcknowledge();

                StreamOffset streamOffset = StreamOffset.create(key, ReadOffset.lastConsumed());

                String cname = Thread.currentThread().getName();
                Consumer consumer = Consumer.from(group, cname);

                List<MapRecord> list = streamOperations.read(consumer, readOptions, streamOffset);
                if (list != null) {
                    list.forEach(it -> {
                        StringBuffer sb = new StringBuffer();
                        sb.append("Group:").append(group).append(",");
                        sb.append("Consumer:").append(cname).append(",");
                        sb.append("Record:").append(it);
                        System.out.println(sb.toString());
                    });
                }
            }
        });
    }

}
