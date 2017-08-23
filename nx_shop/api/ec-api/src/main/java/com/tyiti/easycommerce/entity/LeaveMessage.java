package com.tyiti.easycommerce.entity;

import java.util.Date;

/**
 * @author wangqi
 * @date 2016-4-22 上午9:26:17
 * @description 用户留言
 */
public class LeaveMessage {
    private Integer id;

    private Integer custId;

    private Integer status;

    private Integer managerId;

    private Date leaveTime;

    private Date replyTime;

    private String leaveIp;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public String getLeaveIp() {
        return leaveIp;
    }

    public void setLeaveIp(String leaveIp) {
        this.leaveIp = leaveIp == null ? null : leaveIp.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}