package cc.laop.rocketmq;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/9/7 13:58
 * @Description:
 */
@Component
public class MessageProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 同步发送
     *
     * @param topic
     * @param msg
     */
    public void syncSend(String topic, String msg) {

        SendResult result = rocketMQTemplate.syncSend(topic, msg);
        System.out.println(result.toString());

    }

    /**
     * 异步发送
     *
     * @param topic
     * @param msg
     */
    public void asyncSend(String topic, Object msg) {
        // sendcallback消息发送回调
        rocketMQTemplate.asyncSend(topic, msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println(sendResult.toString());
            }

            @Override
            public void onException(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    /**
     * 异步发送有序消息
     * key用于选择消息队列（同一个队列的消息是可以保证有序性的）
     * 所以对于有顺序性需求的同类的消息可以使用相同key，保证这些消息被分配到相同的队列
     *
     * @param topic
     * @param key
     * @param msg
     */
    public void sendOrderly(String topic, String key, Object msg) {
        Message message = MessageBuilder.withPayload(msg).build();
        rocketMQTemplate.syncSendOrderly(topic, message, key);
    }


    /**
     * 事务消息
     *
     * @param topic
     * @param key
     * @param msg
     */
    public void sendWithTranslation(String topic, String key, Object msg) {
        Message message = MessageBuilder.withPayload(msg).setHeader(RocketMQHeaders.TRANSACTION_ID, key).build();
        SendResult result = rocketMQTemplate.sendMessageInTransaction(topic, message, null);
        System.out.println(result.toString());
    }

}
