package com.tyiti.easycommerce.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ShareRule {

	// 主键
	private Integer id;
	// 优惠券Id
	private Integer couponId;
	// 类型 1、分享者获得 2、被分享者获得 3、被分享者获得额外
	private Integer type;
	// 状态 1、有效  0、无效
	private Integer status;
	// 达到此注册用户数时，可额外获得优惠券(type=3)
	private Integer extraThreshold;
	

	public Integer getExtraThreshold() {
		return extraThreshold;
	}

	public void setExtraThreshold(Integer extraThreshold) {
		this.extraThreshold = extraThreshold;
	}

	// 级别结束 只有当 type=3时有效 获得该优惠券注册成功用户数的最大条件
	// 添加时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	// 更新时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	// 查询添加开始时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date stratTime;
	// 查询添加结束时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTime;
	//查询更新开始时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date upStratTime;
	
	public Date getStratTime() {
		return stratTime;
	}

	public void setStratTime(Date stratTime) {
		this.stratTime = stratTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getUpStratTime() {
		return upStratTime;
	}

	public void setUpStratTime(Date upStratTime) {
		this.upStratTime = upStratTime;
	}

	public Date getUpEndTime() {
		return upEndTime;
	}

	public void setUpEndTime(Date upEndTime) {
		this.upEndTime = upEndTime;
	}

	//查询更新结束时间
	private Date upEndTime;
	
	// 注册数量
	private Integer regNum;

	public Integer getRegNum() {
		return regNum;
	}

	public void setRegNum(Integer regNum) {
		this.regNum = regNum;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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