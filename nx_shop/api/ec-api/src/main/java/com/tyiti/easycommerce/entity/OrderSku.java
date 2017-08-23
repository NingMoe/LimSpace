package com.tyiti.easycommerce.entity;

import java.math.BigDecimal;

public class OrderSku {
	private Integer id;
	private Integer orderId;
	private Integer skuId;
	private String skuName;
	private String skuDescription;
	private String skuDetail;
	private String skuErpCode;
	private String skuHeadThumbnail;
	private Integer skuCount;
	private BigDecimal skuPrice;
	private BigDecimal skuOriginalPrice;
	private BigDecimal commissionRate;//佣金比例
	private String thirdPartyId;//第三方ID
	private String skuAttribute;
	// 所参与的活动类型1,秒杀 2,优惠券
	private Integer activityType;
	// 所参与的活动id(具体是什么活动由activityType决定)
	private Integer activityId;
	
	// 非持久化参数, 用户id
	private Integer custId;
	
	private BigDecimal avgPrice ;
	private BigDecimal  factAmount ;
	
	private Integer returnCount ; 

	public BigDecimal getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(BigDecimal avgPrice) {
		this.avgPrice = avgPrice;
	}

 

	public BigDecimal getFactAmount() {
		return factAmount;
	}

	public void setFactAmount(BigDecimal factAmount) {
		this.factAmount = factAmount;
	}

	public Integer getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(Integer returnCount) {
		this.returnCount = returnCount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	public String getSkuDescription() {
		return skuDescription;
	}

	public void setSkuDescription(String skuDescription) {
		this.skuDescription = skuDescription;
	}

	public String getSkuDetail() {
		return skuDetail;
	}

	public void setSkuDetail(String skuDetail) {
		this.skuDetail = skuDetail;
	}

	public String getSkuErpCode() {
		return skuErpCode;
	}

	public void setSkuErpCode(String skuErpCode) {
		this.skuErpCode = skuErpCode;
	}

	public String getSkuHeadThumbnail() {
		return skuHeadThumbnail;
	}

	public void setSkuHeadThumbnail(String skuHeadThumbnail) {
		this.skuHeadThumbnail = skuHeadThumbnail;
	}

	public Integer getSkuCount() {
		return skuCount;
	}

	public void setSkuCount(Integer skuCount) {
		this.skuCount = skuCount;
	}

	public BigDecimal getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(BigDecimal skuPrice) {
		this.skuPrice = skuPrice;
	}

	public BigDecimal getSkuOriginalPrice() {
		return skuOriginalPrice;
	}

	public void setSkuOriginalPrice(BigDecimal skuOriginalPrice) {
		this.skuOriginalPrice = skuOriginalPrice;
	}

	public String getSkuAttribute() {
		return skuAttribute;
	}

	public void setSkuAttribute(String skuAttribute) {
		this.skuAttribute = skuAttribute;
	}

	public Integer getActivityType() {
		return activityType;
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	
	/**
	 * commissionRate
	 *
	 * @return  the commissionRate
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public BigDecimal getCommissionRate() {
		return commissionRate;
	}

	
	/**
	 * @param commissionRate the commissionRate to set
	 */
	
	public void setCommissionRate(BigDecimal commissionRate) {
		this.commissionRate = commissionRate;
	}

	
	/**
	 * thirdPartyId
	 *
	 * @return  the thirdPartyId
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public String getThirdPartyId() {
		return thirdPartyId;
	}

	
	/**
	 * @param thirdPartyId the thirdPartyId to set
	 */
	
	public void setThirdPartyId(String thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}
	
}
