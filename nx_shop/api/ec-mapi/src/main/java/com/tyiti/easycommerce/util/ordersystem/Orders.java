package com.tyiti.easycommerce.util.ordersystem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单表
 * 
 * @author: xulihui
 * @date: 2016年4月29日 上午11:01:31
 */
public class Orders   {
	
	private Integer shopOrderId ; // 商城订单id
	
	private Integer id;// ID
	
	private Integer cancelStatus ; // 取消状态
	
	private Integer shopOrderStatus ; // 商城订单状态
	private Integer bankId;// 银行ID

	private String bankName;// 银行名

	private String no;// 订单号

	private String bookingDate;// 订购日期

	private Date preDeliveryDate;// 发货日期 表示从银行下单的日期之后几天发货

	private String customer;// 客户名称

	private String customerTel;// 客户电话

	private String otherContact;// 客户的其他联系方式

	private String customerAddress;// 客户地址

	private String customerZipCode;// 客户邮编

	private String creditCard;// 信用卡号

	private String skuName;// 商品名称 (从银行过来的统称商品 ,系统内部的称为产品)

	private Integer skuId;// 商品ID
	
	private String skuNo;// 商品编码

	private BigDecimal skuPrice;// 商品价格

	private BigDecimal paidAmount;// 客户支付金额

	private BigDecimal bankPoints;// 银行扣点 即: 银行搜抽取的利润

	private String gift;// 赠品

	private String terms;// 分期数

	private Integer parentId;// 主订单ID ; 当Type 为3的时候,不为0; 当Type为1,2 的时候,为0
	
//	private OrderConfirmType confirmType; // 订单确认方式
	
	private Integer mainProdId; // 订单产品中,唯一一个主商品所对应产品的id
	
	private String mainProdName; // 订单产品中,唯一一个主商品所对应产品的名称
	
	private Integer mainProdCatid; // 订单产品,唯一一个主商品所对应产品的分类id
	
	private String mainProdCat; // 订单产品中,唯一一个主商品所对应产品的分类名

//	private OrderType type;// 订单类型 ,1:常规订单 ,2:搭销订单,3:被搭订单 即 子订单
	/**
	 * [所有状态环节改变都是人工操作实现的,不需要程序自动更改]
	 * 
	 * 订单状态: 1:未处理  0:搭销中  2:订单确认(已确认)  3:已制单 4:已发货 5:已完成 6:申请取消 7:确认取消 
	 * 8:已取消  (取消环节结束,审批通过订单被导出之后)
	 */
//	private OrderStatus status;
	/**
	 * 退货状态: 
	 * 0:无退货 1:申请退货 (等待库房验收退货) 2:退货收回(库房通过验货,并确认收回) 3:确认退货(客服已知道库房验货通过)
	 * 4:已退货 (状态为3的已被导出之后,自动把此值变为已退货) 5:退货驳回(此次退货申请被取消) 6:确认换货(客服向客户确认后换货)
	 */
//	private ReturnStatus returnStatus;
	
//	private ReturnType returnType; // 退单类型
	
	private Boolean redelivery;

	private Integer userId;// 处理人ID

	private String userName;// 处理人姓名
	
	private Integer salesId; // 业务员id,主订单为银行信息中的客户经理, 子订单为搭销商品中的客服id
	
	private String salesName; // 业务员姓名

	private String invoice;// 发票抬头

	private String requirement;// 特殊要求
	
	private String requiredLogistics;//客户要求的物流

	private String buyerName;// 收货人姓名

	private String buyerTel;// 收货人电话
	
	private String buyerProv; // 收货省
	
	private String buyerCity; // 收货市
	
	private String buyerArea; // 收货县

	private String buyerAddress;// 送货地址

	private String buyerZipCode;// 送货的邮编

	private String remark;// 客户备注

	private Boolean isPriority;// 是否优先发货; 0 否,1 是

	private String logistics;// 发货方式 ->快递公司 eg EMS

	private String logisticsNo;// 发货单号 ->运单号

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date deliveryTime;// 发货时间

