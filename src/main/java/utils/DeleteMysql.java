package utils;

import rock.Connect;
import rock.ForFig;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DeleteMysql {
    public  void  deleteRepetition(String tablename,String datadate,String dataconsumer,String deletesql) throws Exception {
        DynamicSplicing dynamicSplicing = new DynamicSplicing();
        final String mysqltable = dynamicSplicing.splicedMysqlTable(datadate,deletesql);

        Connect connect = new Connect();
        Connection con = connect.getConnection(tablename,dataconsumer);
        Statement statement = con.createStatement();

//            执行查询语句
        String sql = dynamicSplicing.splicedSql(datadate,deletesql);
//        String sql = "delete from "+mysqltable+" WHERE "+deletefield+" = "+String.format("'%s'",datadate);//我的表格叫persons
        System.out.println(sql);
        String[] splits = sql.split(";");
        for(String split:splits){
        statement.executeUpdate(split);
        }

        con.close();

    }

    public static void main(String[] args) throws Exception{

        DeleteMysql deleteMysql = new DeleteMysql();
        deleteMysql.deleteRepetition("gdt_advertisers_report_hourly","20180604","pro_ksa","");


    }






}
