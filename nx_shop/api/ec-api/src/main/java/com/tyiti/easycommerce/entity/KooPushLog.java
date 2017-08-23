package com.tyiti.easycommerce.entity;

import java.util.Date;

public class KooPushLog {
	
    private Integer id;

    private Integer orderId;

    private Integer userId;

    private Date pushTime;

    private Boolean isSuccess;
    
    private Date createTime;

    private Date updateTime;

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

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPushStr() {
        return pushStr;
    }

    public void setPushStr(String pushStr) {
        this.pushStr = pushStr == null ? null : pushStr.trim();
    }
}