package com.tyiti.easycommerce.entity;

import java.util.Date;

import com.tyiti.easycommerce.base.BaseModel;

public class OrderCancellation extends BaseModel {
	private Integer id;
	private Integer orderId;
	private String no;
	private Integer type;
	private String reason;
	private String pics;
	private Date createTime;
	private Date updateTime;
	private Integer status;

	private Integer reasonType ; 
	
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
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getPics() {
		return pics;
	}
	public void setPics(String pics) {
		this.pics = pics;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getReasonType() {
		return reasonType;
	}
	public void setReasonType(Integer reasonType) {
		this.reasonType = reasonType;
	}
	

}