package entry;



import java.util.Date;

/**
 * Created by 123 on 2018/8/22.
 *
 * 消费者组实体
 *
 */
public class mq_consumers {

    private String consumer_job;
    private String consumer;
    private String target_table;
    private String target_fileds;
    private String topic;
    private Integer everynum;
    private  String last_jobstatus;
    private String  last_txdate;
    private Date last_starttime;
    private Date last_endtime;
    private Integer ennable;
    private String delete_table;
    private String separative_sign;

    public String getConsumer_job() {
        return consumer_job;
    }

    public void setConsumer_job(String consumer_job) {
        this.consumer_job = consumer_job;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getTarget_table() {
        return target_table;
    }

    public void setTarget_table(String target_table) {
        this.target_table = target_table;
    }

    public String getTarget_fileds() {
        return target_fileds;
    }

    public void setTarget_fileds(String target_fileds) {
        this.target_fileds = target_fileds;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getEverynum() {
        return everynum;
    }

    public void setEverynum(Integer everynum) {
        this.everynum = everynum;
    }

    public String getLast_jobstatus() {
        return last_jobstatus;
    }

    public void setLast_jobstatus(String last_jobstatus) {
        this.last_jobstatus = last_jobstatus;
    }

    public String getLast_txdate() {
        return last_txdate;
    }

    public void setLast_txdate(String last_txdate) {
        this.last_txdate = last_txdate;
    }

    public Date getLast_starttime() {
        return last_starttime;
    }

    public void setLast_starttime(Date last_starttime) {
        this.last_starttime = last_starttime;
    }

    public Date getLast_endtime() {
        return last_endtime;
    }

    public void setLast_endtime(Date last_endtime) {
        this.last_endtime = last_endtime;
    }

    public Integer getEnnable() {
        return ennable;
    }

    public void setEnnable(Integer ennable) {
        this.ennable = ennable;
    }

    public String getDelete_table() {
        return delete_table;
    }

    public void setDelete_table(String delete_table) {
        this.delete_table = delete_table;
    }

    public String getSeparative_sign() {
        return separative_sign;
    }
    @Override
    public String toString() {
        return "mq_consumers{" +
                "consumer_job='" + consumer_job + '\'' +
                ", consumer='" + consumer + '\'' +
                ", target_table='" + target_table + '\'' +
                ", target_fileds=" + target_fileds +
                ", topic='" + topic + '\'' +
                ", everynum=" + everynum +
                ", last_jobstatus='" + last_jobstatus + '\'' +
                ", last_txdate='" + last_txdate + '\'' +
                ", last_starttime=" + last_starttime +
                ", last_endtime=" + last_endtime +
                ", ennable=" + ennable +
                ", delete_table='" + delete_table + '\'' +
                '}';
    }
}

