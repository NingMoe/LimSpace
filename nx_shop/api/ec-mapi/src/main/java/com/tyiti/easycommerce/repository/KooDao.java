package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.KooSku;

public interface KooDao {
    int deleteByPrimaryKey(Integer id);

    int insert(KooSku sku);

    int insertSelective(KooSku sku);

    KooSku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(KooSku sku);

    int updateByPrimaryKeyWithBLOBs(KooSku sku);

    int updateByPrimaryKey(KooSku sku);

	List<Sku> getSkuBySpuId(Integer spuId);

	void updateDeleteBySpuId(int spuId);

	void deleteNotInByIds(@Param("ids") int[] ids,@Param("spuId") int spuId);

	 /**
	  * <p>功能描述：新东方。</p>	
	  * @param productId
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月20日 下午1:59:46。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	KooSku findSkuProductId(String productId);
	 /**
	  * <p>功能描述：根据条件查询SKu列表。</p>	
	  * @param status
	  * @param supplierId
	  * @param skuName
	  * @param spuId
	  * @param categoryId
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月3日 上午11:11:47。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	//List<KooSku> findSkuList(@Param("status")Integer status, @Param("supplierId")Integer supplierId, @Param("skuName")String skuName, @Param("spuId")Integer spuId, @Param("categoryId")Integer categoryId);

	 /**
	  * <p>功能描述：。</p>	
	  * @param category_id
	  * @param spu_id
	  * @param suppler_id
	  * @param suppler_id2
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月13日 上午11:36:24。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<KooSku> findSkuList(@Param("categoryId")Integer category_id, @Param("spuId")Integer spu_id, @Param("supplierId")Integer suppler_id, @Param("status")Integer status);

	 /**
	  * <p>功能描述：。</p>	
	  * @param string
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月22日 下午2:31:19。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<String> findBySkuProductIdList(@Param("erpCode")String erpCode);


}