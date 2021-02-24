package cc.laop.kafka;

import cc.laop.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/9/2 17:17
 * @Description:
 */
public class MessageProducerTest extends BaseTest {

    AtomicInteger counter = new AtomicInteger(1);

    @Autowired
    private MessageProducer producer;

    @Test
    public void send() throws InterruptedException {
        // int i = 0;
        while (true) {
            Thread.sleep(1000);
            // String ct = String.valueOf(counter.addAndGet(1));
            // producer.send(Topic.TEST, ct, ct);
            Random random = new Random();
            // int i = random.nextInt(10);
            // i++;
            int i = 1;
            long time = System.currentTimeMillis();
            // time = time - i * 88;
            // Map map = new HashMap();
            // map.put("id", i);
            // map.put("time", time);

            producer.send("watermarkTest01", String.valueOf(i), i + "," + time);
        }
    }

}
