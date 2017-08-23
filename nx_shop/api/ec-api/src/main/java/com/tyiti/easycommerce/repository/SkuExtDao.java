package com.tyiti.easycommerce.repository;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.SkuExt;

public interface SkuExtDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SkuExt record);

    int insertSelective(SkuExt record);

    SkuExt selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SkuExt record);

    int updateByPrimaryKey(SkuExt record);

	 /**
	  * <p>功能描述：根据新东方产品ID查询数据 一个产品ID对应一个SKUid。</p>	
	 * @param i 
	  * @param skuId
	  * @returnd
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月3日 下午6:55:49。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	SkuExt findKooProductId(@Param("type")int type,@Param("sku_id") Integer skuId);

	 /**
	  * <p>功能描述：SKUid查询扩展。</p>	
	  * @param skuId
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月1日 上午11:17:19。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	SkuExt findSkuExtBySkuId(@Param("sku_id") Integer skuId);
}