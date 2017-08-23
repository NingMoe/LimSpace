 /**
  * 文件名[fileName]：BillService.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年6月17日 下午3:25:34
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.service;

import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;

/**
  *<p>类描述：。</p>
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.6。
  *<p>创建日期：2016年6月17日 下午3:25:34。</p>
  */

public interface BillService {
	 /**
	  * <p>功能描述：。</p>	
	  * @param startCloseTime 订单开始时间 
	  * @param endCloseTime 订单结束时间
	  * @param startReturnTime 退单开始时间
	  * @param endReturnTimernTime 退单结束时间
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月4日 上午10:20:04。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	SearchResult<Map<String, Object>> findBillList(Map<String, Object> param);

	 /**
	  * <p>功能描述：。</p>	
	  * @param param
	  * @return
	  * <p>创建日期:2016年9月6日 上午9:08:28。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	long findBillTotal(Map<String, Object> param);
}
