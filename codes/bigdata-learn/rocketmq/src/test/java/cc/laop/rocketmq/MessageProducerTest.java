package cc.laop.rocketmq;

import cc.laop.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/9/7 15:44
 * @Description:
 */
public class MessageProducerTest extends BaseTest {

    @Autowired
    private MessageProducer messageProducer;

    private AtomicInteger counter = new AtomicInteger(0);

    private ExecutorService pool = Executors.newFixedThreadPool(10);

    @Test
    public void syncSend() throws IOException {
        for (int i = 0; i < 3; i++) {
            pool.execute(() -> {
                while (true) {
                    try {
                        Thread.sleep(100);
                        synchronized (counter) {
                            String msg = String.valueOf(counter.addAndGet(1));
                            messageProducer.sendOrderly(Topic.TEST, msg, msg);
                            //messageProducer.syncSend(Topic.TEST, msg);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        System.in.read();
    }


    @Test
    public void sendTx() throws IOException {
        pool.execute(() -> {
            String msg = String.valueOf(counter.addAndGet(1));
            messageProducer.sendWithTranslation(Topic.TEST, msg, msg);
            //messageProducer.syncSend(Topic.TEST, msg);
        });
        System.in.read();
    }

}
