package rock;

import dao.Producer_Dao;
import log.producer_Log;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import utils.DynamicSplicing;
import utils.ForSplit;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class ReadDataJDBC {
    static {
        PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator
                + "log4j.properties");
    }

    public static Logger logger1 = Logger.getLogger("ReadData");


    public String readDataJDBC(String tablename, String datadate, String bigbatch) throws Exception {
        ForFig forFig = new ForFig();
        Properties properties = forFig.readProperties();
        Producer_Dao p_dao = new Producer_Dao();
        p_dao.quary_producers(tablename);
        p_dao.quary_producer(tablename);
        int everynum = 0;
        if (p_dao.everynum > 0) {
            everynum = p_dao.everynum;
        } else {
            everynum = Integer.parseInt(properties.getProperty("everynum"));
        }
        final String datatopic = p_dao.mq_topic;

        final String nameserver = properties.getProperty("nameserver");
        final String controltopic = properties.getProperty("controltopic");
        final String stopword = properties.getProperty("stopword");


        final String model = p_dao.type;
        final String allurl = p_dao.description;
        String[] splits = allurl.split("-t");
        String dataurl = splits[0];
        String user = splits[1];
        String pwd = splits[2];
        System.out.println("数据库连接" + dataurl + "账户" + user + "密码" + pwd);

        DynamicSplicing dynamicSplicing = new DynamicSplicing();
        String join1="";
        try {
            if ("hive".equals(model)) {
                Class.forName("org.apache.hive.jdbc.HiveDriver");
            } else {
                Class.forName("com.mysql.jdbc.Driver");
            }

//            String selectSql = "select * from ods.gdt_hourly_reports_advertiser where tx_dt=20180822 limit 2";
            String fullurl = "";

            if ("hive".equals(model)) {
                fullurl = "jdbc:hive2://" + dataurl;
                System.out.println(fullurl);
            } else {
                fullurl = "jdbc:mysql://" + dataurl + "?rewriteBatchedStatements=true";
            }
            Connection connect = DriverManager.getConnection(fullurl.trim(), user, pwd);

            String s = p_dao.path;
            String sql = s.replace("${TX_DT}", datadate);
            PreparedStatement state = null;
//            System.out.println(sql);
            state = connect.prepareStatement(sql);
            ResultSet resultSet = state.executeQuery();
            ForSplit forSplit = new ForSplit();
            String separative_sign  = forSplit.forSplit(tablename);

            //声明并初始化一个producer
            //需要一个producer group名字作为构造方法的参数，这里为producer1
            DefaultMQProducer producer = new DefaultMQProducer("producer1");
            //设置NameServer地址,此处应改为实际NameServer地址，多个地址之间用；分隔
            //NameServer的地址必须有，但是也可以通过环境变量的方式设置，不一定非得写死在代码里
            producer.setNamesrvAddr(nameserver);
            producer.setDefaultTopicQueueNums(1);
            //调用start()方法启动一个producer实例
            producer.start();

            Long begin = new Date().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
            String sd = sdf.format(new Date(Long.parseLong(String.valueOf(begin))));   // 时间戳转换成时间
            Message msg3 = new Message(controltopic,// topic
                    "ending3",// tag
                    ("topic加大批次号" + datatopic + bigbatch + "读HIVE开始时间：" + sd).getBytes());// body
            producer.send(msg3);
            p_dao.job_start(tablename, datadate, sd);

            int columnCount = 0;
            Integer total = 0;
            if (resultSet != null) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                columnCount = metaData.getColumnCount();
                ArrayList<String> objects = new ArrayList<String>();
                for (Integer i = 1; i <= columnCount; i++) {
                    String[] split = (metaData.getColumnName(i)).split("\\.");
                    if (split.length == 2) {
                        objects.add(split[1]);
                    } else {
                        objects.add(split[0]);
                    }
                }
                join1 = StringUtils.join(objects, ",");
                List<Message> messages = new ArrayList<Message>();
                int j = 0;
                while (j < everynum) {
                    if (resultSet.next()) {
                        ArrayList<String> objects2 = new ArrayList<String>();
                        for (Integer i = 1; i <= columnCount; i++) {
                            String resultSetString = resultSet.getString(i);
                            if (null==resultSetString){
                                resultSetString = "\\N";
                            }
                            objects2.add(resultSetString);
                        }
                        String join2 = StringUtils.join(objects2, separative_sign);
//                        System.out.println(join2);
                        total += 1;
                        messages.add(new Message(datatopic,// topic
                                datadate + bigbatch,// tag
                                (join2).getBytes()// body
                        ));
                        j++;
                        if (j >= everynum) {
                            producer.send(messages);
                            messages.clear();
                            j = 0;
                        }
                    } else {
                        if (messages.size() != 0) {
                            producer.send(messages);
                            messages.clear();
                            j = everynum;
                        } else {
                            j = everynum;
                        }
                    }
                }

                List<String> alllist2 = new ArrayList<String>();
                for (int j2 = 0; j2 < columnCount - 1; j2++) {
                    alllist2.add(stopword);
                }
                String join = StringUtils.join(alllist2, separative_sign);
                Message msg2 = new Message(datatopic,// topic
                        datadate + bigbatch,// tag
                        ("sttoop" + separative_sign + join).getBytes());// body
                producer.send(msg2);
                //结束时间
                Long end = new Date().getTime();
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
                String sd2 = sdf2.format(new Date(Long.parseLong(String.valueOf(end))));
                Message msg32 = new Message(controltopic,// topic
                        "ending3",// tag
                        ("topic加大批次号" + datatopic + bigbatch + "总条数：" + total.toString() + "读HIVE结束时间：" + sd2).getBytes());// body
                if (0==total){
                    logger1.info("数据量为0");
                    System.exit(1);}
                String staus = "done";
                /**
                 * 任务结束更新任务状态
                 */
                producer_Log p_log = new producer_Log();
                p_dao.job_end(tablename, staus, sd2);
                producer.send(msg32);

                //发送完消息之后，调用shutdown()方法关闭producer
                producer.shutdown();
            } else {
                logger1.info("sql查询无数据");
                System.exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger1.info("riewyriewro2" + e.toString());
            String logs = e.getMessage();
            System.out.println(logs);
            String log = logs.substring(0, 100);
            Long end = new Date().getTime();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
            /**
             * 异常捕获信息时间
             */
            String sd2 = sdf2.format(new Date(Long.parseLong(String.valueOf(end))));
            int statusnum = 1;
            /**
             * 写入日志表
             */
            producer_Log p_log = new producer_Log();
            p_log.insert_producer_log(tablename, statusnum, datadate, sd2, log);
            String status = "false";
            p_dao.job_end(tablename, status, sd2);
            System.exit(1);

        }
return join1;
    }

    public static void main(String[] args) throws Exception {
        ReadDataJDBC readDataJDBC = new ReadDataJDBC();
        readDataJDBC.readDataJDBC("gdt_material_report_daily", "20181029", "1");
    }
}
