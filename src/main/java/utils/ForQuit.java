package utils;

import rock.Connect;

import java.sql.Connection;
import java.sql.Statement;

public class ForQuit {


//    public ForQuit(final String tablename, final String datadate,final String dataconsumer) {
//        doShutDownWork(tablename,datadate,dataconsumer);
//
//    }
//    public void ForQuit2(final String tablename, final String datadate,final String dataconsumer) {
//        doShutDownWork2(tablename,datadate,dataconsumer);
//
//    }
    /***************************************************************************
     * This is the right work that will do before the system shutdown
     * 这里为了演示，为应用程序的退出增加了一个事件处理，
     * 当应用程序退出时候，将程序退出的日期写入 d:\t.log文件
     **************************************************************************/


    public void doShutDownWork(final String tablename, final String datadate,final String dataconsumer) {


        Runtime.getRuntime().addShutdownHook(

                new Thread() {

            public void run() {
                BatchOrder batchOrder = new BatchOrder();
                try {
//                    String s = batchOrder.selectOrder2(tablename, datadate, dataconsumer);
                    System.out.println("sssssssssssssssssss");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
                Connect connect = new Connect();
                try {
                    Connection con = connect.getStaticConnection();
                    Statement statement = con.createStatement();

                    String sql5 = String.format("delete from batchorder where name ='%s00' and consumer='%s'", tablename, dataconsumer);
                    statement.executeUpdate(sql5);
                    statement.close();
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }
    public void doShutDownWork2(final String tablename, final String datadate,final String dataconsumer) {
        Connect connect = new Connect();
        try {
            Connection con = connect.getStaticConnection();
            Statement statement = con.createStatement();

            String sql5 = String.format("delete from batchorder where name ='%s00' and consumer='%s'", tablename, dataconsumer);
            statement.executeUpdate(sql5);
            statement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {


        System.out.println("OK");
//        long s = System.currentTimeMillis();
//        for (int i = 0; i < 1000000000; i++) {
//            //在这里增添您需要处理代码
//
//        }
//        long se = System.currentTimeMillis();
//        System.out.println(se - s);
    }

}