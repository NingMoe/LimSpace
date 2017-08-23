package com.tyiti.easycommerce.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author wyy
 * @description 报警项
 *
 */
public class WarningItem{	
	private Integer id; 
	private String name; 	
	private Integer sendSms;
	private Integer sendEmail;
	private String smsTpl;
	private String emailTitle;
	private String emailTpl;
	private Integer invalid; 
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime; 
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSendSms() {
		return sendSms;
	}
	public void setSendSms(Integer sendSms) {
		this.sendSms = sendSms;
	}
	public Integer getSendEmail() {
		return sendEmail;
	}
	public void setSendEmail(Integer sendEmail) {
		this.sendEmail = sendEmail;
	}
	public String getSmsTpl() {
		return smsTpl;
	}
	public void setSmsTpl(String smsTpl) {
		this.smsTpl = smsTpl;
	}
	public String getEmailTitle() {
		return emailTitle;
	}
	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}
	public String getEmailTpl() {
		return emailTpl;
	}
	public void setEmailTpl(String emailTpl) {
		this.emailTpl = emailTpl;
	}
	public Integer getInvalid() {
		return invalid;
	}
	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
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
		
	
}