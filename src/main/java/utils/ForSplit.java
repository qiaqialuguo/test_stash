package utils;

import dao.Producer_Dao;

public class ForSplit {
    public  String forSplit(String tablename) {
        Producer_Dao producer_dao = new Producer_Dao();
        producer_dao.quary_producers(tablename);
        String separative_sign = producer_dao.separative_sign;
        if("\\t".equals(separative_sign)){
            separative_sign=separative_sign.replace("\\t","\t");
        }else if("\\001".equals(separative_sign)){
            separative_sign=separative_sign.replace("\\001","\001");
        }


        return separative_sign;
    }

    public static void main(String[] args) {
        ForSplit forSplit = new ForSplit();
        String s = forSplit.forSplit("gdt_index_account_vcc_daily");
        System.out.println(s);
    }

}