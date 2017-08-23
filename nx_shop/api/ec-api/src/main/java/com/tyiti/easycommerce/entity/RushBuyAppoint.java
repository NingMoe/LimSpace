package com.tyiti.easycommerce.entity;

import java.util.Date;

/**
 * 秒杀活动预约信息
 * @author rainyhao
 * @since 2016-3-30 下午4:23:01
 */
public class RushBuyAppoint {
	// id
    private Integer id;
    // 预约用户
    private Integer custId;
    // 所预约的秒杀活动id
    private Integer rushBuyId;
    // 姓名
    private String name;
    // 手机号
    private String mobile;
    // 信合通银行卡号
    private String bankCardNo;
    // 预约时间
    private Date createTime;
    // 最后更新时间
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getRushBuyId() {
        return rushBuyId;
    }

    public void setRushBuyId(Integer rushBuyId) {
        this.rushBuyId = rushBuyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo == null ? null : bankCardNo.trim();
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