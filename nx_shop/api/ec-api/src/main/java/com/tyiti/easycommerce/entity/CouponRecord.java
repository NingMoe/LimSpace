package com.tyiti.easycommerce.entity;

import java.math.BigDecimal;
import java.util.Date;

public class CouponRecord implements Comparable<CouponRecord> {
	private Integer id;

	private Integer couponId;

	private Integer custId;

	private String mobile;

	private String couponCode;

	private Date receiveTime;

	private Integer isUsed;

	private Date useTime;

	private Date expireTime;

	private Integer isDue;

	private Integer invalid;

	private Date createTime;

	private Date updateTime;

	private Date startTime; // 开始时间

	private Integer available;// 可用不可用
	
	private Coupon coupon ; 
	
	private String scopeText ;
	
	private BigDecimal unavailableMoney;

	
	public BigDecimal getUnavailableMoney() {
		return unavailableMoney;
	}

	public void setUnavailableMoney(BigDecimal unavailableMoney) {
		this.unavailableMoney = unavailableMoney;
	}

	public String getScopeText() {
		return scopeText;
	}

	public void setScopeText(String scopeText) {
		this.scopeText = scopeText;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
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

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Integer getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Integer isUsed) {
		this.isUsed = isUsed;
	}

	public Date getUseTime() {
		return useTime;
	}

	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Integer getIsDue() {
		return isDue;
	}

	public void setIsDue(Integer isDue) {
		this.isDue = isDue;
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

	@Override
	public int compareTo(CouponRecord o) {
		// TODO Auto-generated method stub
		return this.getUnavailableMoney().compareTo(o.getUnavailableMoney());
	}

}