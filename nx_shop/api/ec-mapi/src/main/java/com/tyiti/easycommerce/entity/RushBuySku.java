package com.tyiti.easycommerce.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: RushBuySku.java
 * @Package com.tyiti.easycommerce.entity
 * @Description: TODO(通用秒杀商品管理)
 * @author xulihui
 * @date 2016年4月13日 下午5:49:09
 * @version V1.0
 */
public class RushBuySku {

	private Integer id;
	private Integer rushBuyId;// 秒杀活动id
	private Integer skuId;// 活动商品id
	private String skuName;// 商品名称
	private BigDecimal price; // 秒杀活动价
	private BigDecimal originalPrice;
	private int availableTimes; // 单个用户可购买次数
	private int availableCount; // 单个用户可购买个数
	private int leftSku; // 剩余库存量
	private int soldSku; // 已售出数量
	private int returnSku; // 默认为0
	private Date createTime;// 创建时间

	public RushBuySku() {
		super();
	}

	public RushBuySku(Integer id, Integer rushBuyId, Integer skuId, String skuName, BigDecimal price,
			BigDecimal originalPrice, int availableTimes, int availableCount, int leftSku, int soldSku, int returnSku,
			Date createTime) {
		super();
		this.id = id;
		this.rushBuyId = rushBuyId;
		this.skuId = skuId;
		this.skuName = skuName;
		this.price = price;
		this.originalPrice = originalPrice;
		this.availableTimes = availableTimes;
		this.availableCount = availableCount;
		this.leftSku = leftSku;
		this.soldSku = soldSku;
		this.returnSku = returnSku;
		this.createTime = createTime;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public Integer getRushBuyId() {
		return rushBuyId;
	}


	public void setRushBuyId(Integer rushBuyId) {
		this.rushBuyId = rushBuyId;
	}


	public Integer getSkuId() {
		return skuId;
	}


	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}


	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
    
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public int getAvailableTimes() {
		return availableTimes;
	}

	public void setAvailableTimes(int availableTimes) {
		this.availableTimes = availableTimes;
	}

	public int getAvailableCount() {
		return availableCount;
	}

	public void setAvailableCount(int availableCount) {
		this.availableCount = availableCount;
	}

	public int getLeftSku() {
		return leftSku;
	}

	public void setLeftSku(int leftSku) {
		this.leftSku = leftSku;
	}

	public int getSoldSku() {
		return soldSku;
	}

	public void setSoldSku(int soldSku) {
		this.soldSku = soldSku;
	}

	public int getReturnSku() {
		return returnSku;
	}

	public void setReturnSku(int returnSku) {
		this.returnSku = returnSku;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
