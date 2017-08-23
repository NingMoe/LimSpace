package com.tyiti.easycommerce.entity;

import java.util.Date;

public class PushLog {
    private Integer id;

    private Integer orderId;

    private Integer userId;

    private Date pushTime;

    private Boolean flag;

    private Date creatTime;

    private Date updatetime;

    private String pushStr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getPushStr() {
        return pushStr;
    }

    public void setPushStr(String pushStr) {
        this.pushStr = pushStr == null ? null : pushStr.trim();
    }
}