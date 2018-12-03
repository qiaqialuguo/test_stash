package rock;

import dao.Consumser_dao;
import org.apache.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.json.JSONObject;
import utils.ToLinux2;

import java.util.List;
import java.util.Properties;

public class Listening {
    public static Logger logger1 = Logger.getLogger(Listening.class);
    Consumser_dao c_dao=new Consumser_dao();

    public static void main(final String[] args) throws Exception {
        ForFig2 forFig = new ForFig2();
        Properties properties = forFig.readProperties();
        final String controltopic = properties.getProperty("controltopic");
        final String nameserver = properties.getProperty("nameserver");
        final String dataconsumer = properties.getProperty("consumer");
        final String ip = properties.getProperty(dataconsumer + "IP");

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(dataconsumer+"a");
        consumer.setNamesrvAddr(nameserver);
        consumer.setConsumeMessageBatchMaxSize(1000);
        consumer.subscribe(controltopic, "*");
        final ToLinux2 toLinux2 = new ToLinux2();
        final PushData2 pushData2 = new PushData2();
        consumer.registerMessageListener(new MessageListenerOrderly() {
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                for (MessageExt msg : msgs) {
                    try {
                        String topic = msg.getTopic();
                        String tags = msg.getTags();
                        String msgBody = new String(msg.getBody(), "utf-8");
//                        System.out.println("收到消息--" + " topic:" + topic + " ,tags:" + tags + " ,msg:" +msgBody);
//                        logger1.info("收到消息--" + " topic:" + topic + " ,tags:" + tags + " ,msg:" + msgBody);
                        if (tags.equals("beginning")) {

                            JSONObject jsonObject = new JSONObject(msgBody);
                            System.out.println(jsonObject);
                            String tablename = jsonObject.getString("tablename");
                            String datadate = jsonObject.getString("datadate");
                            String bigbatch = jsonObject.getString("num");
                            String fullfield = jsonObject.getString("fullfield");
                            String databases = jsonObject.getString("databases");
                            if ("".equals(databases)) {
                                try {
                                    toLinux2.runPushData2(tablename, datadate, bigbatch, dataconsumer, fullfield);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                String[] split = databases.split(",");
                                for (String base : split) {
                                    if (ip.equals(base)) {
                                        System.out.println(
                                                "kk"
                                        );
                                        try {
                                            toLinux2.runPushData2(tablename, datadate, bigbatch, dataconsumer, fullfield);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        logger1.info("没有指定此库");

                                    }
                                }
                            }

                        }
                    } catch (Exception e) {
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
