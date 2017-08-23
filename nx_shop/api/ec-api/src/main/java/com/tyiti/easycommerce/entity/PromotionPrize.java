package com.tyiti.easycommerce.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PromotionPrize {
	private Integer id;
	private Integer promotionId;
	private String name;
	private String description;
	private Integer inventory;
	private Integer awardedAmount;
	private Short virtualType;
	private BigDecimal virtualValue;
	private Integer virtualExt1;
	private Integer virtualExt2;
	private Integer rate;
	private Date expireTime;
	private Integer expireSeconds;
	private Date createTime;
	private Date modifyTime;
	private String imageUrl;
	
	private Integer raffleId ; 
	public Integer getRaffleId() {
		return raffleId;
	}
	public void setRaffleId(Integer raffleId) {
		this.raffleId = raffleId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}
	public Integer getInventory() {
		return inventory;
	}
	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}
	public Integer getAwardedAmount() {
		return awardedAmount;
	}
	public void setAwardedAmount(Integer awardedAmount) {
		this.awardedAmount = awardedAmount;
	}
	public Short getVirtualType() {
		return virtualType;
	}
	public void setVirtualType(Short virtualType) {
		this.virtualType = virtualType;
	}
	public BigDecimal getVirtualValue() {
		return virtualValue;
	}
	public void setVirtualValue(BigDecimal virtualValue) {
		this.virtualValue = virtualValue;
	}
	public Integer getVirtualExt1() {
		return virtualExt1;
	}
	public void setVirtualExt1(Integer virtualExt1) {
		this.virtualExt1 = virtualExt1;
	}
	public Integer getVirtualExt2() {
		return virtualExt2;
	}
	public void setVirtualExt2(Integer virtualExt2) {
		this.virtualExt2 = virtualExt2;
	}
	public Integer getRate() {
		return rate;
	}
	public void setRate(Integer rate) {
		this.rate = rate;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	public Integer getExpireSeconds() {
		return expireSeconds;
	}
	public void setExpireSeconds(Integer expireSeconds) {
		this.expireSeconds = expireSeconds;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl == null ? null : imageUrl.trim();
	}
 
}
