package fortest;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import rock.ForFig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class Producer {
    public static void main(String[] args) throws Exception {
        MixUtil mixUtil = new MixUtil();
        ForFig forFig = new ForFig();
        Properties properties = forFig.readProperties();
        final String nameserver = properties.getProperty("nameserver");
        Long begin = new Date().getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(begin))));

        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer(mixUtil.createGroupName("ProducerGroupName_"));
        //Launch the instance.
        producer.setNamesrvAddr(nameserver);
        producer.setDefaultTopicQueueNums(4);
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        List<Message> messages = new ArrayList<Message>();
        Message msg2 = new Message("TopicTest0523",
                "begin",
                "begin".getBytes());
        producer.send(msg2);
        for (int i = 0; i < 1; i++) {

            for(int j = 0; j < 1; j++){
            messages.add(new Message("TopicTest0523",
                    "TagA",
                    "HelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworldHelloworld".getBytes()));
        }
//            producer.send(messages);
//            messages.clear();
}
        Message msg = new Message("TopicTest0523",
                "stop1",
                "stop".getBytes());
//        producer.send(msg);
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
        Long end = new Date().getTime();
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd2 = sdf2.format(new Date(Long.parseLong(String.valueOf(end))));
        System.out.println("开始："+sd);
        System.out.println("结束："+sd2);
    }
}
