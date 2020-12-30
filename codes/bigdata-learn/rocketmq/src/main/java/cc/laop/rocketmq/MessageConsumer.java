package cc.laop.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/9/7 13:58
 * @Description:
 */
//@Component
@RocketMQMessageListener(topic = Topic.TEST, consumerGroup = "${rocketmq.consumer.group}")
public class MessageConsumer implements RocketMQListener {

    @Override
    public void onMessage(Object message) {
        System.out.println("Consumer:" + message);
    }
}
