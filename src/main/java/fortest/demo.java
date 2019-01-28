package fortest;

import org.apache.rocketmq.remoting.netty.NettyServerConfig;
import rock.Connect;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class demo {
    public static void main(String[] args) throws Exception{
        Connect connect = new Connect();
        Connection staticConnection = connect.getStaticConnection();
        staticConnection.setAutoCommit(false);
        try {


            Statement statement = staticConnection.prepareStatement("a");
            statement.executeUpdate("INSERT INTO agetest (age) VALUE (19)");

            statement.executeUpdate("INSERT INTO agetest (age) VALUE ('g')");
//        staticConnection.rollback();
            staticConnection.commit();

        }catch (Exception e){
            staticConnection.rollback();
            Statement statement = staticConnection.createStatement();
            statement.executeUpdate("INSERT INTO agetest (age) VALUE (20)");
            staticConnection.commit();
            System.out.println("hi");
        }
    }
}
