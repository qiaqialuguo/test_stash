package log;

import rock.Connect;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by 123 on 2018/8/26.
 */
public class producer_Log {

    public void insert_producer_log(String tablename,int status,String tx_date,String time,String log)  {
        Connection con = null;
        Connect connect = new Connect();
        try {

            con = connect.getStaticConnection();
            Statement statement = con.createStatement();
            String sql=String.format("insert into mq_producer_error_log(mq_job,status,tx_date,time,error) values('%s',%s,'%s','%s','%s')",tablename,status,tx_date,time,log);
            statement.executeUpdate(sql);
            con.setAutoCommit(false);
            con.commit();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        producer_Log p_log=new producer_Log();
        p_log.insert_producer_log("1",2,"3","20180828","6");
    }

}