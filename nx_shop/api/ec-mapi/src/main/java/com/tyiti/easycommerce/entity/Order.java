package com.tyiti.easycommerce.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.tyiti.easycommerce.base.BaseModel;

public class Order extends BaseModel{
    private Integer id;

    private String no;

    private Integer custId;

    private BigDecimal amount;

    private BigDecimal thirdPartyAmount;

    private BigDecimal downPayment;

    private BigDecimal installmentAmount;

    private Integer installmentMonths;

    private BigDecimal installmentRate;

    private Boolean downPaymentPayed;

    private Boolean installmentPayed;

    private Date downPaymentTime;

    private Date installmentTime;

    private Byte payMethod;

    private Integer status;

    private Boolean invalid;

    private Date createTime;

    private Date closeTime;

    private String addressName;

    private String addressMobile;

    private String addressFullText;

    private Integer addressZip;

    private String logisticsNo;

    private String logisticsCompany;

    private Date receiptTime;

    private Date returnTime;

    private String stageId;

    private List<OrderSku> orderSku ;
    
    private BigDecimal discount ;
    
    private String mobile;
    
    private OrderCancellation cancellation;
private Date deliveryTime;
	/**
     * ��Ʊ wyy 2016/09/01
     */
    private String invoiceTitle;
	private Integer invoiceType;
    private Integer invoiceStatus;
    
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
        this.no = no == null ? null : no.trim();
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

    public BigDecimal getThirdPartyAmount() {
        return thirdPartyAmount;
    }

    public void setThirdPartyAmount(BigDecimal thirdPartyAmount) {
        this.thirdPartyAmount = thirdPartyAmount;
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

    public BigDecimal getInstallmentRate() {
        return installmentRate;
    }

    public void setInstallmentRate(BigDecimal installmentRate) {
        this.installmentRate = installmentRate;
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

    public Byte getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Byte payMethod) {
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

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName == null ? null : addressName.trim();
    }

    public String getAddressMobile() {
        return addressMobile;
    }

    public void setAddressMobile(String addressMobile) {
        this.addressMobile = addressMobile == null ? null : addressMobile.trim();
    }

    public String getAddressFullText() {
        return addressFullText;
    }

    public void setAddressFullText(String addressFullText) {
        this.addressFullText = addressFullText == null ? null : addressFullText.trim();
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
        this.logisticsNo = logisticsNo == null ? null : logisticsNo.trim();
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany == null ? null : logisticsCompany.trim();
    }

    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId == null ? null : stageId.trim();
    }

	public List<OrderSku> getOrderSku() {
		return orderSku;
	}

	public void setOrderSku(List<OrderSku> orderSku) {
		this.orderSku = orderSku;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public OrderCancellation getCancellation() {
		return cancellation;
	}

	public void setCancellation(OrderCancellation cancellation) {
		this.cancellation = cancellation;
	}
	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
}
