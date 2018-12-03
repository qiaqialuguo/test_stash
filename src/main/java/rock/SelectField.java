package rock;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.util.*;

public class SelectField {

    public List<Integer> indexs(String changedfield,List<String> selectfield) throws Exception{

        //全部字段
        String[] split = changedfield.split(",");
        List<String> strings = new ArrayList<String>();
        for(String splited:split){
            strings.add(splited);
        }

        //指定字段
        Integer i=0;
        List<Integer> indexs = new ArrayList<Integer>();
        for(String superscript:selectfield){
            indexs.add(strings.indexOf(superscript));
            i=strings.indexOf(superscript);
        }

        return indexs;

    }

    public List<String> selectlist(String allfield,String target_field) throws Exception{

        JSONObject js= JSONObject.parseObject(target_field);

        String[] split = allfield.split(",");
        List<String> strings = new ArrayList<String>();
        for(String splited:split){
            strings.add(splited);
        }
        List<String> target_fileds_list = new ArrayList<String>();
        for(int i=0;i<strings.size();i++){
            if(!js.get(strings.get(i)).equals("")){
                Object obj = js.get(strings.get(i));
                target_fileds_list.add(obj.toString());
            }
        }
        System.out.println("__________________________________-"+target_fileds_list.toString());
        List<String> selectlist = new ArrayList<String>();
        for(String index:target_fileds_list){
            selectlist.add(index);
        }
        return selectlist;

    }
    public List<String> alllist(String allfield) throws Exception{

        //指定字段
        String[] allsplit = allfield.split(",");
        List<String> alllist = new ArrayList<String>();
        for(String index:allsplit){
            alllist.add(index);
        }
        return alllist;

    }
    public String fieldChange(String allfield,String target_field){
        //全部字段
        String[] split = allfield.split(",");
        List<String> strings = new ArrayList<String>();
        for(String splited:split){
            strings.add(splited);
        }
        JSONObject js= JSONObject.parseObject(target_field);
        List<String> changed_fileds_list = new ArrayList<String>();
        for(int i=0;i<strings.size();i++){
            System.out.println(i);
            if(!js.get(strings.get(i)).equals("")){
                Object obj = js.get(strings.get(i));
                changed_fileds_list.add(obj.toString());
            }
            else {
                changed_fileds_list.add(strings.get(i));
            }
        }
        String join = StringUtils.join(changed_fileds_list, ",");
        return join;
    }
//    public static void main(String[] args) {
//        SelectField selectField = new SelectField();
//        List<Integer> indexs = selectField.indexs();
//        for(Integer index:indexs){
////            System.out.println(index);
//        }
//        List<String> selectlist = selectField.selectlist();
//        for(String index:selectlist){
////            System.out.println(index);
//        }
//
//        String join = StringUtils.join(selectlist, ",");
//        String field =String.format("(%s)",join);
//        String prefix = String.format("INSERT INTO %s %s VALUES ","person",field);
//        String[] sourceStrArray = "stop\t8".split("\t", 2);
//        ArrayList<String> objects = new ArrayList<String>();
//        for(Integer index:indexs){
////            objects.add(sourceStrArray[index]);
//        }
//        String join1 = StringUtils.join(objects, "','");
//        String field2 =String.format("('%s'),",join1);
//        String join2 = StringUtils.join(selectlist, "\t");
//        System.out.println("stop\t"+join2);
//    }
}