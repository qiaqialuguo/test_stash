package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ToLinux2 {
    public void runReadData(String tablename,String datadate,String bigbatch) throws Exception{

        try {
            String[] cmd = new String[] { "java", "-cp","appGdt-1.0-SNAPSHOT.jar","rock.ReadData",tablename,datadate,bigbatch,"> /home/zhangxianyi/null 2>&1 &"};
//            String[] cmd = new String[] { "/bin/sh", "/root/test.sh" };
            Process ps = Runtime.getRuntime().exec(cmd);

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            System.out.println(result);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void runPushData2(String tablename,String datadate,String bigbatch,String dataconsumer,String fullfield) throws Exception{

        try {
//            String[] cmd = new String[] { "java", "-cp","appGdt-1.0-SNAPSHOT.jar","rock.PushData2","> /home/zhangxianyi/null 2>&1 &"};
            String[] cmd = new String[] { "java", "-cp","appGdt-1.0-SNAPSHOT.jar","rock.PushData2",tablename,datadate,bigbatch,dataconsumer,fullfield};
// String[] cmd = new String[] { "/bin/sh", "/root/test.sh" };
            Process ps = Runtime.getRuntime().exec(cmd);

//            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
//            StringBuffer sb = new StringBuffer();
//            String line;
//            while ((line = br.readLine()) != null) {
//                sb.append(line).append("\n");
//            }
//            String result = sb.toString();
//            System.out.println(result);



        } catch (Exception e) {

            e.printStackTrace();
        }

    }
    public void runControl(String tablename,String datadate) throws Exception{

        try {
//            String[] cmd = new String[] { "java", "-cp","appGdt-1.0-SNAPSHOT.jar","rock.PushData2","> /home/zhangxianyi/null 2>&1 &"};
            String[] cmd = new String[] { "java", "-cp","appGdt-1.0-SNAPSHOT.jar","rock.ControlProduct",tablename,datadate};
// String[] cmd = new String[] { "/bin/sh", "/root/test.sh" };
            Process ps = Runtime.getRuntime().exec(cmd);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}