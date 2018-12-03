package dao;

import entry.mq_consumer;
import entry.mq_consumers;
import kafka.consumer.Consumer;
import rock.Connect;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/8/22.
 * 消费者组合消费者读取配置表里的配置
 *
 */
public class Consumser_dao  extends basedao {

    /**
     * 消费者组配置读取
     * @return
     */

    public  Integer consumer_num ;
    public    String target_fileds;
    public   String target_table ;
    public     String ip ;
    public  String topic ;
    public     String type ;
    public  String description ;
    public  String delete_table ;
    public   List<mq_consumers> quary_mq_consumers(String consumer_job, String consumer){
        List<mq_consumers>   mq_consumers=new ArrayList<entry.mq_consumers>();
        String sql="select * from mq_consumer_job where consumer_job='"+consumer_job+"'  and consumer='"+consumer+"' and ennable=1";
        System.out.println(sql);
        mq_consumers=executeQuery(sql,mq_consumers.class);
         consumer_num = mq_consumers.get(0).getEverynum();
         target_fileds = mq_consumers.get(0).getTarget_fileds();
         target_table = mq_consumers.get(0).getTarget_table();
         ip = mq_consumers.get(0).getConsumer();
         topic = mq_consumers.get(0).getTopic();
         delete_table = mq_consumers.get(0).getDelete_table();





        return mq_consumers ;
    }

    /**
     * 消费者类类型
     */
    public  List<mq_consumer> quary_mq_consumer(String consumer){
        List<mq_consumer>   mq_consumer=new ArrayList<entry.mq_consumer>();
        String sql="select * from mq_consumer where consumer='"+consumer+"'";
        mq_consumer=executeQuery(sql,mq_consumer.class);
         type = mq_consumer.get(0).getType();
         description = mq_consumer.get(0).getDescription();
        return mq_consumer;
    }

    public  void job_start(String consumser_job,String last_txdate,String starttime,String consumer){
        Consumser_dao c_dao=new Consumser_dao();
        Connect connect = new Connect();
      String  consumer_job=  c_dao.quary_mq_consumers(consumser_job,consumer).get(0).getConsumer_job();

        try {
            conn = connect.getStaticConnection();
            Statement statement = conn.createStatement();
            String sql=String.format("update mq_consumer_job set last_jobstatus='running',last_txdate='%s',last_starttime='%s',last_endtime=null where consumer_job='%s'and consumer='%s' ",last_txdate,starttime,consumer_job,consumer);
            System.out.println(sql);

            statement.executeUpdate(sql);
            conn.setAutoCommit(false);
            conn.commit();

            conn.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  void job_end(String consumser_job,String job_status ,String endtime,String consumer ){
        Consumser_dao c_dao=new Consumser_dao();
        Connect connect=new Connect();
        String  consumer_job=  c_dao.quary_mq_consumers(consumser_job,consumer).get(0).getConsumer_job();
        try{
            conn=connect.getStaticConnection();
            Statement statement = conn.createStatement();
            String sql=String.format("update mq_consumer_job set last_jobstatus='%s',last_endtime='%s'where consumer_job='%s'and consumer='%s'",job_status,endtime,consumer_job,consumer);
            statement.executeUpdate(sql);
            conn.setAutoCommit(false);
            conn.commit();
            conn.close();
            statement.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