	private Date createTime;// 入库时间
	
	private String subNo;//子订单编号
	
	private String otype;//订单类型(输入)
	
	private String  idno;//身份证号
	
	private Integer skuCount;//购买数量
	
	private BigDecimal totalAmount;//订单金额
	
	private BigDecimal bankFee;//银行手续费
	
	private BigDecimal shouldBackAmount;//应回款金额
	
	private String coupon;//优惠券
	
	private Integer score;//积分
	
	private String  subsidy;//其他补贴
	
	// Transient: 查询参数: 开始时间
	private Date startTime;
	
	// Transient: 查询参数: 结束时间
	private Date endTime;
	// Transient: 查询参数：开始时间
	private Date bookingStartTime;
	// Transient: 查询参数 :结束时间
	private Date bookingEndTime;
	
	// Transient 非持久化属性, 表示要查询的列
	private String fields;
	
//	private List<OrderStatus> statusToQuery;
    // Transient 查询参数, 一串用逗号隔开的id
	private String ids;
	// Transient 查询参数, 模糊查询产品名称
	private String likeProdName;
	// Transient 查询参数, 银行分类id
	private Integer bankCategoryId;
	// Transient 查询参数 ,产品名称
    private String prodName;
    // Transient  查询参数,按 desc asc(默认)
    private String sort;
    // Transient  一组bankId
    private List<Integer> bankIds;
    // Transient  按优先级排序
    private String priorityLevel;
    
    private Integer requiredLogisticsId; // 客户要求物流id，在字典表中根据id查询，可填可不填
    
    private String userUuid; // 任务ID，用户在制单过程中出现系统崩溃等现象，记录制单成功的订单，用户可手动导出
    
    // Transient 8位数uuid,作为导出订单时的批次
    private String eightUuid;
    // Transient 制单时间
    private Date makeListDate;
    // Transient 待转型
    private String transform;
    // Transient 购买颜色
    private String buyColor;
    // Transient 保价金额
    private String ensureMoney;
    // Transient 保价费用
    private String ensureCost;
    // Transient 产品成本价
    private String productCost;
    // Transient 商品号 -作废
    private String skuNumber;
    // Transient 多个产品名称  导出数据的时候用作产品
    private String skuNames;
    // Transient 创建时间排序
    private String createTimeSort;
    // Transient 主产品一级分类
    private String oneLevelMainProdCat;
    // Transient 其他特殊银行id
    private Integer otherBankId;
    // Transient 按订单号模糊查询
    private String likeNo;
	// Transient  查出日志拼接的字段 create_time| event| user_name| message; 格式拼接 
	private String orderLogs; // 订单日志
	// Transient 一组父订单id
	private String parentIds;
	// 是否需要催单
	private Boolean isRemind;
	// 是否需要售后
	private Boolean isAfterSales;
	// Transient 完整收货地址
	private String completeBuyerAddress;
	
	// 是否申请取消（或者转型）  0:否 1:申请取消，2:申请转型；默认0
	private Integer isApplyCancel;
	// 取消/退货时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date cancelReturnDate;
	// 是否转型 
	private Boolean isTransform;
	// 是否有效  0：有效，1：无效
	private Boolean isValid;
	// Transient:查询参数， 制单限制单数
	private Integer limit;
	// 发货批次号，记录不同批次制单的订单
	private String deliverUuid;
	// Transient: 物流状态
    private String expStatus;
    // Transient: 根据用户名精确查找
    private String preciseCustomer;
    
    // 规格(该字段由订单导入)
    private String specification;
    
    // 临时参数，订单状态
    private Integer statusVal;
    // 临时参数，订单包含产品
    private List<Sku> skus;
    // 物流是否已签收
    private Boolean isCheck;
    
