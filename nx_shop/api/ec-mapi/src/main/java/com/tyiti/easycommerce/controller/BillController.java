 /**
  * 文件名[fileName]：BillController.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年6月17日 下午2:39:21
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.service.BillService;

/**
  *<p>类描述：对账信息查询，对账明细。</p>
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.6。
  *<p>创建日期：2016年6月17日 下午2:39:21。</p>
  */
@Controller
public class BillController {
	@Autowired
	BillService billService;
	/***
	  * <p>功能描述：查询账单明细。</p>	
	  * @param startCloseTime 订单开始时间 
	  * @param endCloseTime 订单结束时间
	  * @param startReturnTime 退单开始时间
	  * @param endReturnTime 退单结束时间
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月17日 下午2:41:43。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 */
	@RequestMapping(value="findBillList",method=RequestMethod.GET)
	@ResponseBody
	public SearchResult<Map<String, Object>> findBillList(@RequestParam Map<String, Object> param){
		SearchResult<Map<String, Object>> billList =new SearchResult<Map<String, Object>>();
		billList = billService.findBillList(param);
		return billList;
	}

}
