 /**
  * 文件名[fileName]：BillServiceImpl.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年6月17日 下午3:26:31
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.repository.OrderDao;
import com.tyiti.easycommerce.service.BillService;

/**
  *<p>类描述：。</p>
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.6。
  *<p>创建日期：2016年6月17日 下午3:26:31。</p>
  */
@Service
public class BillServiceImpl implements BillService {
	@Autowired
	OrderDao orderDao;
	 /**
	  * <p>功能描述:。</p>	
	  * @param startCloseTime
	  * @param endCloseTime
	  * @param startReturnTime
	  * @param endReturnTimernTime
	  * @return
	  * <p>创建日期2016年8月2日 下午2:10:57。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public SearchResult<Map<String, Object>> findBillList(Map<String, Object> param) {
		SearchResult<Map<String, Object>> searchResult =new SearchResult<Map<String, Object>>();
		 String skuErpCode  ="koo";
		 String startCloseTime = (String) param.get("startCloseTime");
		 String endCloseTime = (String) param.get("endCloseTime");
		 String startReturnTime = (String) param.get("startReturnTime");
		 String endReturnTime = (String) param.get("endReturnTime");
		 String strOffset = (String) param.get("offset");
		 String strLimit = (String) param.get("limit");
		 Integer offset = 0;
		 Integer limit = 0;
		 if(StringUtils.isNotEmpty(strOffset)&&StringUtils.isNotBlank(strOffset)){
			 offset = Integer.parseInt(strOffset);
		 }
		 if(StringUtils.isNotEmpty(strLimit)&&StringUtils.isNotBlank(strLimit)){
			 limit = Integer.parseInt(strLimit);
		 }
		searchResult.setRows(orderDao.findBillList(skuErpCode,startCloseTime,endCloseTime,startReturnTime,endReturnTime,offset,limit));
		searchResult.setTotal(orderDao.findBillTotal(skuErpCode,startCloseTime,endCloseTime,startReturnTime,endReturnTime,offset,limit));
		return searchResult;
	}
	 /**
	  * <p>功能描述:。</p>	
	  * @param param
	  * @return
	  * <p>创建日期2016年9月6日 上午9:09:16。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public long findBillTotal(Map<String, Object> param) {
		String skuErpCode  ="koo";
		 String startCloseTime = (String) param.get("startCloseTime");
		 String endCloseTime = (String) param.get("endCloseTime");
		 String startReturnTime = (String) param.get("startReturnTime");
		 String endReturnTime = (String) param.get("endReturnTime");
		 String strOffset = (String) param.get("offset");
		 String strLimit = (String) param.get("limit");
		 Integer offset = 0;
		 Integer limit = 0;
		 if(StringUtils.isNotEmpty(strOffset)&&StringUtils.isNotBlank(strOffset)){
			 offset = Integer.parseInt(strOffset);
		 }
		 if(StringUtils.isNotEmpty(strLimit)&&StringUtils.isNotBlank(strLimit)){
			 limit = Integer.parseInt(strLimit);
		 }
		return orderDao.findBillTotal(skuErpCode,startCloseTime,endCloseTime,startReturnTime,endReturnTime,offset,limit);
	}

}
