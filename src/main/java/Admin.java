import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.TopicConfig;
import org.apache.rocketmq.common.protocol.route.BrokerData;
import org.apache.rocketmq.common.subscription.SubscriptionGroupConfig;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class Admin {

    private DefaultMQAdminExt defaultMQAdminExt;

    private Set<String> topicList;

    private String brokerName;

    private String singleMasterAddr;

    private ConcurrentMap<String, TopicConfig> topicConfigTable;

    public Admin() {
        defaultMQAdminExt = new DefaultMQAdminExt(5000L);
        defaultMQAdminExt.setInstanceName("admin-" + System.currentTimeMillis());
        defaultMQAdminExt.setNamesrvAddr(Constant.nameSrvAddr);
        try {
            defaultMQAdminExt.start();
            topicList = defaultMQAdminExt.fetchAllTopicList().getTopicList();
            BrokerData brokerData = defaultMQAdminExt.examineBrokerClusterInfo().getBrokerAddrTable().values().stream().collect(Collectors.toList()).get(0);
            brokerName = brokerData.getBrokerName();
            singleMasterAddr = brokerData.getBrokerAddrs().get(0L);
            topicConfigTable = defaultMQAdminExt.getAllTopicGroup(singleMasterAddr, 10000).getTopicConfigTable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void checkTopicAndConsumer(String topic, String group) throws MQClientException {
        boolean exist = false;
        try {
            exist = defaultMQAdminExt.getAllTopicGroup(singleMasterAddr, 3000L)
                    .getTopicConfigTable().containsKey(topic);
            if (!exist) {
                createTopicAndConsumerGroupOnBroker(topic, group);
            }
        } catch (Throwable t) {
           new RuntimeException(t);
        }
    }

    private void createTopicAndConsumerGroupOnBroker(String topic, String group) throws Throwable{
        TopicConfig topicConfig = new TopicConfig(topic, 5, 5, 6);
        defaultMQAdminExt.createAndUpdateTopicConfig(singleMasterAddr, topicConfig);
        SubscriptionGroupConfig config = new SubscriptionGroupConfig();
        config.setGroupName(group);
        defaultMQAdminExt.createAndUpdateSubscriptionGroupConfig(singleMasterAddr, config);
    }

}
