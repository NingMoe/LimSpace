package com.tyiti.easycommerce.entity;

public class SpuAttribute {
	private Integer id;
	private Integer spuId;
	private Integer attributeId;
	private Integer valueConstraintType;
	private String valueConstraintExpression;
	private Integer rank;
	private Attribute attr;
	 private String attrValues ; 
	
	public String getAttrValues() {
		return attrValues;
	}

	public void setAttrValues(String attrValues) {
		this.attrValues = attrValues;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSpuId() {
		return spuId;
	}

	public void setSpuId(Integer spuId) {
		this.spuId = spuId;
	}

	public Integer getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
	}

	public Integer getValueConstraintType() {
		return valueConstraintType;
	}

	public void setValueConstraintType(Integer valueConstraintType) {
		this.valueConstraintType = valueConstraintType;
	}

	public String getValueConstraintExpression() {
		return valueConstraintExpression;
	}

	public void setValueConstraintExpression(String valueConstraintExpression) {
		this.valueConstraintExpression = valueConstraintExpression;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Attribute getAttr() {
		return attr;
	}

	public void setAttr(Attribute attr) {
		this.attr = attr;
	}
}
