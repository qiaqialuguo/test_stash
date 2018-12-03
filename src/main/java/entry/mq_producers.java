package entry;

import java.util.Date;

/**
 * Created by 123 on 2018/8/22.
 * mq_producers 生产实体类
 *
 */
public class mq_producers {
    private  String mq_job;
    private String producer;
    private String mq_topic;
    private int everynum;
    private String separative_sign;
    private String datafields;
    private String last_jobstatus;
    private String  last_txdate;
    private Date last_starttime;
    private Date last_endtime;
    private Integer ennable;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMq_job() {
        return mq_job;
    }

    public void setMq_job(String mq_job) {
        this.mq_job = mq_job;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getMq_topic() {
        return mq_topic;
    }

    public void setMq_topic(String mq_topic) {
        this.mq_topic = mq_topic;
    }

    public int getEverynum() {
        return everynum;
    }

    public void setEverynum(int everynum) {
        this.everynum = everynum;
    }

    public String getSeparative_sign() {
        return separative_sign;
    }

    public void setSeparative_sign(String separative_sign) {
        this.separative_sign = separative_sign;
    }

    public String getDatafields() {
        return datafields;
    }

    public void setDatafields(String datafields) {
        this.datafields = datafields;
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

    @Override
    public String toString() {
        return "mq_producers{" +
                "mq_job='" + mq_job + '\'' +
                ", producer='" + producer + '\'' +
                ", mq_topic='" + mq_topic + '\'' +
                ", everynum=" + everynum +
                ", separative_sign='" + separative_sign + '\'' +
                ", datafields='" + datafields + '\'' +
                ", last_jobstatus='" + last_jobstatus + '\'' +
                ", last_txdate='" + last_txdate + '\'' +
                ", last_starttime=" + last_starttime +
                ", last_endtime=" + last_endtime +
                ", ennable=" + ennable +
                ", path='" + path + '\'' +
                '}';
    }
}
