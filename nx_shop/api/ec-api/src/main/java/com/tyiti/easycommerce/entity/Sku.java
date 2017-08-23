package com.tyiti.easycommerce.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Sku {
	private Integer id;
	private String name;
	private String description;
	private String detail;
	private Integer spuId;
	private String erpCode;
	private Boolean invalid;
	private BigDecimal originalPrice;
	private BigDecimal price;
	private String installment;
	private String headThumbnail;
	private String imagesThumbnail;
	private String imagesOriginal;
	private Boolean isDefault;
	private List<SkuAttribute> attrs;
	private Integer warningInventory;
	
	public Integer getWarningInventory() {
		return warningInventory;
	}

	public void setWarningInventory(Integer warningInventory) {
		this.warningInventory = warningInventory;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	private Integer inventory;
	private Spu spu;
    private String remarks1;//新东方productId
    private String remarks2;//新东方课程有效期
    private String remarks3;//新东方课程课时
    private Integer status;//0表示下架1表示上架
    private Boolean IsFavorite;

    private Integer pickup	;//自提商品 0  不自提 1 自提
    
    private int num;//购物车每种sku的数量
    
    private ActivitySku activeSku ; //每种商品最多有一个正在进行的活动
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public List<SkuAttribute> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<SkuAttribute> attrs) {
		this.attrs = attrs;
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
		this.name = name;
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

	public Integer getSpuId() {
		return spuId;
	}

	public void setSpuId(Integer spuId) {
		this.spuId = spuId;
	}

	public String getErpCode() {
		return erpCode;
	}

	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}

	public Boolean getInvalid() {
		return invalid;
	}

	public void setInvalid(Boolean invalid) {
		this.invalid = invalid;
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
		this.installment = installment;
	}

	public String getHeadThumbnail() {
		return headThumbnail;
	}

	public void setHeadThumbnail(String headThumbnail) {
		this.headThumbnail = headThumbnail;
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

	public Spu getSpu() {
		return spu;
	}

	public void setSpu(Spu spu) {
		this.spu = spu;
	}

	public String getRemarks1() {
		return remarks1;
	}

	public void setRemarks1(String remarks1) {
		this.remarks1 = remarks1;
	}

	public String getRemarks2() {
		return remarks2;
	}

	public void setRemarks2(String remarks2) {
		this.remarks2 = remarks2;
	}

	public String getRemarks3() {
		return remarks3;
	}

	public void setRemarks3(String remarks3) {
		this.remarks3 = remarks3;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getIsFavorite() {
		return IsFavorite;
	}

	public void setIsFavorite(Boolean isFavorite) {
		IsFavorite = isFavorite;
	}
	
	public Integer getPickup() {
		return pickup;
	}
	public void setPickup(Integer pickup) {
		this.pickup = pickup;
	}
	
public Sku() {
		
	}

	public Sku(Integer id, String name, String description, String detail,
			Integer spuId, String erpCode, Boolean invalid,
			BigDecimal originalPrice, BigDecimal price, String installment,
			String headThumbnail, String imagesThumbnail,
			String imagesOriginal, Boolean isDefault, List<SkuAttribute> attrs,
			Date createTime, Date updateTime, Integer inventory, Spu spu,
			String remarks1, String remarks2, String remarks3, Integer status,
			Integer pickup, int num) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.detail = detail;
		this.spuId = spuId;
		this.erpCode = erpCode;
		this.invalid = invalid;
		this.originalPrice = originalPrice;
		this.price = price;
		this.installment = installment;
		this.headThumbnail = headThumbnail;
		this.imagesThumbnail = imagesThumbnail;
		this.imagesOriginal = imagesOriginal;
		this.isDefault = isDefault;
		this.attrs = attrs;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.inventory = inventory;
		this.spu = spu;
		this.remarks1 = remarks1;
		this.remarks2 = remarks2;
		this.remarks3 = remarks3;
		this.status = status;
		this.pickup = pickup;
		this.num = num;
	}

	public ActivitySku getActiveSku() {
		return activeSku;
	}

	public void setActiveSku(ActivitySku activeSku) {
		this.activeSku = activeSku;
	}
}
