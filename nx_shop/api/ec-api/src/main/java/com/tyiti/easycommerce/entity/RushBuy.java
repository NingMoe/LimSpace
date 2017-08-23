package com.tyiti.easycommerce.entity;

import java.util.Date;

/**
 * 秒杀活动表的列映射
 * t_rush_buy
 * @author rainyhao
 * @since 2016-3-30 下午2:35:10
 */
public class RushBuy {
	// id
	private Integer id;
	// 秒杀活动名称
    private String name;
    // 是否需要预约, false(0)不需要,true(1)需要, 默认false(0)
    private Boolean needAppoint;
    // 开始接受预约时间, 当need_appoint=0的时候可空
    private Date appointStartTime;
    // 停止接受预约时间, 当need_appoint=0的时候可空
    private Date appointEndTime;
    // 秒杀开始时间
    private Date startTime;
    // 秒杀结束时间
    private Date endTime;
    // 活动创建时间
    private Date createTime;
    // 是否已删除false(0)未删除,true(1)已删除
    private Boolean invalid;
    
    // 不参与持久化的属性, 用作查询条件, 秒杀结果时间大于某个时间值
    private Date endTimeGT;

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
        this.name = name == null ? null : name.trim();
    }

    public Boolean getNeedAppoint() {
        return needAppoint;
    }

    public void setNeedAppoint(Boolean needAppoint) {
        this.needAppoint = needAppoint;
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

	public Boolean getInvalid() {
		return invalid;
	}

	public void setInvalid(Boolean invalid) {
		this.invalid = invalid;
	}

	public Date getEndTimeGT() {
		return endTimeGT;
	}

	public void setEndTimeGT(Date endTimeGT) {
		this.endTimeGT = endTimeGT;
	}
}