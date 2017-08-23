package com.tyiti.easycommerce.entity;

import java.util.Date;

public class PopularSearch{
	private Integer id;
	private String queryText;
	private Integer rank;
	private Boolean status;
	private Date createTime;
	private Integer rankOld;
	private Integer rankNew;
	private Integer rankMax;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getQueryText() {
		return queryText;
	}
	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Integer getRankOld() {
		return rankOld;
	}
	public void setRankOld(Integer rankOld) {
		this.rankOld = rankOld;
	}
	public Integer getRankNew() {
		return rankNew;
	}
	public void setRankNew(Integer rankNew) {
		this.rankNew = rankNew;
	}
	public Integer getRankMax() {
		return rankMax;
	}
	public void setRankMax(Integer rankMax) {
		this.rankMax = rankMax;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}