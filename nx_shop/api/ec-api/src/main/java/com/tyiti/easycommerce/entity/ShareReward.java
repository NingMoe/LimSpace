package com.tyiti.easycommerce.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ShareReward {
	// 主键
	private Integer id;
	// 用户id(邀请者或注册者)
	private Integer userId;
	private Integer shareId;
	// 't_share_rule.id'
	private Integer shareRuleId;
	// 获取的优惠券记录id(t_coupon_record.id)'
	private Integer couponRecordId;
	//创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getShareId() {
		return shareId;
	}
	public void setShareId(Integer shareId) {
		this.shareId = shareId;
	}
	public Integer getShareRuleId() {
		return shareRuleId;
	}
	public void setShareRuleId(Integer shareRuleId) {
		this.shareRuleId = shareRuleId;
	}
	public Integer getCouponRecordId() {
		return couponRecordId;
	}
	public void setCouponRecordId(Integer couponRecordId) {
		this.couponRecordId = couponRecordId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}