package com.tyiti.easycommerce.entity;

public class SpuAttribute {
    private Integer id;

    private Integer spuId;

    private Integer attributeId;

    private int valueConstraintType;

    private String valueConstraintExpression;

    private Integer rank;

    private String  virtualId ;  
    
    private Attribute attribute;

    private String attributeValues ;
    
    private String attrValues ; 
	public String getAttrValues() {
		return attrValues;
	}

	public void setAttrValues(String attrValues) {
		this.attrValues = attrValues;
	}

	public String getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(String attributeValues) {
		this.attributeValues = attributeValues;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public String getVirtualId() {
		return virtualId;
	}

	public void setVirtualId(String virtualId) {
		this.virtualId = virtualId;
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

    public int getValueConstraintType() {
        return valueConstraintType;
    }

    public void setValueConstraintType(int valueConstraintType) {
        this.valueConstraintType = valueConstraintType;
    }

    public String getValueConstraintExpression() {
        return valueConstraintExpression;
    }

    public void setValueConstraintExpression(String valueConstraintExpression) {
        this.valueConstraintExpression = valueConstraintExpression == null ? null : valueConstraintExpression.trim();
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}