package rock;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import utils.ToLinux2;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OnlyListening {
    static {
        PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator
                + "log4j.properties");
    }

    public static void main(final String[] args) throws Exception {
        ForFig2 forFig = new ForFig2();
        Properties properties = forFig.readProperties();
        final String controltopic = properties.getProperty("controltopic");
        final String nameserver = properties.getProperty("nameserver");
        final String dataconsumer = properties.getProperty("consumer");
        final String databases = properties.getProperty("databases");

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(dataconsumer + "a");
        consumer.setNamesrvAddr(nameserver);
        consumer.setConsumeMessageBatchMaxSize(1000);
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
//        consumer.setConsumeTimestamp("20180516200050");
        consumer.subscribe(controltopic, "*");
        consumer.registerMessageListener(new MessageListenerOrderly() {
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                for (MessageExt msg : msgs) {
                    try {
                        String topic = msg.getTopic();
                        String tags = msg.getTags();
                        String msgBody = new String(msg.getBody(), "utf-8");

                        System.out.println("收到消息--" + " topic:" + topic + " ,tags:" + tags + " ,msg:" + msgBody);
                        String[] split = databases.split(",");
                        List<String> strings = new ArrayList<String>();
                        if (msgBody.contains("数据库")) {
                            for (String splited : split) {
                                if (msgBody.substring(msgBody.indexOf("库") + 1).equals(splited)) {
                                    Logger.getLogger(splited).info("收到消息--" + " topic:" + topic + " ,tags:" + tags + " ,msg:" + msgBody);
                                }
                            }
                        } else {
                            Logger.getLogger("ReadData").info("收到消息--" + " topic:" + topic + " ,tags:" + tags + " ,msg:" + msgBody);
                        }

//                        if(msgBody.substring(msgBody.indexOf("库")+1))
//                        logger1.info("收到消息--" + " topic:" + topic + " ,tags:" + tags + " ,msg:" + msgBody);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
        System.out.println("Consumer Started.");
    }
}
