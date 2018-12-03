package dao;

import rock.Connect;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/8/22.
 * 设计查询父类
 */
public class basedao {
    public    Connection conn = null;
    public  PreparedStatement ps = null;
    public   ResultSet rs = null;
    /**
     *  查询方法 适配多个实体 返回一个集合 利用反射机制获取表的数据
     */

    public   <T> List<T> executeQuery(String sql,Class<T> clz){
        List<T> lists=new ArrayList<T>();
        Connect conncent=new Connect();
        try {
            conn= conncent.getStaticConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int count=md.getColumnCount();
            while(rs.next()){
                try {
                    T t=clz.newInstance();
                    for(int i=0;i<count;i++){
                        String name=md.getColumnName(i+1);
                        Object value=rs.getObject(name);
                        try {

                                Field field =clz.getDeclaredField(name);
                            field.setAccessible(true);
                            field.set(t,value);

                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                    }
                    lists.add(t);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conncent.closeRes(conn,ps);
        }
        return lists;
    }




}
