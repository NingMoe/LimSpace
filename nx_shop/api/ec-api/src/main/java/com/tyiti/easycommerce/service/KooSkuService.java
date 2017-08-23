package com.tyiti.easycommerce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.SkuAttribute;
import com.tyiti.easycommerce.entity.SpuAttribute;
import com.tyiti.easycommerce.repository.KooSkuDao;
import com.tyiti.easycommerce.repository.SkuAttributeDao;
import com.tyiti.easycommerce.repository.SpuAttributeDao;

/**
 * 新东方
 * @author wangqi
 * @date 2016年5月12日 上午9:51:37
 * @description TODO
 */
@Service
public class KooSkuService {
	@Autowired
	private KooSkuDao kooskuDao;
	@Autowired
	private SkuAttributeDao skuAttributeDao;
	@Autowired
	private SpuAttributeDao spuAttributeDao;

	public Sku addSku(Sku sku) {
		int affectedRows = kooskuDao.add(sku);
		if (affectedRows < 1) {
			return null;
		}
		Integer skuId = sku.getId();
		List<SkuAttribute> skuAttributeList = sku.getAttrs();
		if (skuAttributeList != null && skuAttributeList.size() > 0) {
			for (SkuAttribute skuAttribute : skuAttributeList) {
				skuAttribute.setSkuId(skuId);
			}
			skuAttributeDao.addList(skuAttributeList);
		}
		return kooskuDao.getById(skuId);
	}

	/**
	 * 按多种条件查询列表
	 */
	public List<Sku> getByCriteria(Sku sku) {
		return kooskuDao.getByCriteria(sku);
	}

	/**
	 * 商品详情
	 */
	public Sku getById(Integer id) {
		return kooskuDao.getById(id);
	}

	/**
	 * 商品详情 包含商品属性 等多种数据
	 */
	public Map<String, Object> findDetailById(Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Sku sku = kooskuDao.getById(id);
		map.put("sku", sku);

		List<SpuAttribute> spuAttrs = spuAttributeDao.getListBySpuId(sku
				.getSpuId());
		map.put("spuAttrs", spuAttrs);

		/*List<SkuAttribute> otherSkuAttrs = skuAttributeDao
				.getListBySpuIdAndNotSkuAttrId(sku.getId(), sku.getSpuId());
		map.put("otherSkuAttrs", otherSkuAttrs);*/

		map.put("code", 200);
		map.put("messsge", "OK");
		return map;
	}
}
