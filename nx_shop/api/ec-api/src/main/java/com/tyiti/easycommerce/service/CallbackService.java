 /**
  * 文件名[fileName]：CallbackService.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年7月7日 下午5:23:50
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
  *<p>类描述：。</p>
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.7。
  *<p>创建日期：2016年7月7日 下午5:23:50。</p>
  */

public interface CallbackService {

	 /**
	  * <p>功能描述：。</p>	
	  * @param map
	  * @param payMenth
	  * @param reqeust
	  * @param response
	 * @return 
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月7日 下午5:41:03。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Map<String, Object> callback(Map<String, Object> map, String payMenth, HttpServletRequest reqeust, HttpServletResponse response);

}
