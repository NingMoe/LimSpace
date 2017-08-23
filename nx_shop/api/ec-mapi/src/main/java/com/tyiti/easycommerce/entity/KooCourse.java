package com.tyiti.easycommerce.entity;

import java.util.Date;

public class KooCourse {
    private Integer id;

    private Integer kooId;

    private String kooProductId;

    private Integer kooParentId;

    private String name;

    private String hourLength;

    private String courseTeacher;

    private Boolean invalid;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKooId() {
        return kooId;
    }

    public void setKooId(Integer kooId) {
        this.kooId = kooId;
    }

    public String getKooProductId() {
        return kooProductId;
    }

    public void setKooProductId(String kooProductId) {
        this.kooProductId = kooProductId == null ? null : kooProductId.trim();
    }

    public Integer getKooParentId() {
        return kooParentId;
    }

    public void setKooParentId(Integer kooParentId) {
        this.kooParentId = kooParentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getHourLength() {
        return hourLength;
    }

    public void setHourLength(String hourLength) {
        this.hourLength = hourLength == null ? null : hourLength.trim();
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher == null ? null : courseTeacher.trim();
    }

    public Boolean getInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean invalid) {
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