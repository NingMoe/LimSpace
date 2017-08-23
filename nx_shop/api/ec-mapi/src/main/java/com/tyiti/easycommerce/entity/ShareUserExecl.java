package com.tyiti.easycommerce.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ShareUserExecl {
	private Integer userId;
	private String mobile;
	private Integer sUserId;
	private String sMobile;
	private String couponName;
	private String isUsed;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getsUserId() {
		return sUserId;
	}
	public void setsUserId(Integer sUserId) {
		this.sUserId = sUserId;
	}
	public String getsMobile() {
		return sMobile;
	}
	public void setsMobile(String sMobile) {
		this.sMobile = sMobile;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public String getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	// 分享时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
}