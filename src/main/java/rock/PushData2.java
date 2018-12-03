package rock;

import dao.Consumser_dao;
import log.consumer_log;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import utils.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class PushData2 {
    static {
        PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator
                + "log4j.properties");
    }

    public static Logger logger1 = Logger.getLogger("rock");
    public static int i = 0;
    public static Integer total = 0;
    public static StringBuffer suffix = new StringBuffer();
    public static consumer_log c_log = new consumer_log();

    public static void main(final String[] args) throws Exception {
        final String tablename = args[0];
        final String datadate = args[1];
        final String bigbatch = args[2];
        final String dataconsumer = args[3];
        final String fullfield = args[4];
        ForFig2 forFig = new ForFig2();
        Properties properties = forFig.readProperties();
        final Consumser_dao consumser_dao = new Consumser_dao();
        consumser_dao.quary_mq_consumers(tablename, dataconsumer);

//        String mysqleverynumpre = properties.getProperty(tablename + "Mysqleverynum");
//        if(null==mysqleverynumpre){mysqleverynumpre = properties.getProperty("Mysqleverynum");}
//        else {mysqleverynumpre = properties.getProperty(tablename + "Mysqleverynum");}
//        final Integer mysqleverynum=Integer.parseInt(mysqleverynumpre);

        int batchnum = consumser_dao.consumer_num;
        final int mysqleverynum;
        if (batchnum > 0) {
            mysqleverynum = batchnum;
        } else {
            mysqleverynum = Integer.parseInt(properties.getProperty("everynum"));
        }
        final String stopword = properties.getProperty("stopword");
        final String datatopic = consumser_dao.topic;
        String deletesql = consumser_dao.delete_table;
        String Mysqltable = consumser_dao.target_table;
        String target_fileds = consumser_dao.target_fileds;
        ForSplit forSplit = new ForSplit();
        final String Separative_sign = forSplit.forSplit(tablename);
        DynamicSplicing dynamicSplicing = new DynamicSplicing();
        final String mysqltable = dynamicSplicing.splicedMysqlTable(datadate, Mysqltable);
        final String ip = consumser_dao.ip;
        final String controltopic = properties.getProperty("controltopic");
        final String nameserver = properties.getProperty("nameserver");
        //往MySQL记录情况
        final DmpDataRecord dmpDataRecord = new DmpDataRecord();
        //判断是否有消费者
        final BatchOrder batchOrder = new BatchOrder();
        String s = batchOrder.selectOrder(tablename, datadate, dataconsumer);
        if ("exit".equals(s)) {
            logger1.info(tablename + dataconsumer + "消费者正在运行");
            Long end = new Date().getTime();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
            String sd2 = sdf2.format(new Date(Long.parseLong(String.valueOf(end))));
            dmpDataRecord.sendToMysql(dataconsumer, tablename, ip, mysqltable, datadate, bigbatch, sd2, sd2, "1", "0");
            String status = "failed";
            consumser_dao.job_end(tablename, status, sd2, dataconsumer);
            System.exit(1);
        }
        if (null == datatopic) {
            logger1.info(tablename + dataconsumer + "此消费者不消费此数据");
            ForQuit forQuit = new ForQuit();
            forQuit.doShutDownWork2(tablename, datadate, dataconsumer);
            System.exit(1);
        }
//       //退出事件
        ForQuit forQuit = new ForQuit();
        forQuit.doShutDownWork(tablename, datadate, dataconsumer);
//        清理MySQL表
        DeleteMysql deleteMysql = new DeleteMysql();
        try {
            deleteMysql.deleteRepetition(tablename, datadate, dataconsumer, deletesql);
        } catch (Exception e) {
            e.printStackTrace();
            logger1.info("表出错" + e.toString() + "主题" + datatopic + bigbatch + "数据库" + ip);
            Long end = new Date().getTime();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
            String sd2 = sdf2.format(new Date(Long.parseLong(String.valueOf(end))));
            dmpDataRecord.sendToMysql(dataconsumer, tablename, ip, mysqltable, datadate, bigbatch, sd2, sd2, "1", "0");
            ForQuit forQuit2 = new ForQuit();
            forQuit2.doShutDownWork2(tablename, datadate, dataconsumer);
            String status = "failed";
            consumser_dao.job_end(tablename, status, sd2, dataconsumer);
            System.exit(1);
        }

        //发送控制信息
        final DefaultMQProducer producer = new DefaultMQProducer("producer1");
        producer.setNamesrvAddr(nameserver);
        producer.setDefaultTopicQueueNums(1);
        producer.start();
        Long begin = new Date().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        final String sd = sdf.format(new Date(Long.parseLong(String.valueOf(begin))));   // 时间戳转换成时间
        Message msg3 = new Message(controltopic,// topic
                "ending3",// tag
                ("topic加大批次号" + datatopic + bigbatch + "往MySQL开始时间：" + sd + "数据库" + ip).getBytes());// body
        producer.send(msg3);
        /**
         *
         * 记录任务开始状态
         */
        consumser_dao.job_start(tablename, datadate, sd, dataconsumer);
        //引入角标
        SelectField selectField = new SelectField();
        String changedfield = selectField.fieldChange(fullfield, target_fileds);
        final List<String> selectlist = selectField.selectlist(fullfield, target_fileds);
        final List<Integer> indexs = selectField.indexs(changedfield, selectlist);
        System.out.println(selectlist);
        //动态生成SQL
        String join = StringUtils.join(selectlist, "`,`");
        String field = String.format("(`%s`)", join);
        final String prefix = String.format("INSERT INTO %s %s VALUES ", mysqltable, field);
        //引入MySQL链接
        Connect connect = new Connect();
        final Connection conn = connect.getConnection(tablename, dataconsumer);
        // 设置事务为非自动提交
        conn.setAutoCommit(false);
        final PreparedStatement ps = (PreparedStatement) conn.prepareStatement("a");
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(dataconsumer + datatopic);
        consumer.setNamesrvAddr(nameserver);
        consumer.setConsumeMessageBatchMaxSize(1024);
        //设置消费模式
        consumer.setMessageModel(MessageModel.CLUSTERING);
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
//        consumer.setConsumeTimestamp("20180516200050");
        System.out.println(datadate + datatopic + bigbatch);
        consumer.subscribe(datatopic, datadate + bigbatch);
        consumer.registerMessageListener(new MessageListenerOrderly() {
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                String sql = "";
                try {

                    for (MessageExt msg : msgs) {
                        i = i + 1;
                        try {
                            String topic = msg.getTopic();
                            String tags = msg.getTags();
                            String msgBody = new String(msg.getBody(), "utf-8");
                            //替换并分割消息
                            String y = msgBody.replace("\\N", "null");
                            String z = y.replace("\\", "\\\\");
                            String a = z.replace("'", "\\'");
                            String[] sourceStrArray = a.split(Separative_sign, -1);
                            ArrayList<String> objects = new ArrayList<String>();
                            for (Integer index : indexs) {
                                objects.add(sourceStrArray[index]);
                            }
                            //生成拼接SQL
                            String join1 = StringUtils.join(objects, "','");
                            String field2 = String.format("('%s'),", join1);
                            suffix.append(field2);
//                        System.out.println("收到消息--" + " topic:" + topic + " ,tags:" + tags + " ,msg:" +msgBody)

                            if (sourceStrArray[0].equals(stopword)) {
                                //减去停止sql语句的长度
                                int size = selectlist.size();
                                List<String> selectlist2 = selectlist;
                                selectlist2.remove(0);
                                String join = StringUtils.join(selectlist2, "\t");
                                int joinlength = join.length();
                                int stoplength = suffix.length() - 3 - (3 * size) - (size * stopword.length());
                                if(stoplength != -1){
                                String presql = prefix + suffix.substring(0,stoplength);
                                sql = presql.replace("'null'", "null");
                                ps.addBatch(sql);
                                ps.executeBatch();
                                // 提交事务
                                conn.commit();}
//发送结束时间

                                Long end = new Date().getTime();
                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
                                String sd2 = sdf2.format(new Date(Long.parseLong(String.valueOf(end))));
//                    total=total-1;
                                Message msg32 = new Message(controltopic,// topic
                                        "ending3",// tag
                                        ("topic加大批次号" + datatopic + bigbatch + "总条数：" + total.toString() + "往MySQL结束时间：" + sd2 + "数据库" + ip).getBytes());// body
                                producer.send(msg32);
                                /**
                                 * 记录任务结束状态
                                 */

                                String staus = "done";

                                consumser_dao.job_end(tablename, staus, sd2, dataconsumer);

//                    ps.close();
//                    conn.close();
                                dmpDataRecord.sendToMysql(dataconsumer, tablename, ip, mysqltable, datadate, bigbatch, sd, sd2, "0", total.toString());

                                batchOrder.selectOrder4(tablename,dataconsumer);
                                System.exit(0);
                            }
                            if (i % mysqleverynum == 0) {
                                String presql = prefix + suffix.substring(0, suffix.length() - 1);
                                sql = presql.replace("'null'", "null");
                                ps.addBatch(sql);
                                //清空缓存
                                suffix.setLength(0);
                                ps.executeBatch();
                                // 提交事务
                                conn.commit();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            logger1.info("riewyriewrow" + e.toString());
                            Long end = new Date().getTime();
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
                            String sd2 = sdf2.format(new Date(Long.parseLong(String.valueOf(end))));
                            try {
                                dmpDataRecord.sendToMysql(dataconsumer, tablename, ip, mysqltable, datadate, bigbatch, sd, sd2, "1", total.toString());
                            } catch (Exception e2) {
                                e2.printStackTrace();
                                logger1.info("riewyriewrow2" + e2.toString());
                            }
                            String logs = e.getMessage();
                            String s = logs.substring(1, 255);

                            int statusnum = 0;
                            if (s == null) {
                                statusnum = 0;
                            } else {
                                statusnum = 1;
                            }
                            c_log.insert_consumer_log(tablename, statusnum, datadate, sd2, s);
                            String status = "failed";
                            consumser_dao.job_end(tablename, status, sd2, dataconsumer);

                            logger1.info(sql);
                        }
                        total += 1;
                    }

                    ps.executeBatch();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger1.info(sql);
                    logger1.info("riewyriewrow3" + e.toString());

                    Long end = new Date().getTime();
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
//                    String logs = e.getMessage();
//                    String s = logs.substring(1, 255);
//                    int status = 0;
//                    if (s == null) {
//                        status = 0;
//                    } else {
//                        status = 1;
//                    }
                    String sd2 = sdf2.format(new Date(Long.parseLong(String.valueOf(end))));
//                    c_log.insert_consumer_log(tablename, status, datadate, sd2, s);
                    try {
                        dmpDataRecord.sendToMysql(dataconsumer, tablename, ip, mysqltable, datadate, bigbatch, sd, sd2, "1", total.toString());
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        logger1.info("riewyriewrow4" + e2.toString());
                    }
                    ForQuit forQuit2 = new ForQuit();
                    forQuit2.doShutDownWork2(tablename, datadate, dataconsumer);
//                    c_log.insert_consumer_log(tablename, 1, datadate, sd2, s);
                    String status = "failed";
                    consumser_dao.job_end(tablename, status, sd2, dataconsumer);
                    System.exit(1);
                }

                return ConsumeOrderlyStatus.SUCCESS;

            }
        });

        consumer.start();
        System.out.println("Consumer Started.");
    }
}
