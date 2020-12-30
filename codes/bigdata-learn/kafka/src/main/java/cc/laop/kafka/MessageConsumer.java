package cc.laop.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/9/2 16:48
 * @Description:
 */
@Component
public class MessageConsumer implements MessageListener<String, String> {

    @Override
    @KafkaListener(topics = {Topic.TEST})
    public void onMessage(ConsumerRecord<String, String> record) {
        System.out.println(String.format("Topic:%s, Offset:%s, Partition:%s, K:%s", record.topic(),
                record.offset(), record.partition(), record.key()));
    }

}
