package rock;

import dao.Producer_Dao;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.json.JSONObject;
import utils.BatchOrder;
import utils.GetBatchNum;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class ControlProduct {
    static {
        PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator
                + "log4j.properties");
    }
    public static Logger logger1 = Logger.getLogger("ReadData");
    public static void main(String[] args) throws Exception {
        String tablename = args[0];
        String datadate = args[1];
        String databases ="";
        if(args.length==3){databases=args[2];}
        //引入配置参数
        ForFig forFig = new ForFig();
        Properties properties = forFig.readProperties();
        Producer_Dao producer_dao = new Producer_Dao();
        producer_dao.quary_producers(tablename);
        producer_dao.quary_producer(tablename);
        String fullfield = "";
        String model = producer_dao.type;
        final String controltopic = properties.getProperty("controltopic");
        final String nameserver = properties.getProperty("nameserver");
        DefaultMQProducer producer = new DefaultMQProducer("group_name");

        BatchOrder batchOrder = new BatchOrder();
        producer.setNamesrvAddr(nameserver);
        //设置队列数
        producer.setDefaultTopicQueueNums(1);
        producer.start();
        //获取大批次号
        GetBatchNum getBatchNum = new GetBatchNum();
        String num="";
        try {
            num = getBatchNum.getNum(tablename);
        } catch (Exception e) {
            e.printStackTrace();
            logger1.info("任务名不正确或url地址错误");
            System.exit(1);
        }
        if ("hdfs".equals(model)) {
            ReadData readData = new ReadData();
            fullfield = readData.readData(tablename, datadate, num);
        } else if ("hive".equals(model) || "mysql".equals(model)) {
            ReadDataJDBC readDataJDBC = new ReadDataJDBC();
            fullfield = readDataJDBC.readDataJDBC(tablename, datadate, num);
        } else {
            logger1.info("数据源拼写错误");
            System.exit(1);
        }
        try {
            Long begin = new Date().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
            String sd = sdf.format(new Date(Long.parseLong(String.valueOf(begin))));

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tablename",tablename);
            jsonObject.put("datadate",datadate);
            jsonObject.put("num",num);
            jsonObject.put("sd",sd);
            jsonObject.put("fullfield",fullfield);
            jsonObject.put("databases",databases);

            Message msg = new Message(controltopic,              // topic
                    "beginning",                                     // tag
                    (jsonObject.toString()).getBytes()
//            (tablename + "," + datadate + "," + num + "," + sd).getBytes()    // body
                    // body
            );
            SendResult send = producer.send(msg);
            System.out.println(send);


        } catch (Exception e) {
            e.printStackTrace();
            Thread.sleep(1000);
            System.exit(1);

        }

        producer.shutdown();
        if("".equals(fullfield)){
            System.out.println("status:2");
            System.exit(2);
        }
        while (true){
            Thread.sleep(10*1000);
            String s = batchOrder.selectOrder3(tablename, datadate);
            if("yesnow".equals(s)){
                System.out.println("status:0");
                break;
            }
            if("wrong".equals(s)){
                System.out.println("status:1");
                System.exit(1);
            }
        }
    }
}
