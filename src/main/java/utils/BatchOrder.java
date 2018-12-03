package utils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import rock.Connect;
import rock.Listening;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class BatchOrder {
    static {
        PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator
                + "log4j.properties");
    }
    public static Logger logger1 = Logger.getLogger("rock");
    public String selectOrder(String tablename,String datadate,String dataconsumer) throws Exception{
        GetBatchNum getBatchNum = new GetBatchNum();
        Connect connect = new Connect();
        Connection con = connect.getStaticConnection();
        Statement statement = con.createStatement();

//            执行查询语句
        String sql = String.format("select * from batchorder where name='%s00' and consumer='%s';",tablename,dataconsumer);//我的表格叫persons
        ResultSet resultSet = statement.executeQuery(sql);
        String num;
        if (resultSet.first()) {
            //            执行插入语句
            num = getBatchNum.realGetNum(tablename);
//            String sql2 = String.format("INSERT INTO batchorder (name,date,num,consumer) VALUES ('%s','%s','%s','%s');",tablename,datadate,num,dataconsumer);
//            statement.executeUpdate(sql2);
            resultSet.close();
            con.close();
            return "exit";
        } else {
            String sql2 = String.format("INSERT INTO batchorder (name,consumer) VALUES ('%s00','%s');",tablename,dataconsumer);
            statement.executeUpdate(sql2);
            String sql3 = String.format("select * from batchorder where name='%s33' and consumer='%s';",tablename,dataconsumer);//我的表格叫persons
            ResultSet resultSet3 = statement.executeQuery(sql3);
            if (resultSet3.first()) {
                String sql4 = String.format("delete from batchorder where name ='%s33' and consumer='%s'", tablename,dataconsumer);
                statement.executeUpdate(sql4);
                String sql5 = String.format("INSERT INTO batchorder (name,consumer) VALUES ('%s33','%s');",tablename,dataconsumer);
                statement.executeUpdate(sql5);
            }else {
                String sql6 = String.format("INSERT INTO batchorder (name,consumer) VALUES ('%s33','%s');",tablename,dataconsumer);
                statement.executeUpdate(sql6);
            }
            resultSet3.close();
            con.close();
            return "hi";
        }

    }

    public String selectOrder2(String tablename,String datadate,String dataconsumer,String fullfields) throws Exception{
        GetBatchNum getBatchNum = new GetBatchNum();
        Connect connect = new Connect();
        Connection con = connect.getStaticConnection();
        Statement statement = con.createStatement();
//            执行查询语句
        String sql = String.format("select * from batchorder where name='%s00' and consumer='%s';",tablename,dataconsumer);//我的表格叫persons
        ResultSet resultSet = statement.executeQuery(sql);
        String num;
        if (resultSet.first()) {
            //            执行插入语句
//            num = getBatchNum.realGetNum(tablename);
//            String sql2 = String.format("INSERT INTO batchorder (name,date,num,consumer) VALUES ('%s','%s','%s','%s');",tablename,datadate,num,dataconsumer);
//            statement.executeUpdate(sql2);
//            resultSet.close();
//            con.close();
            return "exit";
//            System.exit(1);
        } else {
            String sql3 = String.format("select * from batchorder where name ='%s' and consumer='%s' limit 1;",tablename,dataconsumer);//我的表格叫persons

            ResultSet resultSet2 = statement.executeQuery(sql3);

            if (resultSet2.first()) {
                //            执行插入语句
                String sql2 = String.format("INSERT INTO batchorder (name,consumer) VALUES ('%s00','%s');",tablename,dataconsumer);
                statement.executeUpdate(sql2);

                ResultSet resultSet3 = statement.executeQuery(sql3);
                resultSet3.next();

                ToLinux2 toLinux2 = new ToLinux2();
                System.out.println(resultSet3.getString(3));
                toLinux2.runPushData2(tablename,datadate,resultSet3.getString(3),dataconsumer,fullfields);
                logger1.info("启动"+tablename+datadate+resultSet3.getString(3));

                String sql5 = String.format("delete from batchorder where name ='%s' and num='%s' and consumer='%s';",resultSet3.getString(1),resultSet3.getString(3),resultSet3.getString(4));
                statement.executeUpdate(sql5);
                resultSet.close();
                resultSet2.close();
                resultSet3.close();
                con.close();
                return "hi";
            } else {
//                String sql2 = String.format("INSERT INTO batchorder (name,consumer) VALUES ('%s00','%s');",tablename,dataconsumer);
//                statement.executeUpdate(sql2);
//
//                resultSet.close();
//                resultSet2.close();
//                con.close();
                return "hitoo";
            }
        }
    }
    public String selectOrder3(String tablename,String datadate) throws Exception{
        GetBatchNum getBatchNum = new GetBatchNum();
        Connect connect = new Connect();
        Connection con = connect.getStaticConnection();
        Statement statement = con.createStatement();

//            执行查询语句
        String sql = String.format("select * from batchorder where name='%s00';",tablename);//我的表格叫persons
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.first()) {
            resultSet.close();
            con.close();
            return "notnow";
//            System.exit(1);
        } else {
            String sql3 = String.format("select * from batchorder where name='%s33';",tablename);//我的表格叫persons
            ResultSet resultSet3 = statement.executeQuery(sql3);
            if (resultSet3.first()) {
                String sql4 = String.format("delete from batchorder where name ='%s33'", tablename);
                statement.executeUpdate(sql4);
                resultSet3.close();
                con.close();
                return "wrong";
            }else {
                return "yesnow";
            }

        }


    }
    public void selectOrder4(String tablename,String dataconsumer) throws Exception {
        Connect connect = new Connect();
        Connection con = connect.getStaticConnection();
        Statement statement = con.createStatement();
        String sql5 = String.format("delete from batchorder where name ='%s33' and consumer='%s';",tablename,dataconsumer);
        statement.executeUpdate(sql5);
        con.close();


    }
    public static void main(String[] args) throws Exception{
        BatchOrder batchOrder = new BatchOrder();
        batchOrder.selectOrder("yyb_index_account_vcc_daily","201800707","datacomsumer1");

    }
}
