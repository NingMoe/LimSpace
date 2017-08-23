package com.tyiti.easycommerce.entity;

import java.util.Date;

public class PromotionRaffle {
	 
	private Integer userId;
	private Integer prizeId;
	private Integer promotionId;
	private Integer code;
	private Integer raffleCount;
	private Integer prizeCount; //中奖次数 后加
	private Date createTime;
	private Integer times ;
	private Integer id;//抽奖记录id
	private String realName;//收货人姓名
	private String mobile ;//收货人电话
	private String name ;//奖品名称
	private boolean isReceive;//是否已经接收
	private String address; //收货人地址
	private String imageUrl; //图片地址
	private Date expireTime;//过期时间
	private boolean isExpire ;//是否过期
	
	private Date updateTime ; 
	
	public Integer getPrizeCount() {
		return prizeCount;
	}
	public void setPrizeCount(Integer prizeCount) {
		this.prizeCount = prizeCount;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getPrizeId() {
		return prizeId;
	}
	public void setPrizeId(Integer prizeId) {
		this.prizeId = prizeId;
	}
	public Integer getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Integer getRaffleCount() {
		return raffleCount;
	}
	public void setRaffleCount(Integer raffleCount) {
		this.raffleCount = raffleCount;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	 
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	 
	 
	public boolean isReceive() {
		return isReceive;
	}
	public void setReceive(boolean isReceive) {
		this.isReceive = isReceive;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	public boolean isExpire() {
		return isExpire;
	}
	public void setExpire(boolean isExpire) {
		this.isExpire = isExpire;
	}
	 
}