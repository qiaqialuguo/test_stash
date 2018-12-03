package utils;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import rock.ForFig2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RecordToHttp {

    public void recordToHttp(String dbName,String tablename,String mysqlName,String datadate,String bigbatch,String starttime,String endtime,String status,String row_cnt) throws Exception{
        ForFig2 forFig = new ForFig2();
        Properties properties = forFig.readProperties();
        String httpRecordUrl = properties.getProperty("httpRecordUrl");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String entityStr = null;
        CloseableHttpResponse response = null;

        try {

            // 创建POST请求对象
            HttpPost httpPost = new HttpPost(httpRecordUrl);

        /*
         * 添加请求参数
         */
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dbName",dbName);
            jsonObject.put("tableName",mysqlName);
            jsonObject.put("tableDate",datadate);
            jsonObject.put("banchId",bigbatch);
            jsonObject.put("startTime",starttime);
            jsonObject.put("endTime",endtime);
            jsonObject.put("status",status);
            jsonObject.put("rowCnt",row_cnt);
            // 创建请求参数
            List<NameValuePair> list = new LinkedList<NameValuePair>();
            BasicNameValuePair param1 = new BasicNameValuePair("topic", "dmp");
            BasicNameValuePair param2 = new BasicNameValuePair("event", tablename);
            BasicNameValuePair param3 = new BasicNameValuePair("params", jsonObject.toString());
            list.add(param1);
            list.add(param2);
            list.add(param3);
            System.out.println(jsonObject.toString());
            // 使用URL实体转换工具
            UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
            httpPost.setEntity(entityParam);

        /*
         * 添加请求头信息
         */
            // 浏览器表示
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
            // 传输的类型
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.addHeader("Token","kedadmp2018");
            httpPost.addHeader("RouteKey","keda70d850");

            // 执行请求
            response = httpClient.execute(httpPost);
            // 获得响应的实体对象
//            HttpEntity entity = response.getEntity();
            // 使用Apache提供的工具类进行转换成字符串
//            entityStr = EntityUtils.toString(entity, "UTF-8");

            // System.out.println(Arrays.toString(response.getAllHeaders()));

        } catch (ClientProtocolException e) {
            System.err.println("Http协议出现问题");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("解析错误");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO异常");
            e.printStackTrace();
        } finally {
            // 释放连接
            if (null != response) {
                try {
                    response.close();
                    httpClient.close();
                } catch (IOException e) {
                    System.err.println("释放连接出错");
                    e.printStackTrace();
                }
            }
        }

        // 打印响应内容
//        System.out.println(response);
    }


    public static void main(String[] args) {
        Long end = new Date().getTime();
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        String sd2 = sdf2.format(new Date(Long.parseLong(String.valueOf(end))));
        RecordToHttp recordToHttp = new RecordToHttp();
        try {

            recordToHttp.recordToHttp("dataconsumer3","1","1","1","1","1",sd2,"1","1");
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("qqqq");
        }
    }

}
