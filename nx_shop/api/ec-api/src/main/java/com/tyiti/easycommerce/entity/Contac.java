package com.tyiti.easycommerce.entity;

public class Contac {

	// 联系人信息ID
	private String id;
	// 联系人姓名
	private String contactName;
	// 联系人电话
	private String contactPhone;
	// 类型 A1:直系亲属 A2:其他
	private String type;
	// 关系 A1:父母 A2:配偶 A3:子女
	private String relationship;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

}