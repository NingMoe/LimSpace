package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.tyiti.easycommerce.entity.Sku;

public interface SkuService {
	Sku addSku(Sku sku);

    List<Sku> getByCriteria(Sku sku);

    Sku getById(Integer id);

    /**
     * 按sku_id获取商品详情
     * @param id
     * @return
     */
    Map<String, Object> findDetailById(HttpSession session,Integer id);
    /**
     * 减库存
     * @param skuCount
     * @param id
     * @return
     */
    int subtractInventory(Integer orderId);
	/**
     * 根据搜索字段找出sku
     * @param String querytext,String sortfield,String sorttype
     * @return List<Sku>
     */
    Map<String, Object> getSkusByQueryText(String querytext,String sortfield,String sorttype);
 /**
	  * <p>功能描述：活动结束返还商品库存。</p>	
	  * @param sku
	  * <p>创建日期:2016年9月14日 下午5:22:35。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	void updateSku(Sku sku);
    /**
     * 
     * @param skuId
     * @param count
     */
    void WarningSkuInventory(Integer skuId,Integer count);}
