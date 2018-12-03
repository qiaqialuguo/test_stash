package utils;

import rock.Connect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class GetBatchNum {
    public String getNum(String tablename) throws Exception {
        Connect connect = new Connect();
        Connection con = connect.getStaticConnection();
        Statement statement = con.createStatement();

//            执行查询语句
        String sql = String.format("select * from batchnum where name='%s';",tablename);//我的表格叫persons
        ResultSet resultSet = statement.executeQuery(sql);
        Integer num;
        if (!resultSet.first()) {
            //            执行插入语句
            num = 1;
            String sql2 = String.format("INSERT INTO batchnum (name,num) VALUES ('%s','%s');",tablename, num);
            statement.executeUpdate(sql2);
            resultSet.close();
            con.close();
            return num.toString();
        } else {
            String name;

            num = resultSet.getInt("num");
            num = num + 1;
            //                       执行更新语句
            String sql3 = String.format("UPDATE batchnum set num=" + num + " WHERE name='%s';", tablename);
            statement.executeUpdate(sql3);
            resultSet.close();
            con.close();
            return num.toString();
        }
        }
    public String getNum2(String tablename) throws Exception {
        Connect connect = new Connect();
        Connection con = connect.getStaticConnection();
        Statement statement = con.createStatement();

//            执行查询语句
        String sql = String.format("select * from batchnum where name='%s';",tablename+"00");//我的表格叫persons
        ResultSet resultSet = statement.executeQuery(sql);
        Integer num;
        if (!resultSet.first()) {
            //            执行插入语句
            num = 1;
            String sql2 = String.format("INSERT INTO batchnum (name,num) VALUES ('%s','%s');",tablename+"00", num);
            statement.executeUpdate(sql2);
            resultSet.close();
            con.close();
            return num.toString();
        } else {
            String name;


            num = resultSet.getInt("num");
            num = num + 1;
            //                       执行更新语句
            String sql3 = String.format("UPDATE batchnum set num=" + num + " WHERE name='%s';", tablename+"00");
            statement.executeUpdate(sql3);
            resultSet.close();
            con.close();
            return num.toString();
        }
    }

////            打印查询出来的东西
//        String name;
//        String num;
//        while (resultSet.next()) {
//            name = resultSet.getString("name");
//            num = resultSet.getString("num");
//            System.out.println(name+'\t'+num);
//        }
//
//
//        //            执行插入语句
//        String sql2="INSERT INTO `persons` (`name`, `num`) VALUES ('徐志摩', '45');";
//        statement.executeUpdate(sql2);
//
////                       执行更新语句
//        String sql3="UPDATE persons set num=66 WHERE `name`=\"徐志摩\"";
//        statement.executeUpdate(sql3);
//


////               执行删除语句
//        String sql4="delete from persons WHERE `name`=\"徐志摩\"";
//        statement.executeUpdate(sql4);
//
////            执行调用存储过程
//
//        String sql5="call add_student(3)";
//        statement.executeUpdate(sql5);


//            关闭连接

    public String realGetNum(String tablename) throws Exception {
        Connect connect = new Connect();
        Connection con = connect.getStaticConnection();
        Statement statement = con.createStatement();

//            执行查询语句
        String sql = String.format("select * from batchnum where name='%s';",tablename);//我的表格叫persons
        ResultSet resultSet = statement.executeQuery(sql);
        Integer num;
        resultSet.first();
        num = resultSet.getInt("num");
        resultSet.close();
        con.close();
        return num.toString();


    }
}