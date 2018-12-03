package utils;

import rock.Connect;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DmpDataRecord {
    public void sendToMysql(String dataconsumer,String tablename,String ip,String mysqlname,String datadate,String bigbatch,String starttime,String endtime,String status,String row_cnt) throws Exception{
        Connect connect = new Connect();
        Connection con = connect.getConnection(tablename,dataconsumer);
        Statement statement = con.createStatement();
        String sql = String.format("INSERT INTO dmp_data_record (tb_name,tx_date,banch_id,starttime,endtime,status,row_cnt) VALUES ('%s','%s','%s','%s','%s','%s','%s');",tablename, datadate,bigbatch,starttime,endtime,status,row_cnt);
        statement.executeUpdate(sql);
        con.close();
        RecordToHttp recordToHttp = new RecordToHttp();
        recordToHttp.recordToHttp(ip,tablename,mysqlname,datadate,bigbatch,starttime,endtime,status,row_cnt);
//        System.out.println("aa");

    }

    public static void main(String[] args) {
        Long end = new Date().getTime();
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        String sd2 = sdf2.format(new Date(Long.parseLong(String.valueOf(end))));
        DmpDataRecord dmpDataRecord = new DmpDataRecord();
        try {

            dmpDataRecord.sendToMysql("dataconsumer3","1","1","1","1","1","1",sd2,"1","1");
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("qqqq");
        }
    }

}