    // 查询条件：主产品产品经理名称
    private String productMgrName;
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName == null ? null : bankName.trim();
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no == null ? null : no.trim();
	}

 

	public Date getPreDeliveryDate() {
		return preDeliveryDate;
	}

	public void setPreDeliveryDate(Date preDeliveryDate) {
		this.preDeliveryDate = preDeliveryDate;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer == null ? null : customer.trim();
	}

	public String getCustomerTel() {
		return customerTel;
	}

	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel == null ? null : customerTel.trim();
	}

	public String getOtherContact() {
		return otherContact;
	}

	public void setOtherContact(String otherContact) {
		this.otherContact = otherContact == null ? null : otherContact.trim();
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress == null ? null : customerAddress.trim();
	}

	public String getCustomerZipCode() {
		return customerZipCode;
	}

	public void setCustomerZipCode(String customerZipCode) {
		this.customerZipCode = customerZipCode == null ? null : customerZipCode.trim();
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard == null ? null : creditCard.trim();
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName == null ? null : skuName.trim();
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}

	public BigDecimal getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(BigDecimal skuPrice) {
		this.skuPrice = skuPrice;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getBankPoints() {
		return bankPoints;
	}

	public void setBankPoints(BigDecimal bankPoints) {
		this.bankPoints = bankPoints;
	}

	public String getGift() {
		return gift;
	}

	public void setGift(String gift) {
		this.gift = gift == null ? null : gift.trim();
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms == null ? null : terms.trim();
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

//	public OrderConfirmType getConfirmType() {
//		return confirmType;
//	}
//
//	public void setConfirmType(OrderConfirmType confirmType) {
//		this.confirmType = confirmType;
//	}

	public Integer getMainProdId() {
		return mainProdId;
	}

	public void setMainProdId(Integer mainProdId) {
		this.mainProdId = mainProdId;
	}

	public String getMainProdName() {
		return mainProdName;
	}

	public void setMainProdName(String mainProdName) {
		this.mainProdName = mainProdName;
	}

	public Integer getMainProdCatid() {
		return mainProdCatid;
	}

	public void setMainProdCatid(Integer mainProdCatid) {
		this.mainProdCatid = mainProdCatid;
	}

	public String getMainProdCat() {
		return mainProdCat;
	}

	public void setMainProdCat(String mainProdCat) {
		this.mainProdCat = mainProdCat;
	}

//	public OrderType getType() {
//		return type;
//	}
//
//	public void setType(OrderType type) {
//		this.type = type;
//	}
//
//	public OrderStatus getStatus() {
//		return status;
//	}
//
//	public void setStatus(OrderStatus status) {
//		this.status = status;
//	}
//
//	public ReturnStatus getReturnStatus() {
//		return returnStatus;
//	}
//
//	public void setReturnStatus(ReturnStatus returnStatus) {
//		this.returnStatus = returnStatus;
//	}
//	
//	public String getReturnStatusText() {
//		return null == returnStatus ? "" : returnStatus.getText();
//	}
//    
//	public ReturnType getReturnType() {
//		return returnType;
//	}
//
//	public void setReturnType(ReturnType returnType) {
//		this.returnType = returnType;
//	}
//	
//	public String getReturnTypeText() {
//		return null == returnType ? "" : returnType.getText();
//	}

	public Boolean getRedelivery() {
		return redelivery;
	}

	public void setRedelivery(Boolean redelivery) {
		this.redelivery = redelivery;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public Integer getSalesId() {
		return salesId;
	}

	public void setSalesId(Integer salesId) {
		this.salesId = salesId;
	}

	public String getSalesName() {
		return salesName;
	}

	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice == null ? null : invoice.trim();
	}

	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement == null ? null : requirement.trim();
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName == null ? null : buyerName.trim();
	}

	public String getBuyerTel() {
		return buyerTel;
	}

	public void setBuyerTel(String buyerTel) {
		this.buyerTel = buyerTel == null ? null : buyerTel.trim();
	}

	public String getBuyerProv() {
		return buyerProv;
	}

	public void setBuyerProv(String buyerProv) {
		this.buyerProv = buyerProv;
	}

	public String getBuyerCity() {
		return buyerCity;
	}

	public void setBuyerCity(String buyerCity) {
		this.buyerCity = buyerCity;
	}

	public String getBuyerArea() {
		return buyerArea;
	}

	public void setBuyerArea(String buyerArea) {
		this.buyerArea = buyerArea;
	}

	public String getBuyerAddress() {
		return buyerAddress;
	}

	public void setBuyerAddress(String buyerAddress) {
		this.buyerAddress = buyerAddress == null ? null : buyerAddress.trim();
	}

	public String getBuyerZipCode() {
		return buyerZipCode;
	}

	public void setBuyerZipCode(String buyerZipCode) {
		this.buyerZipCode = buyerZipCode == null ? null : buyerZipCode.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Boolean getIsPriority() {
		return isPriority;
	}

	public void setIsPriority(Boolean isPriority) {
		this.isPriority = isPriority;
	}

	public String getLogistics() {
		return logistics;
	}

	public void setLogistics(String logistics) {
		this.logistics = logistics == null ? null : logistics.trim();
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo == null ? null : logisticsNo.trim();
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

 
 
 
	public String getRequiredLogistics() {
		return requiredLogistics;
	}

	public void setRequiredLogistics(String requiredLogistics) {
		this.requiredLogistics = requiredLogistics;
	}


	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	public String getLikeProdName() {
		return likeProdName;
	}

	public void setLikeProdName(String likeProdName) {
		this.likeProdName = likeProdName;
	}

	public Integer getBankCategoryId() {
		return bankCategoryId;
	}

	public void setBankCategoryId(Integer bankCategoryId) {
		this.bankCategoryId = bankCategoryId;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSubNo() {
		return subNo;
	}

	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}

	public String getOtype() {
		return otype;
	}

	public void setOtype(String otype) {
		this.otype = otype;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public Integer getSkuCount() {
		return skuCount;
	}

	public void setSkuCount(Integer skuCount) {
		this.skuCount = skuCount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getBankFee() {
		return bankFee;
	}

	public void setBankFee(BigDecimal bankFee) {
		this.bankFee = bankFee;
	}

	public BigDecimal getShouldBackAmount() {
		return shouldBackAmount;
	}

	public void setShouldBackAmount(BigDecimal shouldBackAmount) {
		this.shouldBackAmount = shouldBackAmount;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getSubsidy() {
		return subsidy;
	}

	public void setSubsidy(String subsidy) {
		this.subsidy = subsidy;
	}

	public List<Integer> getBankIds() {
		return bankIds;
	}

	public void setBankIds(List<Integer> bankIds) {
		this.bankIds = bankIds;
	}

	public Date getBookingStartTime() {
		return bookingStartTime;
	}

	public void setBookingStartTime(Date bookingStartTime) {
		this.bookingStartTime = bookingStartTime;
	}

	public Date getBookingEndTime() {
		return bookingEndTime;
	}

	public void setBookingEndTime(Date bookingEndTime) {
		this.bookingEndTime = bookingEndTime;
	}

	public Integer getRequiredLogisticsId() {
		return requiredLogisticsId;
	}

	public void setRequiredLogisticsId(Integer requiredLogisticsId) {
		this.requiredLogisticsId = requiredLogisticsId;
	}

	public String getPriorityLevel() {
		return priorityLevel;
	}

	public void setPriorityLevel(String priorityLevel) {
		this.priorityLevel = priorityLevel;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getEightUuid() {
		return eightUuid;
	}

	public void setEightUuid(String eightUuid) {
		this.eightUuid = eightUuid;
	}

	public Date getMakeListDate() {
		return makeListDate;
	}

	public void setMakeListDate(Date makeListDate) {
		this.makeListDate = makeListDate;
	}

	public String getTransform() {
		return transform;
	}

	public void setTransform(String transform) {
		this.transform = transform;
	}

	public String getBuyColor() {
		return buyColor;
	}

	public void setBuyColor(String buyColor) {
		this.buyColor = buyColor;
	}

	public String getEnsureMoney() {
		return ensureMoney;
	}

	public void setEnsureMoney(String ensureMoney) {
		this.ensureMoney = ensureMoney;
	}

	public String getEnsureCost() {
		return ensureCost;
	}

	public void setEnsureCost(String ensureCost) {
		this.ensureCost = ensureCost;
	}

	public String getProductCost() {
		return productCost;
	}

	public void setProductCost(String productCost) {
		this.productCost = productCost;
	}

	public String getSkuNumber() {
		return skuNumber;
	}

	public void setSkuNumber(String skuNumber) {
		this.skuNumber = skuNumber;
	}

	public String getSkuNames() {
		return skuNames;
	}

	public void setSkuNames(String skuNames) {
		this.skuNames = skuNames;
	}

	public String getCreateTimeSort() {
		return createTimeSort;
	}

	public void setCreateTimeSort(String createTimeSort) {
		this.createTimeSort = createTimeSort;
	}

	public String getOneLevelMainProdCat() {
		return oneLevelMainProdCat;
	}

	public void setOneLevelMainProdCat(String oneLevelMainProdCat) {
		this.oneLevelMainProdCat = oneLevelMainProdCat;
	}

	public Integer getOtherBankId() {
		return otherBankId;
	}

	public void setOtherBankId(Integer otherBankId) {
		this.otherBankId = otherBankId;
	}

	public String getLikeNo() {
		return likeNo;
	}

	public void setLikeNo(String likeNo) {
		this.likeNo = likeNo;
	}

	public String getOrderLogs() {
		return orderLogs;
	}

	public void setOrderLogs(String orderLogs) {
		this.orderLogs = orderLogs;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Boolean getIsRemind() {
		return isRemind;
	}

	public void setIsRemind(Boolean isRemind) {
		this.isRemind = isRemind;
	}

	public Boolean getIsAfterSales() {
		return isAfterSales;
	}

	public void setIsAfterSales(Boolean isAfterSales) {
		this.isAfterSales = isAfterSales;
	}

	public Boolean getIsTransform() {
		return isTransform;
	}

	public void setIsTransform(Boolean isTransform) {
		this.isTransform = isTransform;
	}

	public String getCompleteBuyerAddress() {
		return completeBuyerAddress;
	}

	public void setCompleteBuyerAddress(String completeBuyerAddress) {
		this.completeBuyerAddress = completeBuyerAddress;
	}

	public Date getCancelReturnDate() {
		return cancelReturnDate;
	}

	public void setCancelReturnDate(Date cancelReturnDate) {
		this.cancelReturnDate = cancelReturnDate;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getDeliverUuid() {
		return deliverUuid;
	}

	public void setDeliverUuid(String deliverUuid) {
		this.deliverUuid = deliverUuid;
	}

	public String getExpStatus() {
		return expStatus;
	}

	public void setExpStatus(String expStatus) {
		this.expStatus = expStatus;
	}

	public String getPreciseCustomer() {
		return preciseCustomer;
	}

	public void setPreciseCustomer(String preciseCustomer) {
		this.preciseCustomer = preciseCustomer;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Integer getStatusVal() {
		return statusVal;
	}

	public void setStatusVal(Integer statusVal) {
		this.statusVal = statusVal;
	}
	
	public List<Sku> getSkus() {
		return skus;
	}

	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}

	public Boolean getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getProductMgrName() {
		return productMgrName;
	}

	public void setProductMgrName(String productMgrName) {
		this.productMgrName = productMgrName;
	}

	public Integer getIsApplyCancel() {
		return isApplyCancel;
	}

	public void setIsApplyCancel(Integer isApplyCancel) {
		this.isApplyCancel = isApplyCancel;
	}

	public Integer getShopOrderId() {
		return shopOrderId;
	}

	public void setShopOrderId(Integer shopOrderId) {
		this.shopOrderId = shopOrderId;
	}

	public Integer getShopOrderStatus() {
		return shopOrderStatus;
	}

	public void setShopOrderStatus(Integer shopOrderStatus) {
		this.shopOrderStatus = shopOrderStatus;
	}

	public Integer getCancelStatus() {
		return cancelStatus;
	}

	public void setCancelStatus(Integer cancelStatus) {
		this.cancelStatus = cancelStatus;
	}

	public String getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}
    
}