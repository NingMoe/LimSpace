package com.tyiti.easycommerce.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author shenzhiqiang
 * @ClassName: Activity 
 * @Description: 
 * @date 2016年7月11日 下午4:18:00
 */
public class Activity {

	private Integer id;  //活动id
	
	private Integer activityType;  //活动类型 1：限购；2：满减；3：秒杀

	private String activityName;  //活动名称
	
	private Date createTime;  //活动创建时间
	
	private Date updateTime;  //活动修改时间
	
	private Integer activityMode;  //活动方式
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTime;  //活动开始时间
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTime;  //活动结束时间
	
	private String activityRemarks;  //活动备注
	
	private Integer activityStatus;  //活动状态，0：未开始，1：进行中，2：已结束
	
	private Integer invalid;  //活动是否已删除 0：未删除，1：已删除
	
	private String coupon;  //活动支持类型，1：优惠券，2：代金券

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getActivityType() {
		return activityType;
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
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

	public String getActivityRemarks() {
		return activityRemarks;
	}

	public void setActivityRemarks(String activityRemarks) {
		this.activityRemarks = activityRemarks;
	}

	public Integer getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(Integer activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Integer getInvalid() {
		return invalid;
	}

	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
	}

	public Integer getActivityMode() {
		return activityMode;
	}

	public void setActivityMode(Integer activityMode) {
		this.activityMode = activityMode;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
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
