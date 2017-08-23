package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;

import com.tyiti.easycommerce.entity.Headline;
import com.tyiti.easycommerce.entity.SkuShelvesSchedule;
import com.tyiti.easycommerce.entity.Warning;

public interface WarningService {

	/**
	 * @description 添加预警
	 * @param param
	 *            warningType String 预警类型（sku_warning_all、sku_warning_part）
	 *            inventory Integer 预警值 userList List<WarningUser> 预警用户列表
	 *            skuIdList List<Integer> 预警skuId数据
	 *            当warningType=sku_warning_part时
	 * @param response
	 * @return
	 */
	Map<String, Object> addWarning(Warning warning,
			HttpServletResponse response);

	/**
	 * 删除部分预警的sku
	 * 
	 * @param param
	 *            skuIdList List<Integer> 预警skuId数据
	 * @param response
	 * @return
	 */
	Map<String, Object> delWarningSku(Warning warning,
			HttpServletResponse response);

	/**
	 * 要删除的预警用户
	 * 
	 * @param param
	 *            warningType String 预警类型 userId Integer 用户Id
	 * @param response
	 * @return
	 */
	Map<String, Object> delWarningUser(Warning warning,
			HttpServletResponse response);

	/**
	 * 获取预警用户
	 * 
	 * @param param
	 *            warningType String 预警类型
	 * @param response
	 * @return
	 */
	Map<String, Object> getWarningUser(Map<String, Object> param,
			HttpServletResponse response);

	/**
	 * 获取部分的预警sku
	 * 
	 * @param param
	 * @param response
	 * @return
	 */
	Map<String, Object> getWarningSku(HttpServletResponse response);

	/**
	 * 定时上下架
	 * 
	 * @param param
	 *            startDate 开始时间 endDate 结束时间 skuList skuList 只存在 startDate
	 *            type=上架 只存在 endDate type=下架 两个时间都存在是 type=上下架
	 * @return
	 */
	Map<String, Object> addTimingShelves(SkuShelvesSchedule sss,
			HttpServletResponse response);

	/**
	 * 获取定时上下架
	 * 
	 * @param param
	 *            type =上架、 下架 、上下架
	 * @return
	 */
	Map<String, Object> getTimingShelves(
			HttpServletResponse response,SkuShelvesSchedule sssm);

	/**
	 * 结束指定 定时上下架
	 * 
	 * @param param
	 *            skuId 要结束的skuid
	 * @return
	 */
	Map<String, Object> endTimingShelves(Integer id,
			HttpServletResponse response);

	Map<String, Object> delTimingShelves(Integer id,
			HttpServletResponse response);
	
	/**
	 * 上下架定时器
	 * 
	 * @return
	 */
	void addtimingSku();
	
	/**
	 * 添加头条
	 * @param param
	 * @param response
	 * @return
	 */
	Map<String, Object> addHeadline(Headline headline,HttpServletResponse response);
	
	/**
	 * 获取头条
	 * @param param
	 * @param response
	 * @return
	 */
	Map<String, Object> getHeadlineList(HttpServletResponse response);
	
	Map<String, Object> getHeadline(Integer id,HttpServletResponse response);
	
	Map<String, Object> updateHeadline(Headline headline,HttpServletResponse response);
	
	/**
	 * 结束、删除头条
	 * @param param
	 * @param response
	 * @return
	 */
	Map<String, Object> delHeadline(Integer id,HttpServletResponse response);
	
	/**
	 * 获取定时上下架除已设置的SKUList
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> selectTagLists(Map<String, Object> param);
}