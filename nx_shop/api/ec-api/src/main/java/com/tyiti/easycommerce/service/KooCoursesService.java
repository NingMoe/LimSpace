package com.tyiti.easycommerce.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.SkuExt;

@Service
public interface KooCoursesService {
	/**
	 * 根据kooSkuId获取课程表列表
	 * @param productId
	 * @return
	 */
	public Map<String, Object> findListById(String productId);
	 /**
	  * <p>功能描述：。</p>	
	  * @param skuId
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月3日 下午8:15:20。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	public SkuExt findSkuExtSkuId(Integer skuId);
	
}