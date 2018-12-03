package dao;

import entry.mq_producer;
import entry.mq_producers;
import rock.Connect;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/8/22.
 *
 * 生产配置读取
 */
public class Producer_Dao extends basedao {

    /**
     *
     * 生产者配置读取
     * @param mq_job
     * @return
     */
   public   String separative_sign;
    public  String datafields;
    public  Integer everynum  ;
    public  String path ;
    public   String producer  ;
    public   String mq_topic  ;
    public   String type  ;
    public   String description  ;
    public List<mq_producers> quary_producers(String mq_job){
        List<mq_producers> producer_lists =new ArrayList<mq_producers>();
        String sql=String.format("select * from mq_producers  where mq_job='%s'and ennable=1;",mq_job);
        System.out.println(sql);
        producer_lists=executeQuery(sql,mq_producers.class);
         separative_sign = producer_lists.get(0).getSeparative_sign();
         datafields = producer_lists.get(0).getDatafields();
         everynum = producer_lists.get(0).getEverynum();
         path = producer_lists.get(0).getPath();
         producer = producer_lists.get(0).getProducer();
         mq_topic = producer_lists.get(0).getMq_topic();
        return producer_lists;
    }
    /**
     * 生产者类型配置
     *
     */
    public List<mq_producer> quary_producer(String mq_job) {
        Producer_Dao p_dao=new Producer_Dao();
        String producer=p_dao.quary_producers(mq_job).get(0).getProducer();
        List<mq_producer> producer_list = new ArrayList<mq_producer>();
        String sql = String.format("select * from mq_producer  where producer='%s'",producer)  ;
        producer_list = executeQuery(sql, mq_producer.class);
         type = producer_list.get(0).getType();
         description = producer_list.get(0).getDescription();
        return producer_list;
    }

    public void job_start(String tablename,String last_txdate,String starttime){
        Connect connect = new Connect();
        Statement statement=null;
        try {
            conn = connect.getStaticConnection();
             statement = conn.createStatement();
            String sql=String.format("update mq_producers set last_jobstatus='running',last_txdate='%s',last_starttime='%s',last_endtime=null where mq_job='%s'",last_txdate,starttime,tablename);
           System.out.println(sql);

            statement.executeUpdate(sql);
            conn.setAutoCommit(false);
            conn.commit();

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            connect.closeRes(conn,statement);
        }

    }

public void job_end(String tablename,String job_status ,String endtime ){
        Connect connect=new Connect();
    Statement statement=null;
     try{
         conn=connect.getStaticConnection();
          statement = conn.createStatement();
         String sql=String.format("update mq_producers set last_jobstatus='%s',last_endtime='%s'where mq_job='%s'",job_status,endtime,tablename);
         statement.executeUpdate(sql);
         conn.setAutoCommit(false);
         conn.commit();
     } catch (Exception e){
         e.printStackTrace();
     }
    finally {
         connect.closeRes(conn,statement);
     }
    }


}

