package rock;

import dao.Consumser_dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Connect{
    private Connection con;
    public Connection getConnection(String tablename,String dataconsumer) throws Exception{
        Consumser_dao consumser_dao = new Consumser_dao();
        consumser_dao.quary_mq_consumer(dataconsumer);
        String dscription = consumser_dao.description;
        System.out.println("dscription 描述是"+dscription);
        String[] split = dscription.split("-");
        String name = split[0].trim();
        String url = split[1].trim();
        String user = split[2].trim();
        String password = split[3].trim();
        System.out.println(name+url+user+password);
        try {
            Class.forName(name);
            con = DriverManager.getConnection(url, user, password);
            System.out.println("con OK");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
    public Connection getStaticConnection() throws Exception{
        ForFig forFig = new ForFig();
        Properties properties = forFig.readProperties();
        final String name = properties.getProperty("Name");
        final String url =properties.getProperty("Url");
        final String user =properties.getProperty("User");
        final String password =properties.getProperty("Password");
        try{
            Class.forName(name);
            con=DriverManager.getConnection(url,user,password);

            System.out.println("con OK");

        }catch(Exception e){
            e.printStackTrace();
        }
        return con;

    }
    public  void closeRes(Connection conn, Statement ps ) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}