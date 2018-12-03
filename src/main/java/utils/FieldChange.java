package utils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import rock.SelectField;

import java.util.ArrayList;
import java.util.List;

public class FieldChange {
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

    public static void main(String[] args) throws Exception{
        SelectField selectField = new SelectField();
        String s = selectField.fieldChange("account_id,tx_dt,image_id,signature,type,height,width,file_size,preview_url", "{\"type\": \"typeiiiiiiiiiiiiiii\", \"tx_dt\": \"\", \"width\":\"\", \"height\": \"height\", \"image_id\": \"image_id\", \"file_size\": \"file_size\", \"signature\": \"signature\", \"account_id\": \"account_ids\", \"preview_url\": \"preview_url\"}");
        List<String> selectlist = selectField.selectlist("account_id,tx_dt,image_id,signature,type,height,width,file_size,preview_url", "{\"type\": \"typeiiiiiiiiiiiiiii\", \"tx_dt\": \"\", \"width\":\"\", \"height\": \"height\", \"image_id\": \"image_id\", \"file_size\": \"file_size\", \"signature\": \"signature\", \"account_id\": \"account_ids\", \"preview_url\": \"preview_url\"}");
        List<Integer> indexs = selectField.indexs(s, selectlist);
        System.out.println(indexs);
    }


}
