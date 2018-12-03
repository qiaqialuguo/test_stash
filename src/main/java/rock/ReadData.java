package rock;

import log.producer_Log;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import utils.ForSplit;
import utils.GetBatchNum;
import utils.ToLinux2;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import dao.Producer_Dao;

import static rock.TravFile.listAll;

public class ReadData {
    static {
        PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator
                + "log4j.properties");
    }
    public static Logger logger1 = Logger.getLogger("ReadData");

    public String readData(String tablename, String datadate, String bigbatch) throws Exception {
//        final String tablename = args[0];
//        final String datadate = args[1];
//        final String bigbatch = args[2];
        ForFig forFig = new ForFig();
        Properties properties = forFig.readProperties();
        Producer_Dao producer_dao = new Producer_Dao();
        producer_dao.quary_producers(tablename);
        producer_dao.quary_producer(tablename);
        producer_Log p_log=new producer_Log();

        int everynum=0;
        if(producer_dao.everynum>0){
            everynum =producer_dao.everynum;
        }else{
            everynum =Integer.parseInt(properties.getProperty("everynum")) ;
        }

        final String datatopic = producer_dao.mq_topic;
        String datafields = producer_dao.datafields;
        ForSplit forSplit = new ForSplit();
        String Separative_sign  = forSplit.forSplit(tablename);
        final String controltopic = properties.getProperty("controltopic");
        final String stopword = properties.getProperty("stopword");
        final String nameserver = properties.getProperty("nameserver");

        //通过工具类读hdfs目录
        try {
            final String HA=producer_dao.description;
            final String path = producer_dao.path;
            String dir=path.replace("${TX_DT}",datadate).trim();
            dir=HA+dir;
            List<String> names = TravFile.listAll(dir, tablename, datadate,HA,path);
            if (0 == names.size()) {
                logger1.info(tablename + "目录无文件");
                String log=tablename+"目录无文件";
                Long end = new Date().getTime();
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
                String sd2 = sdf2.format(new Date(Long.parseLong(String.valueOf(end))));
                p_log.insert_producer_log(tablename,1,datadate,sd2,log);
                System.exit(1);
            }


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
                    ("topic加大批次号" + datatopic + bigbatch + "读HDFS开始时间：" + sd).getBytes());// body
            producer.send(msg3);

            producer_dao.job_start(tablename,datadate,sd);
            Integer total = 0;

            Configuration conf = new Configuration();
            // TODO Auto-generated method stub
            FileSystem fs = null;
            FSDataInputStream in = null;
            BufferedReader br = null;
            Integer i = 1;
            for (String fullurl : names) {
                fs = FileSystem.get(URI.create(fullurl), conf);
                in = fs.open(new Path(fullurl));
                br = new BufferedReader(new InputStreamReader(in));
                String line = "";
                String[] arrs = null;
                String mes = null;
                byte[] bytes = new byte[]{1};
                String sendString = new String(bytes, "UTF-8");
                arrs = line.split(sendString);
                List<Message> messages = new ArrayList<Message>();
                int j = 0;
                while (j < everynum) {
                    if ((mes = br.readLine()) != null) {
                        total += 1;
                        messages.add(new Message(datatopic,// topic
                                datadate + bigbatch,// tag
                                (mes).getBytes()// body
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
                System.out.println(arrs[0]);

                br.close();
                fs.close();

            }

            if (1 == 1) {
                SelectField selectField = new SelectField();
                List<String> alllist = selectField.alllist(datafields);
                List<String> alllist2 = new ArrayList<String>();
                for (int j = 0; j < alllist.size() - 1; j++) {
                    alllist2.add(stopword);
                }
                String join = StringUtils.join(alllist2, Separative_sign);
                Message msg2 = new Message(datatopic,// topic
                        datadate + bigbatch,// tag
                        ("sttoop"+Separative_sign+ join).getBytes());// body
                producer.send(msg2);
                //结束时间
                Long end = new Date().getTime();
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
                String sd2 = sdf2.format(new Date(Long.parseLong(String.valueOf(end))));
                Message msg32 = new Message(controltopic,// topic
                        "ending3",// tag
                        ("topic加大批次号" + datatopic + bigbatch + "总条数：" + total.toString() + "读HDFS结束时间：" + sd2).getBytes());// body
                producer.send(msg32);
                String staus= "done";
                /**
                 * 任务结束更新任务状态
                 */
                producer_dao.job_end(tablename,staus,sd2);
            }

            //发送完消息之后，调用shutdown()方法关闭producer
            producer.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            logger1.info("riewyriewrow2" + e.toString());

            String logs=e.getMessage();
            System.out.println(logs);
            String log =logs.substring(0,100);
            Long end = new Date().getTime();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
            /**
             * 异常捕获信息时间
             */
            String sd2 = sdf2.format(new Date(Long.parseLong(String.valueOf(end))));
            int statusnum=1;
            /**
             * 写入日志表
             */
            p_log.insert_producer_log(tablename,statusnum,datadate,sd2,log);
            String status="false";
            producer_dao.job_end(tablename,status,sd2);

            System.exit(1);
        }
        return datafields;
    }

    public static void main(String[] args) throws Exception {
        ReadData readData = new ReadData();
        String s = readData.readData("jrtt_account", "20181124", "1");
        System.out.println(s);
    }
}
