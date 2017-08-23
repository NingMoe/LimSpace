package com.tyiti.easycommerce.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.tyiti.easycommerce.base.BaseModel;

public class Refund extends BaseModel {
	private Integer id;

	private Integer orderId;

	private Integer cancellationId;

	private BigDecimal amount;

	private Integer type;

	private Date createTime;

	private Date refundTime;

	private Integer status;

	private Integer refundType ;
	

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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

 

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCancellationId() {
		return cancellationId;
	}

	public void setCancellationId(Integer cancellationId) {
		this.cancellationId = cancellationId;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}
}