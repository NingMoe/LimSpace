 /**
  * 文件名[fileName]：KooPayOrderService.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年5月17日 下午1:37:52
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.service;

import java.util.Map;

import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.User;

/**
  *<p>类描述：。</p>
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.6。
  *<p>创建日期：2016年5月17日 下午1:37:52。</p>
  */

public interface KooPayOrderService {
	 /**
	  * <p>功能描述：。</p>	
	  * @param order_id
	  * @param user_id
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月18日 下午2:15:02。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	String getHttpClients(String order_id, Integer user_id);
	/**
	  * <p>功能描述：。</p>	
	  * @param order_id
	  * @param user_id
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月18日 下午3:23:02。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 */
	String getPushStringJson(Order orderEntity, User user);
	 /**
	  * <p>功能描述：。</p>	
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月12日 下午3:48:00。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Map<String,Object> pushOrderMessage(String no,Integer user_id);


}
