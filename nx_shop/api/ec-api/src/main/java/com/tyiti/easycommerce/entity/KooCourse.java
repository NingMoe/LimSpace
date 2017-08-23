package com.tyiti.easycommerce.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    
    private List<KooCourse> childrenCourses = new ArrayList<KooCourse>();

	
	/**
	 * id
	 *
	 * @return  the id
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Integer getId() {
		return id;
	}

	
	/**
	 * @param id the id to set
	 */
	
	public void setId(Integer id) {
		this.id = id;
	}

	
	/**
	 * kooId
	 *
	 * @return  the kooId
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Integer getKooId() {
		return kooId;
	}

	
	/**
	 * @param kooId the kooId to set
	 */
	
	public void setKooId(Integer kooId) {
		this.kooId = kooId;
	}

	
	/**
	 * kooProductId
	 *
	 * @return  the kooProductId
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public String getKooProductId() {
		return kooProductId;
	}

	
	/**
	 * @param kooProductId the kooProductId to set
	 */
	
	public void setKooProductId(String kooProductId) {
		this.kooProductId = kooProductId;
	}

	
	/**
	 * kooParentId
	 *
	 * @return  the kooParentId
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Integer getKooParentId() {
		return kooParentId;
	}

	
	/**
	 * @param kooParentId the kooParentId to set
	 */
	
	public void setKooParentId(Integer kooParentId) {
		this.kooParentId = kooParentId;
	}

	
	/**
	 * name
	 *
	 * @return  the name
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public String getName() {
		return name;
	}

	
	/**
	 * @param name the name to set
	 */
	
	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * hourLength
	 *
	 * @return  the hourLength
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public String getHourLength() {
		return hourLength;
	}

	
	/**
	 * @param hourLength the hourLength to set
	 */
	
	public void setHourLength(String hourLength) {
		this.hourLength = hourLength;
	}

	
	/**
	 * courseTeacher
	 *
	 * @return  the courseTeacher
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public String getCourseTeacher() {
		return courseTeacher;
	}

	
	/**
	 * @param courseTeacher the courseTeacher to set
	 */
	
	public void setCourseTeacher(String courseTeacher) {
		this.courseTeacher = courseTeacher;
	}

	
	/**
	 * invalid
	 *
	 * @return  the invalid
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Boolean getInvalid() {
		return invalid;
	}

	
	/**
	 * @param invalid the invalid to set
	 */
	
	public void setInvalid(Boolean invalid) {
		this.invalid = invalid;
	}

	
	/**
	 * createTime
	 *
	 * @return  the createTime
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Date getCreateTime() {
		return createTime;
	}

	
	/**
	 * @param createTime the createTime to set
	 */
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	/**
	 * updateTime
	 *
	 * @return  the updateTime
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Date getUpdateTime() {
		return updateTime;
	}

	
	/**
	 * @param updateTime the updateTime to set
	 */
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	
	/**
	 * childrenCourses
	 *
	 * @return  the childrenCourses
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public List<KooCourse> getChildrenCourses() {
		return childrenCourses;
	}

	
	/**
	 * @param childrenCourses the childrenCourses to set
	 */
	
	public void setChildrenCourses(List<KooCourse> childrenCourses) {
		this.childrenCourses = childrenCourses;
	}

	
	
	
   
}