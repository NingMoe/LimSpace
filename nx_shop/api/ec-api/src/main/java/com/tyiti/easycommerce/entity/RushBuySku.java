package com.tyiti.easycommerce.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀活动的商品信息
 * t_rush_buy_sku
 * @author rainyhao
 * @since 2016-4-1 下午3:36:32
 */
public class RushBuySku { 
	// id
	private Integer id;
	// 所关联的活动
	private Integer rushBuyId;
	// 此次活动所关联的商品
	private Integer skuId;
	// 秒杀商品名称
	private String skuName;
	// 商品在此次秒杀的活动价
	private BigDecimal price;
	// 市场价
	private BigDecimal originalPrice;
	// 单个用户可购买数
	private Integer availableCount;
	// 单个用户可秒杀次数
	private Integer availableTimes;
	// 剩余库存量
	private Integer leftSku;
	// 已售出数理
	private Integer soldSku;
	// 过期未支付返库存次数
	private Integer returnSku;
	// 创建时间
	private Date createTime;
	
	// 不参与持久化的属性
	private Sku sku;
	
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
        this.skuName = skuName == null ? null : skuName.trim();
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

	public Integer getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(Integer availableTimes) {
        this.availableTimes = availableTimes;
    }

    public Integer getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(Integer availableCount) {
        this.availableCount = availableCount;
    }

    public Integer getLeftSku() {
        return leftSku;
    }

    public void setLeftSku(Integer leftSku) {
        this.leftSku = leftSku;
    }

    public Integer getSoldSku() {
        return soldSku;
    }

    public void setSoldSku(Integer soldSku) {
        this.soldSku = soldSku;
    }

    public Integer getReturnSku() {
        return returnSku;
    }

    public void setReturnSku(Integer returnSku) {
        this.returnSku = returnSku;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}
}
