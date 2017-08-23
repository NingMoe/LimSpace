package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.SkuShelvesSchedule;

public interface SkuShelvesScheduleDao {

	Integer insert(SkuShelvesSchedule skuShelvesSchedule);

	/**
	 * 新增标签到上下架列表
	 * 
	 * @param tagSkus
	 *            sssList 列表
	 */
	void insertRows(@Param("sssList") List<SkuShelvesSchedule> sssList);

	List<SkuShelvesSchedule> getSSSList(SkuShelvesSchedule skuShelvesSchedule);
	
	List<SkuShelvesSchedule>getskuSSSList(SkuShelvesSchedule skuShelvesSchedule);
	
	Integer getskuSSSListCount(SkuShelvesSchedule skuShelvesSchedule);

	Integer endSSSById(Integer id);

	Integer endSSSBySkuId(Integer skuId);

	/**
	 * 查询上架
	 */
	List<SkuShelvesSchedule> getOnTimeList();
	
	/**
	 * 上架
	 */
	void timeOnShelves(@Param("skuIdsList") List<SkuShelvesSchedule> skuIdsList);

	/**
	 * 查询下架
	 */
	List<SkuShelvesSchedule> getOffTimeList();
	
	/**
	 * 下架
	 */
	void timeOffShelves(@Param("skuIdsList") List<SkuShelvesSchedule> skuIdsList);

	/**
	 * 定时修改状态
	 */
	void timeShelvesInvalid();
	
	void deleteByPrimaryKey(SkuShelvesSchedule skuShelvesSchedule);
	
	SkuShelvesSchedule getSSS(SkuShelvesSchedule skuShelvesSchedule);
	
	List<Map<String, Object>> selectTagLists(Map<String, Object> param);
}