package com.tyiti.easycommerce.entity;

public class SkuAttribute {
	private Integer id;
	private Integer skuId;
	private Integer spuAttributeId;
	private String attributeValue;
	private Attribute attribute;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Integer getSpuAttributeId() {
		return spuAttributeId;
	}

	public void setSpuAttributeId(Integer spuAttributeId) {
		this.spuAttributeId = spuAttributeId;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
}
