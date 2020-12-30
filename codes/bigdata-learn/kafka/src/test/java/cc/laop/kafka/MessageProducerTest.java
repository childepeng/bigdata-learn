package cc.laop.kafka;

import cc.laop.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        while (true) {
            Thread.sleep(1000);
            String ct = String.valueOf(counter.addAndGet(1));
            producer.send(Topic.TEST, ct, ct);
        }
    }

}
