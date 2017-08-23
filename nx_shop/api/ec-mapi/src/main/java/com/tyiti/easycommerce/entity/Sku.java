package com.tyiti.easycommerce.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Sku {

	private Integer id;

    private Integer spuId;

    private String name;

    private String erpCode;
    private int status;

    private int invalid;   

    private BigDecimal originalPrice;

    private BigDecimal price;

    private String installment;

    private String headThumbnail;

    private Boolean isDefault;

    private Date createTime;

    private Date updateTime;

    private Integer inventory;

    private String description;

    private String detail;

    private String imagesThumbnail;

    private String imagesOriginal;
    
    private List<SkuAttribute> skuAttributeList ; 
    
    private Integer pickup ;
    
    /**
     * 库存预警值 wyy 2016/09/01
     */
    private Integer warningInventory; //库存预警（等于这个值时预警）
  
    
    public Integer getWarningInventory() {
		return warningInventory;
	}

	public void setWarningInventory(Integer warningInventory) {
		this.warningInventory = warningInventory;
	}

	public Integer getPickup() {
		return pickup;
	}

	public void setPickup(Integer pickup) {
		this.pickup = pickup;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getImagesThumbnail() {
		return imagesThumbnail;
	}

	public void setImagesThumbnail(String imagesThumbnail) {
		this.imagesThumbnail = imagesThumbnail;
	}

	public String getImagesOriginal() {
		return imagesOriginal;
	}

	public void setImagesOriginal(String imagesOriginal) {
		this.imagesOriginal = imagesOriginal;
	}

	public int getInvalid() {
		return invalid;
	}

	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}

 

	public List<SkuAttribute> getSkuAttributeList() {
		return skuAttributeList;
	}

	public void setSkuAttributeList(List<SkuAttribute> skuAttributeList) {
		this.skuAttributeList = skuAttributeList;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSpuId() {
        return spuId;
    }

    public void setSpuId(Integer spuId) {
        this.spuId = spuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode == null ? null : erpCode.trim();
    }

 

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment == null ? null : installment.trim();
    }

    public String getHeadThumbnail() {
        return headThumbnail;
    }

    public void setHeadThumbnail(String headThumbnail) {
        this.headThumbnail = headThumbnail == null ? null : headThumbnail.trim();
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}