package com.tyiti.easycommerce.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author shenzhiqiang
 * @ClassName: ActivitySku 
 * @Description: 
 * @date 2016年7月11日 下午4:14:55
 */
public class ActivitySku {

	private Integer id;
	
	private Integer activityId;  //活动ID
	
	private Integer skuId;  //skuId
	
	private String skuName;  //商品名称
	
	private BigDecimal price;  //商品原价
	 
	private BigDecimal activityPrice;  //活动价格
	
	private Integer inventory;  //预留库存
	
	private Integer reservedInventory;  //剩余预留库存
	
	private BigDecimal discountedPrice;  //优惠金额
	
	private BigDecimal discount;  //优惠折扣
	
	private Integer purchaseNum;  //限购个数
	
	private Integer soldNum;  //已售个数
	
	private Integer topRank;//商品排序
	
    private Date createTime;

    private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
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

	public BigDecimal getActivityPrice() {
		return activityPrice;
	}

	public void setActivityPrice(BigDecimal activityPrice) {
		this.activityPrice = activityPrice;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public Integer getReservedInventory() {
		return reservedInventory;
	}

	public void setReservedInventory(Integer reservedInventory) {
		this.reservedInventory = reservedInventory;
	}

	public BigDecimal getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(BigDecimal discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public Integer getPurchaseNum() {
		return purchaseNum;
	}

	public void setPurchaseNum(Integer purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

	public Integer getSoldNum() {
		return soldNum;
	}

	public void setSoldNum(Integer soldNum) {
		this.soldNum = soldNum;
	}

	
	/**
	 * topRank
	 *
	 * @return  the topRank
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Integer getTopRank() {
		return topRank;
	}

	
	/**
	 * @param topRank the topRank to set
	 */
	
	public void setTopRank(Integer topRank) {
		this.topRank = topRank;
	}

	


	
	
	/**
	 * createTime
	 *
	 * @return  the createTime
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Date getCreateTime() {
		return createTime;
	}

	
	/**
	 * @param createTime the createTime to set
	 */
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * updateTime
	 *
	 * @return  the updateTime
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Date getUpdateTime() {
		return updateTime;
	}

	
	/**
	 * @param updateTime the updateTime to set
	 */
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
