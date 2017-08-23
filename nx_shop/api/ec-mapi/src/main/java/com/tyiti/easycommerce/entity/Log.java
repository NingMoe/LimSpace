package com.tyiti.easycommerce.entity;

import java.util.Date;

import com.tyiti.easycommerce.base.BaseModel;

public class Log extends BaseModel{
    private Integer id;

    private Integer operatorId;

    private String operatorName;

    private Integer keyId;

    private Date createTime;

    private String ip;

    private Integer operateModel;

    private Integer action;

    private String message;

    private Integer source;

    private Integer successStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Integer getKeyId() {
		return keyId;
	}

	public void setKeyId(Integer keyId) {
		this.keyId = keyId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getOperateModel() {
		return operateModel;
	}

	public void setOperateModel(Integer operateModel) {
		this.operateModel = operateModel;
	}

	public Integer getAction() {
		return action;
	}

	public void setAction(Integer action) {
		this.action = action;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getSuccessStatus() {
		return successStatus;
	}

	public void setSuccessStatus(Integer successStatus) {
		this.successStatus = successStatus;
	}

	public Log(Integer operatorId, String operatorName, Integer keyId,
			Date createTime, String ip, Integer operateModel, Integer action,
			String message, Integer source, Integer successStatus) {
		super();
		this.operatorId = operatorId;
		this.operatorName = operatorName;
		this.keyId = keyId;
		this.createTime = createTime;
		this.ip = ip;
		this.operateModel = operateModel;
		this.action = action;
		this.message = message;
		this.source = source;
		this.successStatus = successStatus;
	}
    
}