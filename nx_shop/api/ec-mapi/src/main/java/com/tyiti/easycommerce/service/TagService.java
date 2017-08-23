package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.Tag;

public interface TagService {
	//增
	Map<String, Object> add(Tag tag);
	//改
	Map<String, Object> edit(Tag tag);
	//删除
	Map<String, Object> del(Tag tag);
	//排序
	Map<String, Object> rank(Tag tag);
	
	/**
	 * 获取 Tag 本身信息，不包含子节点
	 * @param id tagId
	 * @return Tag
	 */
	Tag getById(int id);
	
	List<Tag> getTagList(int parentId);
	
	List<Tag> getAllTags();
	
	/**
	 * (全量)保存标签关联的SKU列表
	 * @param id tagId
	 * @param skuIds skuId数组
	 */
	void saveTagSkus(Integer id, List<Integer> skuIds);

	boolean invalidateById(Integer id);
	void rank(int id, Integer rank);
	
	void toggleStatus(Integer id);
	List<Sku> getSkusByTag(int id);
	
	/**
	 * 按父标签id和层级、商品条数获取标签树及商品列表
	 * @param id
	 * @param level
	 * @param limit
	 * @return
	 */
	Map<String, Object> getSkuListByParentId(Integer id, Integer level, Integer limit);
	
	Map<String, Object> getSkuListByParentIdForTimeSku(Integer id, Integer level, Integer limit);
	
	Integer getTagIdByCode(String code);
	
	Map<String, Object> getSkuListByParentIdForWarning(Integer id, Integer level, Integer limit);
}
