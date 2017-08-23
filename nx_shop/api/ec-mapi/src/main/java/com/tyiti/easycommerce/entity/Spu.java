package com.tyiti.easycommerce.entity;

import java.util.Date;
import java.util.List;

public class Spu {
    private Integer id;

    private String name;

    private String subName;

    private Integer categoryId;

    private int invalid;

    private String thumbnail;

    private Integer rank;

    private Date createTime;

    private Date updateTime;

    private String description;
    
    private Integer supplierId;

    private List<SpuAttribute> spuAttributeList ; 
    
    private List<SpuSpec> spuSpecList ; 

    private List<Sku> skuList; 
	public List<SpuSpec> getSpuSpecList() {
		return spuSpecList;
	}

	public void setSpuSpecList(List<SpuSpec> spuSpecList) {
		this.spuSpecList = spuSpecList;
	}

	public List<SpuAttribute> getSpuAttributeList() {
		return spuAttributeList;
	}

	public void setSpuAttributeList(List<SpuAttribute> spuAttributeList) {
		this.spuAttributeList = spuAttributeList;
	}

	public List<Sku> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<Sku> skuList) {
		this.skuList = skuList;
	}

	
    
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMobileDescription() {
		return mobileDescription;
	}

	public void setMobileDescription(String mobileDescription) {
		this.mobileDescription = mobileDescription;
	}

	private String mobileDescription;
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

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName == null ? null : subName.trim();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

 

    public int getInvalid() {
		return invalid;
	}

	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}

	public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail == null ? null : thumbnail.trim();
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