package com.tyiti.easycommerce.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Order {
	private Integer id;
	private String no;
	private Integer custId;
	private BigDecimal amount;
	private BigDecimal thirdPartyAmount;//第三方平台订单总金额
	private BigDecimal downPayment;
	private BigDecimal installmentAmount;
	private BigDecimal installmentRate;//分期（每期）费率
	private Integer installmentMonths;
	private Boolean downPaymentPayed;
	private Boolean installmentPayed;
	private Date downPaymentTime;
	private Date installmentTime;
	private Integer payMethod;
	private Integer status;
	private Integer secondStatus;
	private Boolean invalid;
	private Date createTime;
	private Date closeTime;
	private Integer addressId;
	private String addressName;
	private String addressMobile;
	private String addressFullText;
	private Integer addressZip;
	private String logisticsNo;
	private String logisticsCompany;
	
	  /**
     * 发票 wyy 2016/09/07
     */
    private String invoiceTitle;
	private Integer invoiceType;
    private Integer invoiceStatus;

	private Integer skuId;
	private Integer skuCount;
	
	private Integer activityId;

	private Integer cancellationType;

	private List<OrderSku> orderSku;
	private Integer couponRecordId;//用户优惠券记录id
	private String stageId;//信分宝分期id

	private List<OrderPayment> orderPaymentList ; //支付信息
	
	private Integer  pickupPointId ; //自提点id 用于下单
	private Integer pickup ;  // 详情 显示 是否为自提订单
	private PickupPoint pickupPoint ;//自提点信息 用于详情
	
	private String pickupCode ; //订单支付后会有提货码
	
	private Integer pickupStatus ; //自提单状态
	
	private Integer reasonType ; 
	//购物车id
	private List<Cart> cart ; 
	
	private List<Sku> sku ; 
	
	private OrderCancellation cancellation;
	
	private BigDecimal discount ;
	
	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getPickupCode() {
		return pickupCode;
	}

	public void setPickupCode(String pickupCode) {
		this.pickupCode = pickupCode;
	}

	public PickupPoint getPickupPoint() {
		return pickupPoint;
	}

	public void setPickupPoint(PickupPoint pickupPoint) {
		this.pickupPoint = pickupPoint;
	}

	public List<OrderPayment> getOrderPaymentList() {
		return orderPaymentList;
	}

	public void setOrderPaymentList(List<OrderPayment> orderPaymentList) {
		this.orderPaymentList = orderPaymentList;
	}

	public List<OrderSku> getOrderSku() {
		return orderSku;
	}

	public void setOrderSku(List<OrderSku> orderSku) {
		this.orderSku = orderSku;
	}

	public Integer getSecondStatus() {
		return secondStatus;
	}

	public void setSecondStatus(Integer secondStatus) {
		this.secondStatus = secondStatus;
	}

	public Integer getSkuCount() {
		return skuCount;
	}

	public void setSkuCount(Integer skuCount) {
		this.skuCount = skuCount;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getDownPayment() {
		return downPayment;
	}

	public void setDownPayment(BigDecimal downPayment) {
		this.downPayment = downPayment;
	}

	public BigDecimal getInstallmentAmount() {
		return installmentAmount;
	}

	public void setInstallmentAmount(BigDecimal installmentAmount) {
		this.installmentAmount = installmentAmount;
	}

	public Integer getInstallmentMonths() {
		return installmentMonths;
	}

	public void setInstallmentMonths(Integer installmentMonths) {
		this.installmentMonths = installmentMonths;
	}

	public Boolean getDownPaymentPayed() {
		return downPaymentPayed;
	}

	public void setDownPaymentPayed(Boolean downPaymentPayed) {
		this.downPaymentPayed = downPaymentPayed;
	}

	public Boolean getInstallmentPayed() {
		return installmentPayed;
	}

	public void setInstallmentPayed(Boolean installmentPayed) {
		this.installmentPayed = installmentPayed;
	}

	public Date getDownPaymentTime() {
		return downPaymentTime;
	}

	public void setDownPaymentTime(Date downPaymentTime) {
		this.downPaymentTime = downPaymentTime;
	}

	public Date getInstallmentTime() {
		return installmentTime;
	}

	public void setInstallmentTime(Date installmentTime) {
		this.installmentTime = installmentTime;
	}

	public Integer getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getInvalid() {
		return invalid;
	}

	public void setInvalid(Boolean invalid) {
		this.invalid = invalid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public String getAddressMobile() {
		return addressMobile;
	}

	public void setAddressMobile(String addressMobile) {
		this.addressMobile = addressMobile;
	}

	public String getAddressFullText() {
		return addressFullText;
	}

	public void setAddressFullText(String addressFullText) {
		this.addressFullText = addressFullText;
	}

	public Integer getAddressZip() {
		return addressZip;
	}

	public void setAddressZip(Integer addressZip) {
		this.addressZip = addressZip;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public String getLogisticsCompany() {
		return logisticsCompany;
	}

	public void setLogisticsCompany(String logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}

	public Integer getCancellationType() {
		return cancellationType;
	}

	public void setCancellationType(Integer cancellationType) {
		this.cancellationType = cancellationType;
	}

	public Integer getCouponRecordId() {
		return couponRecordId;
	}

	public void setCouponRecordId(Integer couponRecordId) {
		this.couponRecordId = couponRecordId;
	}

	public String getStageId() {
		return stageId;
	}

	public void setStageId(String stageId) {
		this.stageId = stageId;
	}

	public BigDecimal getInstallmentRate() {
		return installmentRate;
	}

	public void setInstallmentRate(BigDecimal installmentRate) {
		this.installmentRate = installmentRate;
	}

	public Integer getPickupPointId() {
		return pickupPointId;
	}

	public void setPickupPointId(Integer pickupPointId) {
		this.pickupPointId = pickupPointId;
	}

	public Integer getReasonType() {
		return reasonType;
	}

	public void setReasonType(Integer reasonType) {
		this.reasonType = reasonType;
	}

	public Integer getPickup() {
		return pickup;
	}

	public void setPickup(Integer pickup) {
		this.pickup = pickup;
	}

	public Integer getPickupStatus() {
		return pickupStatus;
	}

	public void setPickupStatus(Integer pickupStatus) {
		this.pickupStatus = pickupStatus;
	}
	/**
	 * thirdPartyAmount
	 *
	 * @return  the thirdPartyAmount
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public BigDecimal getThirdPartyAmount() {
		return thirdPartyAmount;
	}

	
	/**
	 * @param thirdPartyAmount the thirdPartyAmount to set
	 */
	
	public void setThirdPartyAmount(BigDecimal thirdPartyAmount) {
		this.thirdPartyAmount = thirdPartyAmount;
	}
	
	/**
	 * activityId
	 *
	 * @return  the activityId
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Integer getActivityId() {
		return activityId;
	}

	
	/**
	 * @param activityId the activityId to set
	 */
	
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public List<Cart> getCart() {
		return cart;
	}

	public void setCart(List<Cart> cart) {
		this.cart = cart;
	}

	public List<Sku> getSku() {
		return sku;
	}

	public void setSku(List<Sku> sku) {
		this.sku = sku;
	}

	public OrderCancellation getCancellation() {
		return cancellation;
	}

	public void setCancellation(OrderCancellation cancellation) {
		this.cancellation = cancellation;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

}
