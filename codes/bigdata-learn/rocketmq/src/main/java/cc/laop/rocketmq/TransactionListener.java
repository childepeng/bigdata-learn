package cc.laop.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/9/7 19:29
 * @Description:
 */
@RocketMQTransactionListener(rocketMQTemplateBeanName = "rocketMQTemplate")
public class TransactionListener implements RocketMQLocalTransactionListener {

    private Map localTrans = new ConcurrentHashMap();

    /**
     * 消息发送之后，执行本地事务，并将本地事务执行结果发送给Broker
     *
     * @param msg
     * @param arg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        localTrans.put(msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID), 1);
        return RocketMQLocalTransactionState.UNKNOWN;
    }


    /**
     * 当本地事务状态为Unknown或者Broker未收到本地事务提交结果，
     * broker在等待一段时间之后，回调事务检查接口确认事务状态
     *
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        Integer key = (Integer) localTrans.get(msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID));
        if (key == 1) {
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }
}
