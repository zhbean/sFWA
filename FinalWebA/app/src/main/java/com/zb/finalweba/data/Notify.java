package com.zb.finalweba.data;

import android.icu.text.SimpleDateFormat;

import java.util.Date;

public class Notify {
    private Integer id;
    private String address;
    private String content;
    private Integer status;
    private Date ntime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNtime() {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(ntime);
    }

    public void setNtime(Date ntime) {
        this.ntime = ntime;
    }
    //    private Subscription subscription;
}
