package com.tyiti.easycommerce.entity;

import java.util.Date;

public class Coupon {
    private Integer id;

    private String name;

    private Integer count;

    private String summary;

    private Integer type;

    private Integer scope;

    private Integer refId;

    private String prefix;

    private Date receiveStartTime;

    private Date receiveEndTime;

    private Integer timeType;

    private Date startTime;

    private Date expireTime;

    private Integer expireInDays;

    private Double threshold;

    private Double discount;

    private String receiveSms;

    private String dueSms;

    private Integer isDue;

    private Integer receivedNum;

    private Integer usedNum;

    private Integer invalid;

    private Date createTime;

    private Date updateTime;

    private Integer stop ; 
	public Integer getStop() {
		return stop;
	}

	public void setStop(Integer stop) {
		this.stop = stop;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getScope() {
		return scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}

	public Integer getRefId() {
		return refId;
	}

	public void setRefId(Integer refId) {
		this.refId = refId;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Date getReceiveStartTime() {
		return receiveStartTime;
	}

	public void setReceiveStartTime(Date receiveStartTime) {
		this.receiveStartTime = receiveStartTime;
	}

	public Date getReceiveEndTime() {
		return receiveEndTime;
	}

	public void setReceiveEndTime(Date receiveEndTime) {
		this.receiveEndTime = receiveEndTime;
	}

	public Integer getTimeType() {
		return timeType;
	}

	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Integer getExpireInDays() {
		return expireInDays;
	}

	public void setExpireInDays(Integer expireInDays) {
		this.expireInDays = expireInDays;
	}

	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getReceiveSms() {
		return receiveSms;
	}

	public void setReceiveSms(String receiveSms) {
		this.receiveSms = receiveSms;
	}

	public String getDueSms() {
		return dueSms;
	}

	public void setDueSms(String dueSms) {
		this.dueSms = dueSms;
	}

	public Integer getIsDue() {
		return isDue;
	}

	public void setIsDue(Integer isDue) {
		this.isDue = isDue;
	}

	public Integer getReceivedNum() {
		return receivedNum;
	}

	public void setReceivedNum(Integer receivedNum) {
		this.receivedNum = receivedNum;
	}

	public Integer getUsedNum() {
		return usedNum;
	}

	public void setUsedNum(Integer usedNum) {
		this.usedNum = usedNum;
	}

	public Integer getInvalid() {
		return invalid;
	}

	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
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

    
}