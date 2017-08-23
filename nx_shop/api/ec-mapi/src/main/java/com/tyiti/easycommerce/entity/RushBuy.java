package com.tyiti.easycommerce.entity;

import java.util.Date;

/**
 * 通用秒杀
 * Title:RushBuy.java
 * Description:
 * @author: xulihui
 * @date: 2016年4月8日 下午5:47:56
 */
public class RushBuy {
		  
	private  int  id;//活动名称
	private  String  name;//活动名称
	private  String needAppoint;// 是否需要预约, 0不需要,1需要, 默认0 
	private  Date appointStartTime;//开始接受预约时间, 当need_appoint=0的时候可空 
	private  Date appointEndTime;//停止接受预约时间, 当need_appoint=0的时候可空 
	private  Date startTime;//秒杀开始时间 
	private  Date endTime;//秒杀结束时间 
	private  Date createTime;//活动创建时间
	private  String invalid;//是否删除,0:未删除,1:删除
	
	
	
	public RushBuy() {
		super();
	}
	
	public RushBuy(int id, String name, String needAppoint, Date appointStartTime, Date appointEndTime, Date startTime,
			Date endTime, Date createTime, String invalid) {
		super();
		this.id = id;
		this.name = name;
		this.needAppoint = needAppoint;
		this.appointStartTime = appointStartTime;
		this.appointEndTime = appointEndTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.createTime = createTime;
		this.invalid = invalid;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNeedAppoint() {
		return needAppoint;
	}

	public void setNeedAppoint(String needAppoint) {
		this.needAppoint = needAppoint;
	}

	public String getInvalid() {
		return invalid;
	}

	public void setInvalid(String invalid) {
		this.invalid = invalid;
	}

	public Date getAppointStartTime() {
		return appointStartTime;
	}
	public void setAppointStartTime(Date appointStartTime) {
		this.appointStartTime = appointStartTime;
	}
	public Date getAppointEndTime() {
		return appointEndTime;
	}
	public void setAppointEndTime(Date appointEndTime) {
		this.appointEndTime = appointEndTime;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
