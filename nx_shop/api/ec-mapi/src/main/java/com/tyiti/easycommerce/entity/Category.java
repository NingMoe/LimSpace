package com.tyiti.easycommerce.entity;

import java.util.Date;

public class Category {
    private Integer id;

    private String name;

    private Integer parentId;

    private Integer invalid;

    private Integer rank;

    private Date createTime;

    private Date updateTime;

    private String metaTitle;

    private int status;

    private String icon;

    private String metaDescription;

    private String metaKeywords;
    
    private Integer supplierId;
    
    private int toRank ;
    
    private boolean hasTemplate;
 
    
	public int getToRank() {
		return toRank;
	}

	public void setToRank(int toRank) {
		this.toRank = toRank;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	public String getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
 

    public Integer getInvalid() {
		return invalid;
	}

	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
	}

	public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle == null ? null : metaTitle.trim();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

	public boolean getHasTemplate() {
		return hasTemplate;
	}

	public void setHasTemplate(boolean hasTemplate) {
		this.hasTemplate = hasTemplate;
	}

	
	/**
	 * supplierId
	 *
	 * @return  the supplierId
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public Integer getSupplierId() {
		return supplierId;
	}

	
	/**
	 * @param supplierId the supplierId to set
	 */
	
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	
}