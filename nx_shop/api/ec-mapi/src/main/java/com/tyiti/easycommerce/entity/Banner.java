package com.tyiti.easycommerce.entity;

import java.util.Date;

public class Banner {
    private Integer id;

    private String picUrl;

    private String url;

    private Integer rank;

    private String title;

    private Date createTime;

    private Date updateTime;

    private Integer invalid;

    private Integer adId;
    //移动到哪个位置
    private Integer toRank ; 
    
     

	public Integer getToRank() {
		return toRank;
	}

	public void setToRank(Integer toRank) {
		this.toRank = toRank;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title == null ? null : title.trim();
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

	public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }
}