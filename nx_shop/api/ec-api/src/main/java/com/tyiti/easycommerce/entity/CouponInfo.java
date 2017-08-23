package com.tyiti.easycommerce.entity;

import java.util.Date;


/**
 * @author wangqi
 * @date 2016-4-6 上午10:09:22
 * @description TODO
 */
public class CouponInfo {
    // Fields
    private Integer id;
    private String name;
    private Integer type;//优惠券类别 1:SKU专用 2:SPU专用 3:活动专用 4:全场通用 5:品牌专用 6:类别专用
    private String refId;
    private String picUrls;
    private String summary;
    private Integer validDay;
    private Date createTime;
    private Date issueTime;
    private Date startTime;
    private Date endTime;
    private Byte invalid;
    private Integer rank;//是否为相对时间 0：相对时间（使用用户收到优惠券时间+有效天数） 1：绝对时间（开始时间-结束时间）
    private Byte isDelete;
    private Short couType;//优惠方式  0：满减；1：折扣 2：代金券
    private Double startMoney;
    private String couMoney;
    private Integer count;
    private Integer recordId;//优惠券记录id
    private String categoryName;//优惠券类别名称
    private Date receiveTime;//用户收到优惠券时间
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getPicUrls() {
		return picUrls;
	}
	public void setPicUrls(String picUrls) {
		this.picUrls = picUrls;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Integer getValidDay() {
		return validDay;
	}
	public void setValidDay(Integer validDay) {
		this.validDay = validDay;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getIssueTime() {
		return issueTime;
	}
	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Byte getInvalid() {
		return invalid;
	}
	public void setInvalid(Byte invalid) {
		this.invalid = invalid;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Byte getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Byte isDelete) {
		this.isDelete = isDelete;
	}
	public Short getCouType() {
		return couType;
	}
	public void setCouType(Short couType) {
		this.couType = couType;
	}
	public Double getStartMoney() {
		return startMoney;
	}
	public void setStartMoney(Double startMoney) {
		this.startMoney = startMoney;
	}
	public String getCouMoney() {
		return couMoney;
	}
	public void setCouMoney(String couMoney) {
		this.couMoney = couMoney;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getRecordId() {
		return recordId;
	}
	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
    
}
