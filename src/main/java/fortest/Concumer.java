package fortest;

import jdk.nashorn.internal.objects.Global;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import rock.ForFig;
import rock.PushData2;
import utils.ToLinux2;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class Concumer {

    private static long start = System.currentTimeMillis();

    private volatile static long end = start + 40*1000;
    static int i=1;

    public static void main(final String[] args) throws Exception {

        ForFig forFig = new ForFig();
        Properties properties = forFig.readProperties();
        final String controltopic = properties.getProperty("controltopic");
        final String nameserver = properties.getProperty("nameserver");

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("a1");
        consumer.setNamesrvAddr(nameserver);
        consumer.setConsumeMessageBatchMaxSize(1000);

        consumer.subscribe("gdt_funds_v2", "*");
        consumer.registerMessageListener(new MessageListenerOrderly() {
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                for (MessageExt msg: msgs) {
                    try {
                        String topic = msg.getTopic();
                        String tags = msg.getTags();
                        String msgBody = new String(msg.getBody(),"utf-8");
                        System.out.println(msgBody);
                        if (tags.equals("begin")){Long begin = new Date().getTime();
                            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String sd = sdf.format(new Date(Long.parseLong(String.valueOf(begin))));
                            System.out.println("开始："+sd);}
                        if (tags.equals("stop")){Long end = new Date().getTime();
                            SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String sd2 = sdf2.format(new Date(Long.parseLong(String.valueOf(end))));
                            System.out.println("结束："+sd2);
                        }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    end = System.currentTimeMillis() + 40*1000;
                    System.out.println(end);
                    System.out.println("111111111111111");
                    i=i+1;
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
        System.out.println("=================================");
        System.out.println("Consumer Started.");
//        while (true){
////            Thread.sleep(1*1000);
//            long a=System.currentTimeMillis();
////            System.out.println(a);
////            System.out.println(end);
//            if(a >= end) {
//                System.out.println(a>=end);
//                System.out.println(a);
//                System.out.println(end);
//                System.exit(1);
//            }
//        }
    }
}
