package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.OrderSkuActivity;

public interface OrderSkuActivityDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderSkuActivity record);

    int insertSelective(OrderSkuActivity record);

    OrderSkuActivity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderSkuActivity record);

    int updateByPrimaryKey(OrderSkuActivity record);
	 /**
	  * <p>功能描述：批量插入信息。</p>	
	  * @param skuActivityList
	  * <p>创建日期:2016年8月4日 下午5:25:13。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	void insertOrderSkuActivityList(@Param("skuActivityList")List<OrderSkuActivity> skuActivityList);
	 /**
	  * <p>功能描述：。</p>	
	  * @param orderId
	  * @param skuId
	  * @return
	  * <p>创建日期:2016年8月4日 下午7:55:54。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	OrderSkuActivity findOrderSkuActivity(@Param("orderId")Integer orderId, @Param("skuId")Integer skuId);

	 /**
	  * <p>功能描述：根据orderId，skuID查询信息。</p>	
	  * @param orderId
	  * @param skuId
	  * @return
	  * <p>创建日期:2016年8月10日 下午2:46:39。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<OrderSkuActivity> findOrderSkuActivityList(@Param("orderId")Integer orderId, @Param("skuId")Integer skuId);
}