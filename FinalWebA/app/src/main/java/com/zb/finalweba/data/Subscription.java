package com.zb.finalweba.data;

import java.util.Date;

public class Subscription {
    private Integer id;
    private Integer status;
    private Date datetime;
    private SubsDetail subsdetail;
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public SubsDetail getSubsdetail() {
        return subsdetail;
    }

    public void setSubsdetail(SubsDetail subsdetail) {
        this.subsdetail = subsdetail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
