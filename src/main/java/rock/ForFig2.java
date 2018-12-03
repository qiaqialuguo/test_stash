package rock;

import java.io.*;
import java.util.Properties;

public class ForFig2 {
    public static void main(String [] args) throws IOException {
//        readProperties();
        writeProperties();
    }

    /**
     * 读取properties属性文件
     */
    public Properties readProperties()throws IOException{
        Properties prop=new Properties();
//        InputStream inputFile = ForFig.class.getClassLoader().getResourceAsStream("fig.properties");
        InputStream inputFile=new FileInputStream(new File(System.getProperty("user.dir")+File.separator+"consumer.properties"));
        prop.load(inputFile);
        inputFile.close();
        return prop;
    }

    /**
     * 生成properties属性文件
     */
    public static void writeProperties()  {

        Properties prop=new Properties();
        try{
            //FileOutputStream oFile=new FileOutputStream("fig.properties",true);
            FileOutputStream oFile=new FileOutputStream(new File("src\\main\\resources\\fig.properties"),true);
//            prop.setProperty("driver-name","oracle.jdbc.driver.OracleDriver");
//            prop.setProperty("url","jdbc：oracle:thin:@localhost:1521:ORCL");
//            prop.setProperty("user-name","drp1");
//            prop.setProperty("password","drp1");
//            prop.setProperty("everynum","3");
//            prop.setProperty("stopword","sttoop1");
//            prop.setProperty("hdfshost","hdfs://172.21.16.45:8020");
//            prop.setProperty("fileurl","/apps/hive/warehouse/ods.db/jrtt_creative_read/tx_date=20180531/000002_0");
//            prop.setProperty("datatopic","TopicTest2");
            prop.setProperty("datadate","20180604");
            prop.setProperty("bigbatch","000001");
            prop.store(oFile,"datatopic");

            oFile.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
