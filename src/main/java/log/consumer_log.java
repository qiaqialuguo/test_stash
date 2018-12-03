package log;

import rock.Connect;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by 123 on 2018/8/26.
 *
 * 往消费者日志表插入数据
 *
 *
 */
public class consumer_log {

    public void insert_consumer_log(String tablename,int status,String tx_date,String time,String log)  {
        Connection con = null;
        Connect connect = new Connect();
        try {

            con = connect.getStaticConnection();
            Statement statement = con.createStatement();
            String sql=String.format("insert into mq_consumer_error_log(consumer,status,tx_date,time,error) values('%s','%s','%s','%s','%s')",tablename,status,tx_date,time,log);
            System.out.println("log+"+sql);
            statement.execute(sql);
            con.setAutoCommit(false);
            con.commit();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
