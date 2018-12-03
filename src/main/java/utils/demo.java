package utils;
import dao.Consumser_dao;
import dao.Producer_Dao;
import rock.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class demo {
    public static void main(String[] args) throws Exception{
        Connect connect = new Connect();
        final Connection conn = connect.getConnection("gdt_adcreative_template_enable","uat_ksa");
        PreparedStatement ps = (PreparedStatement) conn.prepareStatement("a");
        String sql ="ww";
        ps.addBatch(sql);
        ps.executeBatch();
        // 提交事务
        conn.commit();
           }
}
