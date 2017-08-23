package com.tyiti.easycommerce.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Activity {
    private Integer id;

    private Integer activityType;

    private Integer activityMode;

    private String activityName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    private String activityRemarks;

    private Integer activityStatus;

    private Integer invalid;

    private String coupon;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    
    private Integer display;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    
    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
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

    public String getActivityRemarks() {
        return activityRemarks;
    }

    public void setActivityRemarks(String activityRemarks) {
        this.activityRemarks = activityRemarks == null ? null : activityRemarks.trim();
    }
	/**
	 * activityType
	 *
	 * @return  the activityType
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Integer getActivityType() {
		return activityType;
	}

	
	/**
	 * @param activityType the activityType to set
	 */
	
	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}

	
	/**
	 * activityMode
	 *
	 * @return  the activityMode
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Integer getActivityMode() {
		return activityMode;
	}

	
	/**
	 * @param activityMode the activityMode to set
	 */
	
	public void setActivityMode(Integer activityMode) {
		this.activityMode = activityMode;
	}

	
	/**
	 * activityStatus
	 *
	 * @return  the activityStatus
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Integer getActivityStatus() {
		return activityStatus;
	}

	
	/**
	 * @param activityStatus the activityStatus to set
	 */
	
	public void setActivityStatus(Integer activityStatus) {
		this.activityStatus = activityStatus;
	}

	
	/**
	 * invalid
	 *
	 * @return  the invalid
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Integer getInvalid() {
		return invalid;
	}

	
	/**
	 * @param invalid the invalid to set
	 */
	
	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
	}

	public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon == null ? null : coupon.trim();
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

	
	/**
	 * display
	 *
	 * @return  the display
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Integer getDisplay() {
		return display;
	}

	
	/**
	 * @param display the display to set
	 */
	
	public void setDisplay(Integer display) {
		this.display = display;
	}
    
}