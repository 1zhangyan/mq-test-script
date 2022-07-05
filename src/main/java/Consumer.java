import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class Consumer {

    private Admin admin;

    private String topic;

    private String group;

    private String instance;

    private DefaultMQPushConsumer defaultMQPushConsumer;

    public Consumer (String key, Admin admin) throws MQClientException {
        this.admin = admin;
        topic = Constant.TOPIC_PREFIX + key;
        group = Constant.GROUP_PREFIX + key;
        instance = Constant.CONSUMER_INSTANCE_PREFIX + key;
        admin.checkTopicAndConsumer(topic, group);
        start();
    }

    public class MessageListenerImpl implements MessageListenerConcurrently {

        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                                                        ConsumeConcurrentlyContext consumeConcurrentlyContext) {
            for(MessageExt msg : list) {
                long endTime = System.currentTimeMillis();
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }

    public void start() throws MQClientException {
        defaultMQPushConsumer = new DefaultMQPushConsumer(group);
        defaultMQPushConsumer.setNamesrvAddr(Constant.nameSrvAddr);
        defaultMQPushConsumer.subscribe(topic, "*");
        defaultMQPushConsumer.setInstanceName(instance);
        defaultMQPushConsumer.registerMessageListener(new MessageListenerImpl());
        defaultMQPushConsumer.start();
    }
}
