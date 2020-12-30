package cc.laop.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/9/2 16:58
 * @Description:
 */
@Component
public class MessageProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String k, String v) {
        //ListenableFuture listenableFuture = kafkaTemplate.send(topic, k, v);

        //可指定消息发送的分片
        ProducerRecord record = new ProducerRecord(topic, k, v);
        ListenableFuture listenableFuture = kafkaTemplate.send(record);

        //listenableFuture.addCallback(o -> System.out.print("OK. " + o), throwable -> throwable.printStackTrace());
        listenableFuture.addCallback(System.out::println, Throwable::printStackTrace);
    }

}
