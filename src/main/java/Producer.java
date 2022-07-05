import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import static org.apache.rocketmq.client.producer.SendStatus.SEND_OK;

public class Producer {

    private String topic;

    private Message message;

    private String instance;

    private DefaultMQProducer defaultMQProducer;

    public Producer(String producerKey) throws MQClientException {
        topic =  Constant.TOPIC_PREFIX + producerKey;
        instance = Constant.PRODUCER_INSTANCE_PREFIX + producerKey;
        message = new Message(topic, Constant.MSG.getBytes());
        startInstance();
    }

    private void startInstance() throws MQClientException {
        defaultMQProducer = new DefaultMQProducer(instance);
        defaultMQProducer.setInstanceName(instance);
        defaultMQProducer.setNamesrvAddr(Constant.nameSrvAddr);
        defaultMQProducer.setRetryTimesWhenSendFailed(Constant.RETRY_TIMES);
        defaultMQProducer.setSendMsgTimeout(Constant.TIMEOUT);
        defaultMQProducer.start();
    }

    public Boolean send() {
        try {
            if (defaultMQProducer.send(message).getSendStatus().equals(SEND_OK)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public void close() {
        defaultMQProducer.shutdown();
    }

}
