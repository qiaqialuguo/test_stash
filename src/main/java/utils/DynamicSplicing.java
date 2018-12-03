package utils;

import rock.ForFig;

import java.util.ArrayList;
import java.util.Properties;

public class DynamicSplicing {
    public String splicedSql(String datadate,String deletesql) throws Exception {
        String delte_sql = deletesql.replace("${TX_DT}", datadate);
        return delte_sql;
    }

    public String splicedMysqlTable(String datadate,String Mysqltable) throws Exception {
        String target_table = Mysqltable;
        if (Mysqltable.contains("${TX_DT}")) {
            target_table = Mysqltable.replace("${TX_DT}", datadate);
        }
        return target_table;
    }

    public String selectJDBC(String tablename,String datadate,String Mysqltable) throws Exception{
        ForFig forFig = new ForFig();
        Properties properties = forFig.readProperties();
        String deletesql = properties.getProperty(tablename + "jdbcsql");
        String[] splits = deletesql.split("\t");
        ArrayList<String> strings = new ArrayList<String>();
        for (String split : splits) {
            if ("datadate".equals(split)) {
                DynamicSplicing dynamicSplicing = new DynamicSplicing();
                String ff1 = dynamicSplicing.getDataDate(datadate);
//                Object ff1 = dynamicSplicing.getClass().getMethod("getDataDate", datadate.getClass()).invoke(dynamicSplicing, datadate);
                String datadate1 = split.replace("datadate", ff1);
                strings.add(datadate1);
            } else if ("jdbcMysql".equals(split)) {
                DynamicSplicing dynamicSplicing = new DynamicSplicing();
                String mysqltable = dynamicSplicing.splicedMysqlTable(datadate,Mysqltable);
                String datadate1 = split.replace("jdbcMysql",mysqltable);
                strings.add(datadate1);
            } else {
                strings.add(split);
            }
        }
        String fullsql="";
        for(int i=0;i<strings.size();i++){
            fullsql += strings.get(i);
        }

        return fullsql;
    }
    public String getDataDate(String datadate) {
        return datadate;
    }
    public static void main(String[] args) throws Exception{
        DynamicSplicing dynamicSplicing = new DynamicSplicing();
        Object jrttadcreativeinfo = dynamicSplicing.splicedMysqlTable("20180909","");
        System.out.println(jrttadcreativeinfo);
    }

}
