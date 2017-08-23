package com.tyiti.easycommerce.entity;

import java.util.Date;
import java.util.List;

public class PickupOrder {
    private Integer id;

    private Integer orderId;

    private Integer pickupPointId;

    private String code;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer invalid;
    
    private Date arrivetime;// 到货时间
    private Date taketime;// 提货时间
    
    
    public Date getArrivetime() {
		return arrivetime;
	}

	public void setArrivetime(Date arrivetime) {
		this.arrivetime = arrivetime;
	}

	public Date getTaketime() {
		return taketime;
	}

	public void setTaketime(Date taketime) {
		this.taketime = taketime;
	}

	private List<OrderPayment> orderPaymentList;
    
    private PickupPoint pickupPoint;
    
        
    private OrderCancellation orderCancellation;
    
    
    public OrderCancellation getOrderCancellation() {
		return orderCancellation;
	}

	public void setOrderCancellation(OrderCancellation orderCancellation) {
		this.orderCancellation = orderCancellation;
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

	private Order order;
    

    public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
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

    public Integer getPickupPointId() {
        return pickupPointId;
    }

    public void setPickupPointId(Integer pickupPointId) {
        this.pickupPointId = pickupPointId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getInvalid() {
        return invalid;
    }

    public void setInvalid(Integer invalid) {
        this.invalid = invalid;
    }
}